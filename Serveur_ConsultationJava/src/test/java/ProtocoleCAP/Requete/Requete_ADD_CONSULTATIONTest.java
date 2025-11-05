package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_ADD_CONSULTATIONTest {

    @Test
    public void testCreationValide() {
        Date date = new Date();
        Requete_ADD_CONSULTATION requete =
                new Requete_ADD_CONSULTATION(date, "09:30", "30min", 2);

        assertEquals(date, requete.getDate(), "La date doit correspondre à celle passée au constructeur");
        assertEquals("09:30", requete.getHeure(), "L'heure doit être correctement enregistrée");
        assertEquals("30min", requete.getDuree(), "La durée doit être correctement enregistrée");
        assertEquals(2, requete.getNombreConsultations(), "Le nombre de consultations doit être correct");
    }

    @Test
    public void testSettersEtGetters() {
        Date date = new Date();
        Requete_ADD_CONSULTATION requete =
                new Requete_ADD_CONSULTATION(date, "08:00", "20min", 1);

        Date newDate = new Date();
        requete.setDate(newDate);
        requete.setHeure("10:00");
        requete.setDuree("45min");
        requete.setNombreConsultations(3);

        assertEquals(newDate, requete.getDate());
        assertEquals("10:00", requete.getHeure());
        assertEquals("45min", requete.getDuree());
        assertEquals(3, requete.getNombreConsultations());
    }

    @Test
    public void testConstructeurAvecValeursInvalides() {
        Date date = new Date();

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_CONSULTATION(null, "09:00", "30min", 1),
                "Une date nulle doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_CONSULTATION(date, null, "30min", 1),
                "Une heure nulle doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_CONSULTATION(date, "09:00", "", 1),
                "Une durée vide doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_CONSULTATION(date, "09:00", "30min", 0),
                "Un nombre de consultations <= 0 doit lever une exception");
    }

    @Test
    public void testToStringContientChamps() {
        Date date = new Date();
        Requete_ADD_CONSULTATION requete =
                new Requete_ADD_CONSULTATION(date, "11:00", "60min", 1);

        String toString = requete.toString();
        assertTrue(toString.contains("11:00"), "Le toString doit contenir l'heure");
        assertTrue(toString.contains("60min"), "Le toString doit contenir la durée");
        assertTrue(toString.contains("1"), "Le toString doit contenir le nombre de consultations");
    }
}
