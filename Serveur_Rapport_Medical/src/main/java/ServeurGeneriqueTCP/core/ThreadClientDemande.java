package ServeurGeneriqueTCP.core;
import ServeurGeneriqueTCP.logging.Logger;
import protocol.Protocole;

import java.io.IOException;
import java.net.Socket;
public class ThreadClientDemande extends ThreadClient
{
    public ThreadClientDemande(Protocole protocole, Socket csocket, Logger logger)
            throws IOException
    {
        super(protocole, csocket, logger);
    }
    @Override
    public void run()
    {
        logger.Trace("TH Client (Demande) dÃ©marre...");
        super.run();
        logger.Trace("TH Client (Demande) se termine.");
    }
}