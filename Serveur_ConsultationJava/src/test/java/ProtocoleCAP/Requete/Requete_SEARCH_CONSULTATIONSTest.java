package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class Requete_SEARCH_CONSULTATIONSTest {

    @Test
    public void testCreationValide() {
        LocalDate d = LocalDate.of(2025, 1, 10);

        Requete_SEARCH_CONSULTATIONS requete =
                new Requete_SEARCH_CONSULTATIONS(12, d);

        assertEquals(12, requete.getIdPatient(),
                "L'identifiant du patient doit être correctement enregistré");
        assertEquals(d, requete.getDate(),
                "La date doit être correctement enregistrée");
    }

    @Test
    public void testSettersValides() {
        LocalDate d1 = LocalDate.of(2025, 2, 15);

        Requete_SEARCH_CONSULTATIONS requete =
                new Requete_SEARCH_CONSULTATIONS(1, d1);

        LocalDate d2 = LocalDate.of(2025, 3, 1);

        requete.setIdPatient(99);
        requete.setDate(d2);

        assertEquals(99, requete.getIdPatient(),
                "Le setter doit mettre à jour l'id patient");
        assertEquals(d2, requete.getDate(),
                "Le setter doit mettre à jour la date");
    }

    @Test
    public void testValeursNullesAccepteesCarValidationsDesactivees() {
        Requete_SEARCH_CONSULTATIONS req =
                new Requete_SEARCH_CONSULTATIONS(null, null);

        assertNull(req.getIdPatient(),
                "Sans validation, idPatient null doit être accepté");
        assertNull(req.getDate(),
                "Sans validation, date null doit être acceptée");

        req.setIdPatient(null);
        req.setDate(null);

        assertNull(req.getIdPatient());
        assertNull(req.getDate());
    }

    @Test
    public void testToStringContientChamps() {
        LocalDate d = LocalDate.of(2025, 6, 12);

        Requete_SEARCH_CONSULTATIONS requete =
                new Requete_SEARCH_CONSULTATIONS(7, d);

        String ts = requete.toString();

        assertTrue(ts.contains("7"),
                "Le toString doit contenir l'idPatient");
        assertTrue(ts.contains("Requete_SEARCH_CONSULTATIONS"),
                "Le toString doit contenir le nom de la classe");
        assertTrue(ts.contains("2025-06-12"),
                "Le toString doit contenir la date");
    }
}
