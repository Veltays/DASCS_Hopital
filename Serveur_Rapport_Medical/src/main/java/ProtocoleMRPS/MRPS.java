package ProtocoleMRPS;

import ServeurGeneriqueTCP.logging.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ProtocoleMRPS.Requete.*;
import ProtocoleMRPS.Reponse.*;
import model.entity.Report;
import protocol.*;
import model.dao.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class MRPS implements Protocole
{
    private final Logger logger;
    private final UserDAO userDAO;
    private final ReportDAO reportDAO;
    private final DoctorDAO doctorDAO;
    private final ConsultationDAO consultationDAO;

    private final ConcurrentHashMap<String, String> salts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, String> clientsConnectes = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, SecretKey> sessionKeys = new ConcurrentHashMap<>();

    private PrivateKey clePriveeServeur;
    public MRPS(Logger log)
    {
        logger = log;
        userDAO = new UserDAO();
        reportDAO = new ReportDAO();
        doctorDAO = new DoctorDAO();
        consultationDAO = new ConsultationDAO();

        // Charger la clé privée RSA du serveur
        try {
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("clePriveeServeur.ser");
            if (is == null) {
                throw new FileNotFoundException("clePriveeServeur.ser introuvable dans resources");
            }
            ObjectInputStream ois = new ObjectInputStream(is);
            clePriveeServeur = (PrivateKey) ois.readObject();
            ois.close();
            logger.Trace("Clé privée serveur chargée");
        } catch (Exception e) {
            logger.Trace("ERREUR : impossible de charger la clé privée RSA");
            e.printStackTrace();
        }
    }

    @Override
    public String getNom() {
        return "MRPS";
    }
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket)
    {
        if (requete == null) {
            logger.Trace("Requête reçue est null !");
        }

        logger.Trace("Traitement d'une requête de type " + requete.getClass().getSimpleName() + "Pour l'utilisateur avec l'id connexion" + requete.getIdConnexion() );

        switch(requete)
        {
            case Requete_LOGIN_REQUEST requeteLogin -> {
                return TraiteRequeteLOGIN_REQUEST(requeteLogin, socket);
            }

            case Requete_LOGIN_AUTH requeteLoginAuth -> {
                return TraiteRequeteLogin_Auth(requeteLoginAuth, socket);
            }

            case Requete_ADD_REPORT requeteAddReport -> {
                return TraiteRequeteAddReport(requeteAddReport, socket);
            }
            case Requete_HANDSHAKE requeteHS -> {
                return TraiteRequeteHANDSHAKE(requeteHS, socket);
            }

            default -> {
                logger.Trace("Requête de type inconnu reçue : " + requete.getClass().getName());
                return new Reponse_ERROR("Type de requête non reconnu : " + requete.getClass().getSimpleName());
            }
        }

    }

    //////////////////////////////////////////////////////////////////
    ////////// LOGIN /////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////

    private synchronized Reponse_LOGIN_REQUEST TraiteRequeteLOGIN_REQUEST (Requete_LOGIN_REQUEST requete, Socket socket) {
        logger.Trace("***RequeteLOGIN_REQUEST reçue de " + requete.getUsername()+"***");

        String userName = requete.getUsername();

        // Vérifier l'utilisateur dans la DB
        if (userDAO.checkLogin(userName)) {

            // Vérifier si déjà connecté
            if (clientsConnectes.containsKey(userName)) {
                logger.Trace("Échec du login pour " + userName + " : déjà connecté.");
                return new Reponse_LOGIN_REQUEST("000",false);
            }

            // Construire la réponse
            // on génère le sel et on le renvoie
            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[32];
            random.nextBytes(saltBytes);
            String salt = Base64.getEncoder().encodeToString(saltBytes);

            //mémorier le sel du client
            salts.put(userName,salt);

            return new Reponse_LOGIN_REQUEST(salt,true);
        }

        else
        {
            return new Reponse_LOGIN_REQUEST("000",false); // cas où le login n'existait pas
        }
    }

    private synchronized Reponse_LOGIN_AUTH TraiteRequeteLogin_Auth (Requete_LOGIN_AUTH requete, Socket socket){
        logger.Trace("*** RequeteLOGIN_AUTH reçue ***");

        String username = requete.getUsername();
        String digestRecu = requete.getDigest();

        String password;
        try {
            password = userDAO.getPasswordByLogin(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Reponse_LOGIN_AUTH(false,0);
        }

        if (password == null) {
            return new Reponse_LOGIN_AUTH(false,0);
        }
        String salt = salts.get(username);
        if(salt==null) { return new Reponse_LOGIN_AUTH(false,0);}

        try{
            String digestAttendu = DigestHelper.generateDigest(username, password,salt);

            if(digestAttendu.equals(digestRecu))
            {
                // génération de l'id de connexion + placer dans la hashmap
                int idConnexion = Math.abs(UUID.randomUUID().hashCode());
                clientsConnectes.put(idConnexion,username);
                logger.Trace("Login OK pour " + username);
                logger.Trace("Id de connexion attribué : "+ idConnexion);
                return new Reponse_LOGIN_AUTH(true, idConnexion);
            } else {
                logger.Trace("Login KO pour " + username + " : digest différent");
                return new Reponse_LOGIN_AUTH(false,0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Reponse_LOGIN_AUTH(false,0);
        }
    }

    private synchronized Reponse_HANDSHAKE TraiteRequeteHANDSHAKE(Requete_HANDSHAKE requete, Socket socket) {
        logger.Trace("*** Requete_HANDSHAKE reçue ***");

        try {
            // 1) Récupérer le login via idConnexion
            String login = clientsConnectes.get(requete.getIdConnexion());
            if (login == null) {
                return new Reponse_HANDSHAKE(false, "Session invalide");
            }

            // 2) Déchiffrer la clé de session avec la clé privée RSA
            byte[] cleSessionChiffree = requete.getCleSessionChiffree();
            byte[] cleSessionClair = MyCrypto.DecryptAsymRSA(clePriveeServeur, cleSessionChiffree);

            // 3) Reconstruire la SecretKey AES
            SecretKey sessionKey = new SecretKeySpec(cleSessionClair, "AES");

            // 4) Stocker dans la map
            sessionKeys.put(login, sessionKey);

            logger.Trace("Clé de session enregistrée pour " + login);
            return new Reponse_HANDSHAKE(true, "Handshake OK");

        } catch (Exception e) {
            e.printStackTrace();
            logger.Trace("Erreur handshake");
            return new Reponse_HANDSHAKE(false, "Erreur serveur");
        }
    }


    private synchronized Reponse_ADD_REPORT TraiteRequeteAddReport(Requete_ADD_REPORT requete, Socket socket)  {
        logger.Trace("***** REQUETE ADD REPORT RECUE DU CLIENT *****");
        String login = clientsConnectes.get(requete.getIdConnexion());
        if (login == null) {
            logger.Trace("login nul?");
            return new Reponse_ADD_REPORT(false); // pas logué
        }
        SecretKey sessionKey = sessionKeys.get(login); // map login -> sessionKey
        if (sessionKey == null) {
            logger.Trace("clé de session nulle?");
            return new Reponse_ADD_REPORT(false); // handshake pas fait
        }
        try{byte[] messageClair = MyCrypto.DecryptSymAES(sessionKey, requete.getData());

        // décrypter le message

        ByteArrayInputStream bais = new ByteArrayInputStream(messageClair);
        DataInputStream dis = new DataInputStream(bais);

        int id = dis.readInt();
        int idPatient = dis.readInt();
        LocalDate date = LocalDate.parse(dis.readUTF());
        String description = dis.readUTF();
        //recup l'id du medecin
        Integer doctorID = doctorDAO.getDoctorIdByLogin(login);
        logger.Trace("***"+ doctorID+"***"+idPatient);

        // vérification à faire ici avec un booolean ok qu'on met à jour

        boolean okConsult = consultationDAO.existsForDoctorAndPatient(doctorID,idPatient);

        if(!okConsult)
        {
            logger.Trace("pas ok pcq okconsult pas ok");
            return new Reponse_ADD_REPORT(false);

        }

        Report reportToAdd = new Report(id, idPatient, doctorID, date, description);
        reportDAO.save(reportToAdd);
        logger.Trace("est ce que on arrive bien ici au moins?");
        return new Reponse_ADD_REPORT(true);
    }
        catch (Exception e) {
            e.printStackTrace();
            logger.Trace("pas ok pcq exception");
            return new Reponse_ADD_REPORT(false);

        }

    }

    public class DigestHelper {
        public static String generateDigest(String username, String password, String salt)
                throws Exception {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String data = username + password + salt;
            byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        }
    }

}
