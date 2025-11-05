package ServeurGeneriqueTCP.core;

import ServeurGeneriqueTCP.logging.Logger;
import ServeurGeneriqueTCP.protocol.Protocole;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadServeurPool extends ThreadServeur {

    protected FileAttente connexionsEnAttente;
    protected final ThreadGroup pool;
    private int taillePool;

    public ThreadServeurPool(int port, Protocole protocole, int taillePool, Logger logger) throws IOException {
        super(port, protocole, logger);
        this.connexionsEnAttente = new FileAttente();
        this.pool = new ThreadGroup("POOL");
        this.taillePool = taillePool;
    }

    @Override
    public void run()
    {
        logger.Trace("Démarrage du TH Serveur (Pool)...");

        // Création du pool de threads clients
        try
        {
            for (int i = 0; i < taillePool; i++)
            {
                new ThreadClientPool(protocole, connexionsEnAttente, pool, logger).start();
            }
        }
        catch (IOException ex)
        {
            logger.Trace("Erreur I/O lors de la création du pool de threads : " + ex.getMessage());
            return;
        }

        // Boucle principale d’attente des connexions
        while (!this.isInterrupted())
        {
            try
            {
                ssocket.setSoTimeout(2000);
                Socket csocket = ssocket.accept();
                logger.Trace("Connexion acceptée du client " +  csocket.getInetAddress().getHostAddress() + ":" + csocket.getPort() + ", mise en file d'attente.");
                connexionsEnAttente.addConnexion(csocket);

            }
            catch (SocketTimeoutException ex)
            {
                logger.Trace("Le thread serveur est en attente (timeout).");
            }
            catch (IOException ex)
            {
                logger.Trace("Erreur I/O : " + ex.getMessage());
            }
            catch (Exception e)
            {
                logger.Trace("Erreur inattendue : " + e.getMessage());
            }
        }
        logger.Trace("TH Serveur (Pool) interrompu.");
        pool.interrupt();
    }
}
