package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_UPDATE_CONSULTATIONTest {

    @Test
    public void testCreationValide() {
        Date date = new Date();
        String heure = "15h45";
        Requete_UPDATE_CONSULTATION requete =
                new Requete_UPDATE_CONSULTATION(1, date, heure, 2, "Nouvelle raison");

        assertEquals(1, requete.getIdConsultation());
        assertEquals(date, requete.getNouvelleDate());
        assertEquals(heure, requete.getNouvelleHeure());
        assertEquals(2, requete.getIdPatient());
        assertEquals("Nouvelle raison", requete.getNouvelleRaison());
    }

    @Test
    public void testSettersValides() {
        Date d1 = new Date();
        String h1 = "0h30";
        Requete_UPDATE_CONSULTATION requete =
                new Requete_UPDATE_CONSULTATION(1, d1, h1, 2, "Test");

        Date d2 = new Date(System.currentTimeMillis() + 100000);
        String h2 = "9h30";
        requete.setIdConsultation(10);
        requete.setNouvelleDate(d2);
        requete.setNouvelleHeure(h2);
        requete.setIdPatient(99);
        requete.setNouvelleRaison("Consultation modifiée");

        assertEquals(10, requete.getIdConsultation());
        assertEquals(d2, requete.getNouvelleDate());
        assertEquals(h2, requete.getNouvelleHeure());
        assertEquals(99, requete.getIdPatient());
        assertEquals("Consultation modifiée", requete.getNouvelleRaison());
    }

    @Test
    public void testValeursInvalides() {
        Date d = new Date();
        String h = "14h00";

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_UPDATE_CONSULTATION(null, d, h, 1, "Raison"),
                "idConsultation ne peut pas être null");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_UPDATE_CONSULTATION(1, null, h, 1, "Raison"),
                "nouvelleDate ne peut pas être null");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_UPDATE_CONSULTATION(1, d, null, 1, "Raison"),
                "nouvelleHeure ne peut pas être null");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_UPDATE_CONSULTATION(1, d, h, 0, "Raison"),
                "idPatient doit être > 0");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_UPDATE_CONSULTATION(1, d, h, 2, ""),
                "nouvelleRaison ne peut pas être vide");
    }

    @Test
    public void testToStringContientChamps() {
        Date d = new Date();
        String h = "11h15";
        Requete_UPDATE_CONSULTATION requete =
                new Requete_UPDATE_CONSULTATION(3, d, h, 5, "Changement de motif");

        String ts = requete.toString();
        assertTrue(ts.contains("3"));
        assertTrue(ts.contains("5"));
        assertTrue(ts.contains("Changement de motif"));
        assertTrue(ts.contains("Requete_UPDATE_CONSULTATION"));
    }
}
