package ProtocoleMRPS;

import ServeurGeneriqueTCP.logging.Logger;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ProtocoleMRPS.Requete.*;
import ProtocoleMRPS.Reponse.*;
import protocol.*;
import model.dao.*;

import static jdk.internal.jrtfs.JrtFileAttributeView.AttrID.size;


public class MRPS implements Protocole
{
    private final Logger logger;
    private final UserDAO userDAO;

    private final ConcurrentHashMap<String, String> salts = new ConcurrentHashMap<>();

    public MRPS(Logger log)
    {
        logger = log;
        userDAO = new UserDAO();
    }

    @Override
    public String getNom() {
        return "MRPS";
    }
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException
    {
        if (requete == null) {
            logger.Trace("Requête reçue est null !");
        }

        logger.Trace("Traitement d'une requête de type " + requete.getClass().getSimpleName() + "Pour l'utilisateur avec l'id connexion" );

        switch(requete)
        {
            case Requete_LOGIN_REQUEST requeteLogin -> {
                return TraiteRequeteLOGIN_REQUEST(requeteLogin, socket);
            }

            case Requete_LOGIN_AUTH requeteLoginAuth -> {
                return TraiteRequeteLogin_Auth(requeteLoginAuth, socket);
            }

            case Requete_ADD_REPORT requeteAddReport -> {
                return TraiteRequeteAddReprt(requeteAddReport, socket);
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
        logger.Trace("RequeteLOGIN_REQUEST reçue de " + requete.getUsername());

        String userName = requete.getUsername();

        // Vérifier l'utilisateur dans la DB
        if (userDAO.checkLogin(userName)) {

            // Vérifier si déjà connecté
//            if (clientsConnectes.containsKey(userName)) {
//                logger.Trace("Échec du login pour " + userName + " : déjà connecté.");
//                return new Reponse_LOGIN(false);
//            }

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
        logger.Trace("RequeteLOGIN_AUTH reçue");

        String username = requete.getUsername();
        String digestRecu = requete.getDigest();

        String password = userDAO.getPasswordByLogin(requete.getUsername());
        String salt = salts.get(username);
        if(salt==null) { return new Reponse_LOGIN_AUTH(false);}

        try{
            String digestAttendu = DigestHelper.generateDigest(username, password,salt);

            if(digestAttendu.equals(digestRecu))
            {
                logger.Trace("Login OK pour " + username);
                return new Reponse_LOGIN_AUTH(true);
            } else {
                logger.Trace("Login KO pour " + username + " : digest différent");
                return new Reponse_LOGIN_AUTH(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Reponse_LOGIN_AUTH(false);
        }
    }

    private synchronized Reponse_ADD_REPORT TraiteRequeteAddReprt(Requete_ADD_REPORT requete, Socket socket){
        return new Reponse_ADD_REPORT(false);
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
