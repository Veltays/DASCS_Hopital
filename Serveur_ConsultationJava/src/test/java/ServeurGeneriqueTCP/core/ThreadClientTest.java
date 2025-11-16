package ServeurGeneriqueTCP.core;

import ServeurGeneriqueTCP.logging.Logger;
import protocol.FinConnexionException;
import protocol.Protocole;
import protocol.Reponse;
import protocol.Requete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.mockito.Mockito.*;

class ThreadClientTest {

    private Protocole mockProtocole;
    private Logger mockLogger;
    private Socket mockSocket;
    private ThreadGroup group;

    // Classe concrète minimale pour tester l’abstraite
    static class TestableThreadClient extends ThreadClient {
        public TestableThreadClient(Protocole protocole, ThreadGroup groupe, Logger logger) throws IOException {
            super(protocole, groupe, logger);
        }
    }

    @BeforeEach
    void setUp() {
        mockProtocole = mock(Protocole.class);
        mockLogger = mock(Logger.class);
        mockSocket = mock(Socket.class);
        group = new ThreadGroup("TestGroup");
    }

    @Test
    void run_ShouldProcessRequestAndSendResponse() throws Exception {
        // --- Préparation ---
        Requete req = mock(Requete.class);
        Reponse rep = mock(Reponse.class);

        when(mockProtocole.getNom()).thenReturn("TESTPROTO");
        when(mockProtocole.TraiteRequete(any(), eq(mockSocket))).thenReturn(rep);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialize(req));

        when(mockSocket.getInputStream()).thenReturn(inputStream);
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        ThreadClient client = new TestableThreadClient(mockProtocole, group, mockLogger);
        client.csocket = mockSocket;

        // --- Exécution ---
        client.run();

        // --- Vérifications ---
        verify(mockLogger).Trace(contains("traitement de la requête"));
        verify(mockLogger).Trace(contains("réponse envoyée"));
        verify(mockLogger).Trace(contains("socket fermée"));
    }

    @Test
    void run_ShouldHandleFinConnexionException() throws Exception {
        when(mockProtocole.getNom()).thenReturn("PROTO");
        when(mockProtocole.TraiteRequete(any(), any())).thenThrow(new FinConnexionException(mock(Reponse.class)));

        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(serialize(mock(Requete.class))));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        ThreadClient client = new TestableThreadClient(mockProtocole, group, mockLogger);
        client.csocket = mockSocket;

        client.run();

        verify(mockLogger).Trace(contains("Fin connexion demandée"));
    }

    @Test
    void run_ShouldHandleIOException() throws Exception {
        when(mockProtocole.getNom()).thenReturn("PROTO");
        when(mockSocket.getInputStream()).thenThrow(new IOException("Simulated IO error"));

        ThreadClient client = new TestableThreadClient(mockProtocole, group, mockLogger);
        client.csocket = mockSocket;

        client.run();

        verify(mockLogger).Trace(contains("Erreur I/O"));
    }

    @Test
    void run_ShouldHandleNullSocketGracefully() throws Exception {
        when(mockProtocole.getNom()).thenReturn("PROTO");

        ThreadClient client = new TestableThreadClient(mockProtocole, group, mockLogger);
        client.csocket = null;

        client.run();

        verify(mockLogger).Trace(contains("socket client non initialisée"));
    }

    // --- Utilitaire pour simuler un objet sérialisé ---
    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        return bos.toByteArray();
    }
}
