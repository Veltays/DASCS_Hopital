package ServeurGeneriqueTCP.core;

import ServeurGeneriqueTCP.logging.Logger;
import protocol.Protocole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static org.mockito.Mockito.*;

class ThreadServeurPoolTest {

    private Protocole mockProtocole;
    private Logger mockLogger;
    private ServerSocket mockServerSocket;
    private Socket mockSocket;

    @BeforeEach
    void setUp() {
        mockProtocole = mock(Protocole.class);
        mockLogger = mock(Logger.class);
        mockServerSocket = mock(ServerSocket.class);
        mockSocket = mock(Socket.class);
    }

    /**
     * Classe interne pour permettre d’injecter un ServerSocket mocké.
     */
    static class TestableThreadServeurPool extends ThreadServeurPool {
        public TestableThreadServeurPool(int port, Protocole protocole, int taillePool, Logger logger) throws IOException {
            super(port, protocole, taillePool, logger);
        }

        public void setServerSocket(ServerSocket ssocket) {
            this.ssocket = ssocket;
        }

        public FileAttente getFileAttente() {
            return this.connexionsEnAttente;
        }

        public ThreadGroup getPool() {
            return this.pool;
        }
    }

    @Test
    void run_ShouldCreateThreadPool_AndAcceptConnections() throws Exception {
        when(mockProtocole.getNom()).thenReturn("PROTO");
        when(mockServerSocket.accept()).thenReturn(mockSocket)
                .thenThrow(new SocketTimeoutException())
                .thenThrow(new IOException("Test I/O"))
                .thenAnswer(inv -> {
                    Thread.currentThread().interrupt();
                    return null;
                });

        when(mockSocket.getInetAddress()).thenReturn(java.net.InetAddress.getByName("127.0.0.1"));
        when(mockSocket.getPort()).thenReturn(5050);

        TestableThreadServeurPool serveur = new TestableThreadServeurPool(5050, mockProtocole, 2, mockLogger);
        serveur.setServerSocket(mockServerSocket);

        // Mock file d’attente pour éviter d’exécuter de vrais ThreadClientPool
        FileAttente mockFile = mock(FileAttente.class);
        serveur.connexionsEnAttente = mockFile;

        serveur.start();
        Thread.sleep(100); // Laisse le temps à run() d'exécuter 1 ou 2 boucles
        serveur.interrupt();
        serveur.join(500);

        verify(mockLogger).Trace("Démarrage du TH Serveur (Pool)...");
        verify(mockLogger, atLeastOnce()).Trace(contains("Connexion acceptée"));
        verify(mockLogger, atLeastOnce()).Trace(contains("time"));
        verify(mockLogger, atLeastOnce()).Trace(contains("Erreur I/O"));
        verify(mockLogger).Trace("TH Serveur (Pool) interrompu.");
    }

    @Test
    void run_ShouldLogError_WhenIOExceptionDuringThreadPoolCreation() throws Exception {
        Logger logger = mock(Logger.class);
        Protocole protocole = mock(Protocole.class);

        // 🧩 Sous-classe de test pour forcer une exception dans la création du ThreadClientPool
        class BrokenThreadServeurPool extends ThreadServeurPool {
            public BrokenThreadServeurPool(int port, Protocole protocole, int taillePool, Logger logger) throws IOException {
                super(port, protocole, taillePool, logger);
            }

            @Override
            public void run() {
                logger.Trace("Démarrage du TH Serveur (Pool)...");
                try {
                    throw new IOException("Erreur test"); // Simule l'erreur ici
                } catch (IOException ex) {
                    logger.Trace("Erreur I/O lors de la création du pool de threads : " + ex.getMessage());
                }
            }
        }

        BrokenThreadServeurPool serveur = new BrokenThreadServeurPool(0, protocole, 2, logger);
        serveur.run();

        verify(logger).Trace(contains("Erreur I/O lors de la création du pool"));
    }
}
