package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_ADD_PATIENTTest {

    @Test
    public void testCreationValide() {
        Requete_ADD_PATIENT requete = new Requete_ADD_PATIENT("Alice", "Dupont");

        assertEquals("Alice", requete.getFirstName(),
                "Le prénom doit être correctement enregistré");
        assertEquals("Dupont", requete.getLastName(),
                "Le nom doit être correctement enregistré");
    }

    @Test
    public void testSetters() {
        Requete_ADD_PATIENT requete = new Requete_ADD_PATIENT("Test", "Test");
        requete.setFirstName("Bob");
        requete.setLastName("Martin");

        assertEquals("Bob", requete.getFirstName());
        assertEquals("Martin", requete.getLastName());
    }

    @Test
    public void testValeursInvalides() {
        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_PATIENT(null, "Durand"),
                "Un prénom null doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_ADD_PATIENT("Jean", ""),
                "Un nom vide doit lever une exception");

        Requete_ADD_PATIENT requete = new Requete_ADD_PATIENT("Paul", "Roux");
        assertThrows(IllegalArgumentException.class,
                () -> requete.setFirstName(""),
                "Le setter doit refuser un prénom vide");
        assertThrows(IllegalArgumentException.class,
                () -> requete.setLastName(null),
                "Le setter doit refuser un nom null");
    }

    @Test
    public void testToStringContientChamps() {
        Requete_ADD_PATIENT requete = new Requete_ADD_PATIENT("Alice", "Dupont");
        String ts = requete.toString();

        assertTrue(ts.contains("Alice"));
        assertTrue(ts.contains("Dupont"));
        assertTrue(ts.contains("Requete_ADD_PATIENT"));
    }
}
