package ServeurGeneriqueTCP.core;

import ServeurGeneriqueTCP.logging.Logger;
import ServeurGeneriqueTCP.protocol.Protocole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.*;

class ThreadClientPoolTest {

    private Protocole mockProtocole;
    private Logger mockLogger;
    private FileAttente mockFileAttente;
    private Socket mockSocket;
    private ThreadGroup group;

    @BeforeEach
    void setUp() {
        mockProtocole = mock(Protocole.class);
        mockLogger = mock(Logger.class);
        mockFileAttente = mock(FileAttente.class);
        mockSocket = mock(Socket.class);
        group = new ThreadGroup("TestGroup");
    }

    @Test
    void run_ShouldLogStartAndEnd_WhenInterrupted() throws Exception {
        when(mockFileAttente.getConnexion()).thenThrow(new InterruptedException());

        ThreadClientPool pool = new ThreadClientPool(mockProtocole, mockFileAttente, group, mockLogger);
        pool.run();

        verify(mockLogger).Trace("TH Client (Pool) démarre...");
        verify(mockLogger, atLeastOnce()).Trace(contains("Demande d'interruption"));
        verify(mockLogger).Trace("TH Client (Pool) se termine.");
    }

    @Test
    void run_ShouldHandleClientConnectionProperly() throws Exception {
        when(mockFileAttente.getConnexion())
                .thenReturn(mockSocket)
                .thenThrow(new InterruptedException());

        when(mockSocket.getInetAddress()).thenReturn(java.net.InetAddress.getByName("127.0.0.1"));
        when(mockSocket.getPort()).thenReturn(5000);

        ThreadClientPool pool = new ThreadClientPool(mockProtocole, mockFileAttente, group, mockLogger);
        pool.run();

        verify(mockLogger).Trace("TH Client (Pool) démarre...");
        verify(mockLogger, atLeastOnce()).Trace("Attente d'une connexion...");
        verify(mockLogger).Trace(contains("Connexion prise en charge du client"));
        verify(mockLogger, atLeastOnce()).Trace(contains("Demande d'interruption"));
        verify(mockLogger).Trace("TH Client (Pool) se termine.");
    }

    @Test
    void run_ShouldSkipNullConnection() throws Exception {
        when(mockFileAttente.getConnexion())
                .thenReturn(null)
                .thenThrow(new InterruptedException());

        ThreadClientPool pool = new ThreadClientPool(mockProtocole, mockFileAttente, group, mockLogger);
        pool.run();

        verify(mockLogger, atLeastOnce()).Trace(contains("Aucune connexion disponible"));
    }
}
