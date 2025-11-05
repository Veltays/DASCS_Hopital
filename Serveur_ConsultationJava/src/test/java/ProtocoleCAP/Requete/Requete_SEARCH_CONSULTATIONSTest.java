package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_SEARCH_CONSULTATIONSTest {

    @Test
    public void testCreationValide() {
        Date d = new Date();
        Requete_SEARCH_CONSULTATIONS requete = new Requete_SEARCH_CONSULTATIONS(12, d);

        assertEquals(12, requete.getIdPatient(),
                "L'identifiant du patient doit être correctement enregistré");
        assertEquals(d, requete.getDate(),
                "La date doit être correctement enregistrée");
    }

    @Test
    public void testSettersValides() {
        Date d1 = new Date();
        Requete_SEARCH_CONSULTATIONS requete = new Requete_SEARCH_CONSULTATIONS(1, d1);

        Date d2 = new Date(System.currentTimeMillis() + 100000); // + quelques secondes
        requete.setIdPatient(99);
        requete.setDate(d2);

        assertEquals(99, requete.getIdPatient());
        assertEquals(d2, requete.getDate());
    }

    @Test
    public void testValeursInvalides() {
        Date d = new Date();

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_SEARCH_CONSULTATIONS(null, d),
                "Un idPatient null doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_SEARCH_CONSULTATIONS(0, d),
                "Un idPatient <= 0 doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_SEARCH_CONSULTATIONS(1, null),
                "Une date nulle doit lever une exception");

        Requete_SEARCH_CONSULTATIONS requete = new Requete_SEARCH_CONSULTATIONS(1, d);
        assertThrows(IllegalArgumentException.class,
                () -> requete.setIdPatient(-3),
                "Le setter doit refuser un id négatif");
    }

    @Test
    public void testToStringContientChamps() {
        Date d = new Date();
        Requete_SEARCH_CONSULTATIONS requete = new Requete_SEARCH_CONSULTATIONS(7, d);
        String ts = requete.toString();

        assertTrue(ts.contains("7"),
                "Le toString doit contenir l'identifiant du patient");
        assertTrue(ts.contains("Requete_SEARCH_CONSULTATIONS"),
                "Le toString doit contenir le nom de la classe");
    }
}
