package ProtocoleCAP;
import ProtocoleCAP.Reponse.*;
import ProtocoleCAP.Requete.*;
import ServeurGeneriqueTCP.logging.Logger;
import ServeurGeneriqueTCP.protocol.*;

import java.net.Socket;
import java.util.HashMap;
public class CAP implements Protocole
{
    private final HashMap<String,String> passwords;
    private final HashMap<String,Socket> clientsConnectes;
    private final Logger logger;


    public CAP(Logger log)
    {
        passwords = new HashMap<>();
        passwords.put("wagner","abcd");
        passwords.put("charlet","1234");
        passwords.put("calmant","azerty");
        logger = log;
        clientsConnectes = new HashMap<>();
    }



    @Override
    public String getNom()
    {
        return "PAC";
    }



    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException
    {
        if (requete instanceof Requete_LOGIN)
            return TraiteRequeteLOGIN((Requete_LOGIN)requete, socket);

        if (requete instanceof Requete_ADD_CONSULTATION)
            return TraiteRequeteADD_CONSUTATION((Requete_ADD_CONSULTATION)requete, socket);

        if (requete instanceof Requete_ADD_PATIENT)
            return TraiteRequeteADD_PATIENT((Requete_ADD_PATIENT)requete, socket);

        if (requete instanceof Requete_UPDATE_CONSULTATION)
            return TraiteRequeteUPDATE_CONSULTATION((Requete_UPDATE_CONSULTATION)requete, socket);

        if (requete instanceof Requete_SEARCH_CONSULTATIONS)
            return TraiteRequeteSEARCH_CONSULTATIONS((Requete_SEARCH_CONSULTATIONS)requete, socket);

        if (requete instanceof Requete_DELETE_CONSULTATION)
            return TraiteRequeteDELETE_CONSULTATION((Requete_DELETE_CONSULTATION)requete, socket);

        if (requete instanceof Requete_LOGOUT)
            TraiteRequeteLOGOUT((Requete_LOGOUT)requete);

        return null;
    }


    private synchronized Reponse_LOGIN TraiteRequeteLOGIN(Requete_LOGIN requete, Socket socket) throws FinConnexionException
    {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());
        String password = passwords.get(requete.getLogin());
        if (password != null)
            if (password.equals(requete.getPassword()))
            {
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
                logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
                clientsConnectes.put(requete.getLogin(),socket);
                return new Reponse_LOGIN(true);
            }
        logger.Trace(requete.getLogin() + " --> erreur de login");
        throw new FinConnexionException(new Reponse_LOGIN(false));
    }


    private synchronized Reponse_ADD_CONSULTATION TraiteRequeteADD_CONSUTATION(Requete_ADD_CONSULTATION requete, Socket socket) {
    }


    private synchronized Reponse_ADD_PATIENT TraiteRequeteADD_PATIENT(Requete_ADD_PATIENT requete, Socket socket) {
    }


    private synchronized Reponse_UPDATE_CONSULTATION TraiteRequeteUPDATE_CONSULTATION(Requete_UPDATE_CONSULTATION requete, Socket socket) {
    }


    private synchronized Reponse_SEARCH_CONSULTATIONS TraiteRequeteSEARCH_CONSULTATIONS(Requete_SEARCH_CONSULTATIONS requete, Socket socket) {
    }

    private Reponse_DELETE_CONSULTATION TraiteRequeteDELETE_CONSULTATION(Requete_DELETE_CONSULTATION requete, Socket socket) {
    }


    private synchronized void TraiteRequeteLOGOUT(Requete_LOGOUT requete) throws FinConnexionException
    {
        logger.Trace("RequeteLOGOUT reçue de " + requete.getLogin());
        clientsConnectes.remove(requete.getLogin());
        logger.Trace(requete.getLogin() + " correctement déloggé");
        throw new FinConnexionException(null);
    }

}