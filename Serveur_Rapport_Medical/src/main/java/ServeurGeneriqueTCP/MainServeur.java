package ServeurGeneriqueTCP;

import ProtocoleMRPS.MRPS;
import ServeurGeneriqueTCP.core.ThreadServeurDemande;
import ServeurGeneriqueTCP.logging.ConsoleLogger;
import ServeurGeneriqueTCP.logging.Logger;
import ServeurGeneriqueTCP.utils.ConfigLoader;
import protocol.Protocole;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class MainServeur {
    public static void main(String[] args) {

        Security.addProvider(new BouncyCastleProvider());
        try {
            ConfigLoader config = new ConfigLoader();

            int port = Integer.parseInt(config.get("PORT_REPORT_SECURE"));


            Logger logger = new ConsoleLogger();
            Protocole protocole = new MRPS(logger); // le protocole que tu veux utiliser (CAP, etc.)

            ThreadServeurDemande serveur = new ThreadServeurDemande(port, protocole, logger);
            serveur.start();

            logger.Trace("Serveur Consultation lancé sur le port " + port);
        }
        catch (Exception e) {
            System.err.println("Erreur au lancement du serveur : " + e.getMessage());
            e.printStackTrace();

        }
    }
}