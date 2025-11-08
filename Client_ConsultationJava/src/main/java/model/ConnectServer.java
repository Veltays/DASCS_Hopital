package model;
import ProtocoleCAP.Reponse.*;
import ProtocoleCAP.Requete.*;
import model.entity.Consultation;
import protocol.Reponse;
import protocol.Requete;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.lang.*;

public class ConnectServer {
    private final String host;
    private final int port;


    public ConnectServer (String host, int port)
    {
        this.host=host;
        this.port=port;
    }

    public void Login(Requete requete)
    {
        try (Socket socket = new Socket("localhost", 6769))
        {
            try (ObjectOutputStream  dos = new ObjectOutputStream (socket.getOutputStream());
                 ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());)
            {
                System.out.println("[CLIENT] Envoi de la requête");
                dos.writeObject(requete);
                System.out.println("[CLIENT] Requête envoyée");

                Reponse_LOGIN reponse = (Reponse_LOGIN) dis.readObject();

                if(reponse.isValide())
                {
                    System.out.println("[CLIENT] Réponse du serveur : connexion réussie!");
                }
                else
                {
                    System.out.println("[CLIENT] Réponse du serveur : connexion refusée ");
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        catch (IOException e)
        {
            System.err.println("Erreur d’E/S : " + e.getMessage());
        }
    }

    public void Logout(String requete)
    {
        try(Socket socket = new Socket("localhost", 6769))
        {
            try(ObjectOutputStream  dos = new ObjectOutputStream (socket.getOutputStream());
                ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());)
            {
                System.out.println("[CLIENT] Envoi de la requête");
                dos.writeObject(requete);
                System.out.println("[CLIENT] User correctement déloggé");

            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur d’E/S : " + e.getMessage());
        }
    }

    public void AddConsultation(Requete requete)
    {
        try(Socket socket = new Socket("localhost",6769))
        {
            try(ObjectOutputStream  dos = new ObjectOutputStream (socket.getOutputStream());
                ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());)
            {
                System.out.println("[CLIENT] Envoi de la requête");
                dos.writeObject(requete);
                System.out.println("[CLIENT] Requête envoyée");

                Reponse_ADD_CONSULTATION reponse = (Reponse_ADD_CONSULTATION) dis.readObject();

                if(reponse.isValide())
                {
                    System.out.println("[CLIENT] ok!");
                }
                else
                {
                    System.out.println("[CLIENT] pas ok ! :(");
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur d’E/S : " + e.getMessage());
        }

    }

    public void AddPatient(Requete requete)
    {
        try(Socket socket = new Socket("localhost",6769))
        {
            try(ObjectOutputStream  dos = new ObjectOutputStream (socket.getOutputStream());
                ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());)
            {
                System.out.println("[CLIENT] Envoi de la requête");
                dos.writeObject(requete);
                System.out.println("[CLIENT] Requête envoyée");

                Reponse_ADD_PATIENT reponse = (Reponse_ADD_PATIENT) dis.readObject();

                System.out.println("[CLIENT] ok! patient ajouté avec l'id : "+ reponse.getIdAttribuer());

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur d’E/S : " + e.getMessage());
        }
    }

    public void Search_Consultation(Requete requete)
    {
        try(Socket socket = new Socket("localhost",6769))
        {
            try(ObjectOutputStream  dos = new ObjectOutputStream (socket.getOutputStream());
                ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());)
            {
                System.out.println("[CLIENT] Envoi de la requête");
                dos.writeObject(requete);
                System.out.println("[CLIENT] Requête envoyée");

                Reponse_SEARCH_CONSULTATIONS reponse = (Reponse_SEARCH_CONSULTATIONS) dis.readObject();

                for(Consultation c : reponse.getConsultationsList())
            {
                System.out.println("[CLIENT] " +c.toString());
            }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur d’E/S : " + e.getMessage());
        }
    }

    public void Delete_Consultation(Requete requete)
    {
        try(Socket socket = new Socket("localhost",6769))
        {
            try(ObjectOutputStream  dos = new ObjectOutputStream (socket.getOutputStream());
                ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());)
            {
                System.out.println("[CLIENT] Envoi de la requête");
                dos.writeObject(requete);
                System.out.println("[CLIENT] Requête envoyée");

                Reponse_DELETE_CONSULTATION reponse = (Reponse_DELETE_CONSULTATION) dis.readObject();

                if(reponse.isValide())
                {
                    System.out.println("[CLIENT] ok! consultation supprimée");
                }
                else
                {
                    System.out.println("[CLIENT] pas ok! pb lors de la suppression :(");
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur d’E/S : " + e.getMessage());
        }
    }

    public void Update_Consultation(Requete requete)
    {
        try(Socket socket = new Socket("localhost",6769))
        {
            try(ObjectOutputStream  dos = new ObjectOutputStream (socket.getOutputStream());
                ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());)
            {
                System.out.println("[CLIENT] Envoi de la requête");
                dos.writeObject(requete);
                System.out.println("[CLIENT] Requête envoyée");

                Reponse_UPDATE_CONSULTATION reponse = (Reponse_UPDATE_CONSULTATION) dis.readObject();

                if(reponse.isValide())
                {
                    System.out.println("[CLIENT] ok! consultation modifiée");
                }
                else
                {
                    System.out.println("[CLIENT] pas ok! pb lors de la modification :(");
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur d’E/S : " + e.getMessage());
        }
    }
}
