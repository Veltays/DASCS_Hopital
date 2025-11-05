package ProtocoleCAP;

import ProtocoleCAP.Reponse.*;
import ProtocoleCAP.Requete.*;
import ServeurGeneriqueTCP.logging.Logger;
import ServeurGeneriqueTCP.model.entity.Consultation;
import ServeurGeneriqueTCP.protocol.*;

import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

public class CAP implements Protocole {

    private final HashMap<String, String> passwords;
    private final HashMap<String, Socket> clientsConnectes;
    private final Logger logger;

    public CAP(Logger log) {
        passwords = new HashMap<>();
        passwords.put("wagner", "abcd");
        passwords.put("charlet", "1234");
        passwords.put("calmant", "azerty");
        logger = log;
        clientsConnectes = new HashMap<>();
    }

    @Override
    public String getNom() {
        return "CAP";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        if (requete == null) {
            logger.Trace("Requête reçue est null !");
            return new Reponse_ERROR("Requête vide ou non reconnue.");
        }

        logger.Trace("Traitement d'une requête de type " + requete.getClass().getSimpleName());

        switch (requete) {
            case Requete_LOGIN requeteLogin -> {
                return TraiteRequeteLOGIN(requeteLogin, socket);
            }
            case Requete_ADD_CONSULTATION requeteAddConsultation -> {
                return TraiteRequeteADD_CONSULTATION(requeteAddConsultation, socket);
            }
            case Requete_ADD_PATIENT requeteAddPatient -> {
                return TraiteRequeteADD_PATIENT(requeteAddPatient, socket);
            }
            case Requete_UPDATE_CONSULTATION requeteUpdateConsultation -> {
                return TraiteRequeteUPDATE_CONSULTATION(requeteUpdateConsultation, socket);
            }
            case Requete_SEARCH_CONSULTATIONS requeteSearchConsultations -> {
                return TraiteRequeteSEARCH_CONSULTATIONS(requeteSearchConsultations, socket);
            }
            case Requete_DELETE_CONSULTATION requeteDeleteConsultation -> {
                return TraiteRequeteDELETE_CONSULTATION(requeteDeleteConsultation, socket);
            }
            case Requete_LOGOUT requeteLogout -> {
                TraiteRequeteLOGOUT(requeteLogout);
            }
            default -> {
                logger.Trace("Requête de type inconnu reçue : " + requete.getClass().getName());
                return new Reponse_ERROR("Type de requête non reconnu : " + requete.getClass().getSimpleName());
            }
        }

        return null;
    }






    // ============================================================
    // 🔹 LOGIN
    // ============================================================
    private synchronized Reponse_LOGIN TraiteRequeteLOGIN(Requete_LOGIN requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());
        String password = passwords.get(requete.getLogin());

        if (password != null && password.equals(requete.getPassword())) {
            String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
            logger.Trace(requete.getLogin() + " correctement loggé depuis " + ipPortClient);
            clientsConnectes.put(requete.getLogin(), socket);
            return new Reponse_LOGIN(true);
        }

        logger.Trace(requete.getLogin() + " → erreur de login");
        throw new FinConnexionException(new Reponse_LOGIN(false));
    }









    // ============================================================
    // 🔹 ADD_CONSULTATION
    // ============================================================
    private synchronized Reponse_ADD_CONSULTATION TraiteRequeteADD_CONSULTATION(Requete_ADD_CONSULTATION requete, Socket socket) {
        logger.Trace("Ajout d'une consultation par " + socket.getInetAddress());
        return new Reponse_ADD_CONSULTATION(true);
    }








    // ============================================================
    // 🔹 ADD_PATIENT
    // ============================================================
    private synchronized Reponse_ADD_PATIENT TraiteRequeteADD_PATIENT(Requete_ADD_PATIENT requete, Socket socket) {
        logger.Trace("Ajout d'un patient : " + requete);
        return new Reponse_ADD_PATIENT(1); // simulate success
    }









    // ============================================================
    // 🔹 UPDATE_CONSULTATION
    // ============================================================
    private synchronized Reponse_UPDATE_CONSULTATION TraiteRequeteUPDATE_CONSULTATION(Requete_UPDATE_CONSULTATION requete, Socket socket) {
        logger.Trace("Mise à jour consultation ID=" + requete.getIdConsultation());
        return new Reponse_UPDATE_CONSULTATION(true);
    }







    // ============================================================
    // 🔹 SEARCH_CONSULTATIONS
    // ============================================================
    private synchronized Reponse_SEARCH_CONSULTATIONS TraiteRequeteSEARCH_CONSULTATIONS(Requete_SEARCH_CONSULTATIONS requete, Socket socket) {
        logger.Trace("Recherche consultations (critères : " + requete + ")");
        Reponse_SEARCH_CONSULTATIONS response = new Reponse_SEARCH_CONSULTATIONS();
        response.addConsultation(new Consultation(1, 1, 1, "8:00", new Date(), "Routine"));
        response.addConsultation(new Consultation(2, 2, 1, "10:00", new Date(), "Checkup"));
        return response;
    }








    // ============================================================
    // 🔹 DELETE_CONSULTATION
    // ============================================================
    private synchronized Reponse_DELETE_CONSULTATION TraiteRequeteDELETE_CONSULTATION(Requete_DELETE_CONSULTATION requete, Socket socket) {
        logger.Trace("Suppression consultation ID=" + requete.getIdConsultation());
        return new Reponse_DELETE_CONSULTATION(true);
    }







    // ============================================================
    // 🔹 LOGOUT
    // ============================================================
    private synchronized void TraiteRequeteLOGOUT(Requete_LOGOUT requete) throws FinConnexionException {
        logger.Trace("RequeteLOGOUT reçue de " + requete.getLogin());
        clientsConnectes.remove(requete.getLogin());
        logger.Trace(requete.getLogin() + " correctement déloggé");
    }
}
