package model;

import ProtocoleCAP.Reponse.*;
import ProtocoleCAP.Requete.*;
import model.entity.Consultation;
import protocol.Requete;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectServer {
    private final String host;
    private final int port;

    public ConnectServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // ============================================================
    // Fonction générique d’envoi de requête
    // ============================================================
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

    // ============================================================
    // LOGIN
    // ============================================================
    public boolean Login(Requete requete) {
        Reponse_LOGIN reponse = (Reponse_LOGIN) sendRequest(requete);
        if (reponse != null && reponse.isValide()) {
            System.out.println("[CLIENT] Réponse du serveur : connexion réussie!");
            return true;
        }
        System.out.println("[CLIENT] Réponse du serveur : connexion refusée");
        return false;
    }

    // ============================================================
    // LOGOUT
    // ============================================================
    public void Logout(Requete requete) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream())) {
            System.out.println("[CLIENT] Envoi de la requête LOGOUT");
            dos.writeObject(requete);
            System.out.println("[CLIENT] User correctement déloggé");
        } catch (IOException e) {
            System.err.println("Erreur d’E/S : " + e.getMessage());
        }
    }

    // ============================================================
    // ADD CONSULTATION
    // ============================================================
    public boolean AddConsultation(Requete requete) {
        Reponse_ADD_CONSULTATION reponse = (Reponse_ADD_CONSULTATION) sendRequest(requete);
        if (reponse != null && reponse.isValide()) {
            System.out.println("[CLIENT] ok!");
            return true;
        }
        else {
            System.out.println("[CLIENT] pas ok ! :(");
            return false;
        }

    }

    // ============================================================
    // ADD PATIENT
    // ============================================================
    public boolean AddPatient(Requete requete) {
        Reponse_ADD_PATIENT reponse = (Reponse_ADD_PATIENT) sendRequest(requete);
        if (reponse.getIdAttribuer() == -1) {
            System.out.println("[CLIENT] pas ok ! :(");
            return false;
        } else {
            System.out.println("[CLIENT] ok! id attribué : " + reponse.getIdAttribuer());
            return true;
        }



    }

    // ============================================================
    // SEARCH CONSULTATIONS
    // ============================================================
    public List<Consultation> SearchConsultation(Requete requete) {

        Reponse_SEARCH_CONSULTATIONS reponse = (Reponse_SEARCH_CONSULTATIONS) sendRequest(requete);
        if (reponse != null && reponse.getConsultationsList() != null) {
            for (Consultation c : reponse.getConsultationsList())
                System.out.println("[CLIENT] " + c);
            return reponse.getConsultationsList();
        }
        System.out.println("vide");
        return new ArrayList<>();
    }

    // ============================================================
    // DELETE CONSULTATION
    // ============================================================
    public void DeleteConsultation(Requete requete) {
        Reponse_DELETE_CONSULTATION reponse = (Reponse_DELETE_CONSULTATION) sendRequest(requete);
        if (reponse != null && reponse.isValide())
            System.out.println("[CLIENT] ok! consultation supprimée");
        else
            System.out.println("[CLIENT] pas ok! pb lors de la suppression :(");
    }

    // ============================================================
    // UPDATE CONSULTATION
    // ============================================================
    public void UpdateConsultation(Requete requete) {
        Reponse_UPDATE_CONSULTATION reponse = (Reponse_UPDATE_CONSULTATION) sendRequest(requete);
        if (reponse != null && reponse.isValide())
            System.out.println("[CLIENT] ok! consultation modifiée");
        else
            System.out.println("[CLIENT] pas ok! pb lors de la modification :(");
    }
}
