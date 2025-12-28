package model;

import ProtocoleMRPS.Reponse.*;
import ProtocoleMRPS.Requete.*;
import protocol.Requete;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
public class ConnectServer {
    private final String host;
    private final int port;

    private SecretKey sessionKey;          // <-- ajout
    public SecretKey getSessionKey() {     // <-- ajout
        return sessionKey;
    }

    public ConnectServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Object sendRequest(Requete requete) {
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
            System.err.println("Erreur d’E/S : " + e.getMessage());
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
            String digest = DigestHelper.generateDigest(username, password, sel);
            Requete req = new Requete_LOGIN_AUTH(username, digest);
            Reponse_LOGIN_AUTH authRep = (Reponse_LOGIN_AUTH) sendRequest(req);

            if (authRep != null && authRep.isValide()) {
                System.out.println("[CLIENT] Authentification réussie");

                // Générer une clé de session pour les futurs rapports
                KeyGenerator cleGen = KeyGenerator.getInstance("AES");
                cleGen.init(128, new SecureRandom());
                this.sessionKey = cleGen.generateKey();
                System.out.println("[CLIENT] sessionKey générée : " + sessionKey.getAlgorithm());

                // (plus tard tu pourras l’envoyer chiffrée au serveur via une requête HANDSHAKE)
                return true;
            } else {
                System.out.println("[CLIENT] Authentification échouée");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Erreur lors du calcul du digest");
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

    public static class DigestHelper {
        public static String generateDigest(String username, String password, String salt)
                throws Exception {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String data = username + password + salt;
            byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        }
    }
}
