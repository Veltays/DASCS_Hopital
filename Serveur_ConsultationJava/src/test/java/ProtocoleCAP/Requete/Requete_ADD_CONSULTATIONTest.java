package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_ADD_CONSULTATIONTest {

    @Test
    public void testCreationValide() {
        LocalDate date = LocalDate.of(2025, 1, 10);

        Requete_ADD_CONSULTATION requete =
                new Requete_ADD_CONSULTATION(date, "09:30", "30min", 2);

        assertEquals(date, requete.getDate());
        assertEquals("09:30", requete.getHeure());
        assertEquals("30min", requete.getDuree());
        assertEquals(2, requete.getNombreConsultations());
    }

    @Test
    public void testSettersEtGetters() {
        LocalDate date = LocalDate.of(2025, 1, 1);

        Requete_ADD_CONSULTATION requete =
                new Requete_ADD_CONSULTATION(date, "08:00", "20min", 1);

        LocalDate newDate = LocalDate.of(2025, 2, 1);
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
        LocalDate date = LocalDate.now();

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_CONSULTATION(null, "09:00", "30min", 1));

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_CONSULTATION(date, null, "30min", 1));

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_CONSULTATION(date, "09:00", "", 1));

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_CONSULTATION(date, "09:00", "30min", 0));
    }

    @Test
    public void testToStringContientChamps() {
        LocalDate date = LocalDate.of(2025, 1, 1);

        Requete_ADD_CONSULTATION requete =
                new Requete_ADD_CONSULTATION(date, "11:00", "60min", 1);

        String str = requete.toString();

        assertTrue(str.contains("11:00"));
        assertTrue(str.contains("60min"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("Requete_ADD_CONSULTATION"));
    }
}