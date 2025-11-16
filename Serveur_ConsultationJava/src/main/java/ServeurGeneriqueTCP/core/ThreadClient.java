package ServeurGeneriqueTCP.core;

import ServeurGeneriqueTCP.logging.Logger;
import protocol.FinConnexionException;
import protocol.Protocole;
import protocol.Reponse;
import protocol.Requete;

import java.io.*;
import java.net.Socket;

public abstract class ThreadClient extends Thread {
    protected Protocole protocole;
    protected Socket csocket;
    protected Logger logger;
    private int numero;
    private static int numCourant = 1;

    public ThreadClient(Protocole protocole, ThreadGroup groupe, Logger logger) throws IOException {
        super(groupe, "TH Client " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.csocket = null;
        this.logger = logger;
        this.numero = numCourant++;
    }

    @Override
    public void run() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try
        {
            if (csocket == null)
            {
                logger.Trace("Erreur : socket client non initialisée.");
                return;
            }

            logger.Trace("TH Client " + numero + " : traitement de la requête...");

            ois = new ObjectInputStream(csocket.getInputStream());
            oos = new ObjectOutputStream(csocket.getOutputStream());

            Requete requete = (Requete) ois.readObject();
            Reponse reponse = protocole.TraiteRequete(requete, csocket);
            oos.writeObject(reponse);
            logger.Trace("TH Client " + numero + " : réponse envoyée au client.");

        }
        catch (FinConnexionException ex) {
            logger.Trace("Fin connexion demandée par le protocole.");
            try
            {
                if (oos != null && ex.getReponse() != null)
                    oos.writeObject(ex.getReponse());
            }
            catch (IOException e)
            {
                logger.Trace("Erreur d'envoi de la réponse de fin de connexion.");
            }

        }
        catch (IOException e)
        {
            logger.Trace("Erreur I/O : " + e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            logger.Trace("Erreur : requête invalide (classe introuvable).");
        }
        catch (Exception e)
        {
            logger.Trace("Erreur inattendue : " + e.getMessage());
        }
        finally
        {
            try
            {
                if (csocket != null && !csocket.isClosed())
                {
                    csocket.close();
                    logger.Trace("TH Client " + numero + " : socket fermée.");
                }
            }
            catch (IOException e)
            {
                logger.Trace("Erreur lors de la fermeture de la socket.");
            }
        }
    }
}
