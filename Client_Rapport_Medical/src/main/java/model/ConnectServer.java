package model;

import ProtocoleMRPS.MyCrypto;
import ProtocoleMRPS.Reponse.*;
import ProtocoleMRPS.Requete.*;
import model.entity.Report;
import protocol.Requete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ConnectServer {
    private final String host;
    private final int port;

    private SecretKey sessionKey;
    private Integer IdConnexionWithServer;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public ConnectServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public SecretKey getSessionKey() {
        return sessionKey;
    }

    public Integer getIdConnexionWithServer() {
        return IdConnexionWithServer;
    }

    public void setIdConnexionWithServer(Integer idConnexionWithServer) {
        IdConnexionWithServer = idConnexionWithServer;
    }

    private Object sendRequest(Requete requete) {
        requete.setIdConnexion(getIdConnexionWithServer());
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream dis = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("[CLIENT] Envoi de la requête : " + requete.getClass().getSimpleName());
            dos.writeObject(requete);
            dos.flush();
            System.out.println("[CLIENT] Requête envoyée");

            Object response = dis.readObject();
            System.out.println("[CLIENT] Réponse reçue : " + response.getClass().getSimpleName());
            return response;

        } catch (IOException e) {
            System.err.println("Erreur d'E/S : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Classe non trouvée : " + e.getMessage());
        }
        return null;
    }

    public boolean LoginRequest(Requete requete, String username, String password)
    {
        Reponse_LOGIN_REQUEST reponse = (Reponse_LOGIN_REQUEST) sendRequest(requete);
        if (reponse == null || !reponse.isValide()) {
            System.out.println("[CLIENT] Réponse du serveur : connexion refusée car login inconnu");
            return false;
        }
        String sel = reponse.getSel();

        try {
            // 1) LOGIN_AUTH
            String digest = DigestHelper.generateDigest(username, password, sel);
            Requete_LOGIN_AUTH req = new Requete_LOGIN_AUTH(username, digest);
            Reponse_LOGIN_AUTH authRep = (Reponse_LOGIN_AUTH) sendRequest(req);

            if (authRep == null || !authRep.isValide()) {
                System.out.println("[CLIENT] Authentification échouée");
                return false;
            }

            System.out.println("[CLIENT] Authentification réussie");
            setIdConnexionWithServer(authRep.getIdConnexion());

            // 2) Générer clé de session AES
            KeyGenerator cleGen = KeyGenerator.getInstance("AES");
            cleGen.init(128, new SecureRandom());
            this.sessionKey = cleGen.generateKey();
            System.out.println("[CLIENT] sessionKey générée : " + sessionKey.getAlgorithm());

            // 3) Charger la clé publique du serveur (CONVERTIE EN BC)
            PublicKey clePubliqueServeur = loadServerPublicKey();

            // 4) Chiffrer la clé de session avec RSA
            byte[] sessionKeyBytes = sessionKey.getEncoded();
            byte[] sessionKeyChiffree = MyCrypto.CryptAsymRSA(clePubliqueServeur, sessionKeyBytes);

            // 5) Envoyer HANDSHAKE
            Requete_HANDSHAKE reqHS = new Requete_HANDSHAKE(sessionKeyChiffree);
            Reponse_HANDSHAKE repHS = (Reponse_HANDSHAKE) sendRequest(reqHS);

            if (repHS == null || !repHS.isOk()) {
                System.out.println("[CLIENT] Handshake échoué");
                return false;
            }

            System.out.println("[CLIENT] Handshake réussi, clé de session partagée");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Erreur lors du login/handshake");
            return false;
        }
    }

    public boolean AddReport(Requete requete) {
        Reponse_ADD_REPORT reponse = (Reponse_ADD_REPORT) sendRequest(requete);
        if (reponse != null && reponse.isValide()) {
            System.out.println("[CLIENT] ok!");
            return true;
        } else {
            System.out.println("[CLIENT] pas ok!");
            return false;
        }
    }

    public boolean Logout(Requete requete)
    {
        Reponse_LOGOUT reponse = (Reponse_LOGOUT) sendRequest(requete);
        if(reponse!=null && reponse.isOk()){
            System.out.println("[CLIENT] Déconnexion réussie!");

            this.sessionKey = null;
            this.setIdConnexionWithServer(null);
            return true;
        }
        else {
            System.out.println("[CLIENT] Erreur lors de la déconnexion...");
            return false;
        }
    }

    public List<Report> ListReport() throws Exception {
        try{
        Requete_LIST_REPORT req = new Requete_LIST_REPORT();
        Reponse_LIST_REPORT rep = (Reponse_LIST_REPORT) sendRequest(req);

        if (rep == null || !rep.isOk()) {
            System.out.println("[CLIENT] Échec récupération liste");
            return new ArrayList<>();
        }

        // Vérifier HMAC
        String hmacCalcule = calculateHMAC(rep.getDataChiffree(), sessionKey);
        if (!hmacCalcule.equals(rep.getHmac())) {
            System.err.println("[CLIENT] ERREUR : HMAC invalide, données corrompues !");
            return new ArrayList<>();
        }

        // Déchiffrer
        byte[] messageClair = MyCrypto.DecryptSymAES(sessionKey, rep.getDataChiffree());

        // Désérialiser
        ByteArrayInputStream bais = new ByteArrayInputStream(messageClair);
        DataInputStream dis = new DataInputStream(bais);

        int count = dis.readInt();
        List<Report> reports = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int id = dis.readInt();
            int idPatient = dis.readInt();
            LocalDate date = LocalDate.parse(dis.readUTF());
            String description = dis.readUTF();

            reports.add(new Report(id, idPatient, null, date, description));
        }

        System.out.println("[CLIENT] " + reports.size() + " rapports reçus");
        return reports;

    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
    }

    // CHARGEMENT + CONVERSION de la clé publique en BC
    private static PublicKey loadServerPublicKey() throws Exception {
        InputStream is = ConnectServer.class.getClassLoader()
                .getResourceAsStream("clePubliqueServeur.ser");
        if (is == null) {
            throw new FileNotFoundException("clePubliqueServeur.ser introuvable dans resources");
        }

        ObjectInputStream ois = new ObjectInputStream(is);
        PublicKey cleTemp = (PublicKey) ois.readObject();
        ois.close();

        // CONVERSION vers BC
        byte[] encoded = cleTemp.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        PublicKey cleBc = keyFactory.generatePublic(keySpec);

        System.out.println("[CLIENT] Clé publique chargée : " + cleBc.getClass().getName());
        return cleBc;
    }

    public static class DigestHelper {
        public static String generateDigest(String username, String password, String salt)
                throws Exception {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String data = username + password + salt;
            byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        }
    }

    private String calculateHMAC(byte[] data, SecretKey key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] hmacBytes = mac.doFinal(data);
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
