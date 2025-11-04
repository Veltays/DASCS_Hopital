package ServeurGeneriqueTCP;

import ServeurGeneriqueTCP.core.ThreadServeurPool;
import ServeurGeneriqueTCP.logging.ConsoleLogger;
import ServeurGeneriqueTCP.logging.Logger;

import ServeurGeneriqueTCP.protocol.Protocole;
import ProtocoleCAP.CAP; // ou ton protocole concret (par ex. ProtocoleConsultation)
import ServeurGeneriqueTCP.utils.ConfigLoader;

public class MainServeur {
    static void main(String[] args) {
        try {
            ConfigLoader config = new ConfigLoader();

            int port = Integer.parseInt(config.get("PORT_CONSULTATION"));

            int taillePool = 5;

            Logger logger = new ConsoleLogger(); // ton logger maison
            Protocole protocole = new CAP(logger); // le protocole que tu veux utiliser (CAP, etc.)

            ThreadServeurPool serveur = new ThreadServeurPool(port, protocole, taillePool, logger);
            serveur.start();

            logger.Trace("Serveur Consultation lancé sur le port " + port);
        }
        catch (Exception e) {
            System.err.println("Erreur au lancement du serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}