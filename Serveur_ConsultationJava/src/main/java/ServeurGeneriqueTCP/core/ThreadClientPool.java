package ServeurGeneriqueTCP.core;
import ServeurGeneriqueTCP.logging.Logger;
import protocol.Protocole;

import java.io.IOException;
public class ThreadClientPool extends ThreadClient
{
    private FileAttente connexionsEnAttente;
    public ThreadClientPool(Protocole protocole, FileAttente file, ThreadGroup
            groupe, Logger logger) throws IOException
    {
        super(protocole, groupe, logger);
        connexionsEnAttente = file;
    }
    @Override
    public void run()
    {
        logger.Trace("TH Client (Pool) démarre...");
        boolean interrompu = false;
        while(!interrompu)
        {
            try
            {
                logger.Trace("Attente d'une connexion...");
                csocket = connexionsEnAttente.getConnexion();

                if (csocket == null) {
                    logger.Trace("Aucune connexion disponible, le thread attend...");
                    continue;
                }


                logger.Trace("Connexion prise en charge du client " + csocket.getInetAddress().getHostAddress() + ":" + csocket.getPort() + ".");
                super.run();
            }
            catch (InterruptedException ex)
            {
                logger.Trace("Demande d'interruption...");
                interrompu = true;
            }
            catch (Exception e) {
                logger.Trace("Erreur dans ThreadClientPool : " + e.getMessage());
            }
        }
        logger.Trace("TH Client (Pool) se termine.");
    }
}