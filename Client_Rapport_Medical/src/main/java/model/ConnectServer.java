package model;

import ProtocoleMRPS.Reponse.*;
import ProtocoleMRPS.Requete.*;
import protocol.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectServer {
    private final String host;
    private final int port;

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

    public boolean Login(Requete requete)
    {
        Reponse_LOGIN reponse = (Reponse_LOGIN) sendRequest(requete);
        if(reponse !=null && reponse.isValide())
        {
            System.out.println("[CLIENT] Réponse du serveur : connexion réussie!");
            return true;
        }
        System.out.println("[CLIENT] Réponse du serveur : connexion refusée");
        return false;
    }

    public boolean AddReport(Requete requete) {
        Reponse_ADD_REPORT reponse = (Reponse_ADD_REPORT) sendRequest(requete);
        if(reponse !=null && reponse.isValide())
        {
            System.out.println("[CLIENT] ok!");
            return true;
        }
        else {
            System.out.println("[CLIENT]  pas ok!");
            return false;
        }
    }
}
