package ProtocoleMRPS;

import ServeurGeneriqueTCP.logging.Logger;

import java.net.Socket;
import java.util.UUID;

import ProtocoleMRPS.Requete.*;
import ProtocoleMRPS.Reponse.*;
import protocol.*;
import model.dao.*;


public class MRPS implements Protocole
{
    private final Logger logger;
    private final UserDAO userDAO;

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
            case Requete_LOGIN requeteLogin -> {
                return TraiteRequeteLOGIN(requeteLogin, socket);
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

    private synchronized Reponse_LOGIN TraiteRequeteLOGIN (Requete_LOGIN requete, Socket socket) {
        logger.Trace("RequeteLOGIN reçue de " + requete.getUsername());

        String password = requete.getPassword();
        String userName = requete.getUsername();

        // Vérifier l'utilisateur dans la DB
        if (userDAO.checkOrCreateUser(userName, password, requete.isNewUser())) {

            // Vérifier si déjà connecté
//            if (clientsConnectes.containsKey(userName)) {
//                logger.Trace("Échec du login pour " + userName + " : déjà connecté.");
//                return new Reponse_LOGIN(false);
//            }

            // Construire la réponse

            return new Reponse_LOGIN(true);
        }

        else
        {
            return new Reponse_LOGIN(false);
        }
    }
}
