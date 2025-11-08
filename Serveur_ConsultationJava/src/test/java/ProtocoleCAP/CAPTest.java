package ProtocoleCAP;

import ProtocoleCAP.Reponse.*;
import ProtocoleCAP.Requete.*;
import ServeurGeneriqueTCP.logging.Logger;
import model.entity.Consultation;
import ServeurGeneriqueTCP.protocol.FinConnexionException;
import ServeurGeneriqueTCP.protocol.Reponse;

import ServeurGeneriqueTCP.protocol.Requete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


public class CAPTest {

    private CAP cap;
    private DummyLogger logger;
    private Socket fakeSocket;

    // Logger minimaliste pour tester les traces
    static class DummyLogger implements Logger {
        private final StringBuilder logs = new StringBuilder();
        @Override public void Trace(String message) { logs.append(message).append("\n"); }
        public String getLogs() { return logs.toString(); }
    }

    @BeforeEach
    public void setUp() {
        logger = new DummyLogger();
        cap = new CAP(logger);

        // Création d'un faux socket sans connexion réseau
        fakeSocket = new Socket() {
            @Override
            public java.net.InetAddress getInetAddress() {
                try {
                    return java.net.InetAddress.getLocalHost();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public int getPort() {
                return 9999; // peu importe
            }
        };
    }

    // ============================================================
    // 🔹 LOGIN
    // ============================================================
    @Test
    public void testLoginValide() throws Exception {
        Requete_LOGIN req = new Requete_LOGIN("wagner", "abcd");
        Reponse rep = cap.TraiteRequete(req, fakeSocket);

        assertInstanceOf(Reponse_LOGIN.class, rep);
        assertTrue(((Reponse_LOGIN) rep).isValide(), "Le login valide doit renvoyer true");
        assertTrue(logger.getLogs().contains("correctement loggé"));
    }

    @Test
    public void testLoginInvalideLanceException() {
        Requete_LOGIN req = new Requete_LOGIN("wagner", "mauvais_mdp");
        assertThrows(FinConnexionException.class, () -> cap.TraiteRequete(req, fakeSocket),
                "Un mauvais mot de passe doit lever une FinConnexionException");
    }

    // ============================================================
    // 🔹 ADD_CONSULTATION
    // ============================================================
    @Test
    public void testAddConsultation() throws Exception {
        Requete_ADD_CONSULTATION req = new Requete_ADD_CONSULTATION(new Date(), "10:00", "30min", 2);
        Reponse rep = cap.TraiteRequete(req, fakeSocket);

        assertInstanceOf(Reponse_ADD_CONSULTATION.class, rep);
        assertTrue(((Reponse_ADD_CONSULTATION) rep).isValide());
    }

    // ============================================================
    // 🔹 ADD_PATIENT
    // ============================================================
    @Test
    public void testAddPatient() throws Exception {
        Requete_ADD_PATIENT req = new Requete_ADD_PATIENT("Alice", "Dupont");
        Reponse rep = cap.TraiteRequete(req, fakeSocket);

        assertInstanceOf(Reponse_ADD_PATIENT.class, rep);
        assertEquals(1, ((Reponse_ADD_PATIENT) rep).getIdAttribuer());
    }

    // ============================================================
    // 🔹 UPDATE_CONSULTATION
    // ============================================================
    @Test
    public void testUpdateConsultation() throws Exception {
        String Midi = "12h00";
        Date now = new Date();
        Requete_UPDATE_CONSULTATION req =
                new Requete_UPDATE_CONSULTATION(1, now, Midi, 2, "Nouvelle raison");
        Reponse rep = cap.TraiteRequete(req, fakeSocket);

        assertInstanceOf(Reponse_UPDATE_CONSULTATION.class, rep);
        assertTrue(((Reponse_UPDATE_CONSULTATION) rep).isValide());
    }

    // ============================================================
    // 🔹 SEARCH_CONSULTATIONS
    // ============================================================
    @Test
    public void testSearchConsultations() throws Exception {
        Requete_SEARCH_CONSULTATIONS req = new Requete_SEARCH_CONSULTATIONS(1, new Date());
        Reponse rep = cap.TraiteRequete(req, fakeSocket);

        assertInstanceOf(Reponse_SEARCH_CONSULTATIONS.class, rep);
        Reponse_SEARCH_CONSULTATIONS rsc = (Reponse_SEARCH_CONSULTATIONS) rep;
        assertFalse(rsc.getConsultationsList().isEmpty(), "La liste doit contenir des consultations simulées");
        assertInstanceOf(Consultation.class, rsc.getConsultationsList().get(0));
    }

    // ============================================================
    // 🔹 DELETE_CONSULTATION
    // ============================================================
    @Test
    public void testDeleteConsultation() throws Exception {
        Requete_DELETE_CONSULTATION req = new Requete_DELETE_CONSULTATION(1);
        Reponse rep = cap.TraiteRequete(req, fakeSocket);

        assertInstanceOf(Reponse_DELETE_CONSULTATION.class, rep);
        assertTrue(((Reponse_DELETE_CONSULTATION) rep).isValide());
    }

    // ============================================================
    // 🔹 LOGOUT
    // ============================================================
    @Test
    public void testLogout() throws Exception {
        // Simule un login
        cap.TraiteRequete(new Requete_LOGIN("charlet", "1234"), fakeSocket);
        // Logout
        Requete_LOGOUT req = new Requete_LOGOUT("charlet");
        cap.TraiteRequete(req, fakeSocket);

        assertTrue(logger.getLogs().contains("correctement déloggé"),
                "Le logger doit indiquer que l'utilisateur s'est déloggé");
    }

    // ============================================================
    // 🔹 ERREURS
    // ============================================================
    @Test
    public void testRequeteNullRetourneErreur() throws Exception {
        Reponse rep = cap.TraiteRequete(null, fakeSocket);
        assertInstanceOf(Reponse_ERROR.class, rep);
        assertTrue(((Reponse_ERROR) rep).getMessage().contains("vide"));
    }

    @Test
    public void testRequeteNonReconnuRetourneErreur() throws Exception {
        Requete requeteBidon = new Requete() {}; // classe anonyme simulant un type inconnu
        Reponse rep = cap.TraiteRequete(requeteBidon, fakeSocket);

        assertInstanceOf(Reponse_ERROR.class, rep);
        assertTrue(((Reponse_ERROR) rep).getMessage().contains("non reconnu"));
    }
}
