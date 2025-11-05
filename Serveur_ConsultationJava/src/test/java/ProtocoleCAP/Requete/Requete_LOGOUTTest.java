package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_LOGOUTTest {

    @Test
    public void testCreationValide() {
        Requete_LOGOUT requete = new Requete_LOGOUT("admin");

        assertEquals("admin", requete.getLogin(),
                "Le login doit être correctement enregistré dans la requête");
    }

    @Test
    public void testSetterValide() {
        Requete_LOGOUT requete = new Requete_LOGOUT("user");
        requete.setLogin("doctor");

        assertEquals("doctor", requete.getLogin(),
                "Le setter doit mettre à jour le login correctement");
    }

    @Test
    public void testValeursInvalides() {
        assertThrows(IllegalArgumentException.class,
                () -> new Requete_LOGOUT(null),
                "Un login null doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_LOGOUT(""),
                "Un login vide doit lever une exception");

        Requete_LOGOUT requete = new Requete_LOGOUT("ok");
        assertThrows(IllegalArgumentException.class,
                () -> requete.setLogin(""),
                "Le setter doit refuser un login vide");
    }

    @Test
    public void testToStringContientLogin() {
        Requete_LOGOUT requete = new Requete_LOGOUT("user1");
        String ts = requete.toString();

        assertTrue(ts.contains("user1"),
                "Le toString doit contenir le login");
        assertTrue(ts.contains("Requete_LOGOUT"),
                "Le toString doit contenir le nom de la classe");
    }
}
