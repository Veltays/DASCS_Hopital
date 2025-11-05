package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_LOGINTest {

    @Test
    public void testCreationValide() {
        Requete_LOGIN requete = new Requete_LOGIN("admin", "secret");
        assertEquals("admin", requete.getLogin(),
                "Le login doit être correctement enregistré");
        assertEquals("secret", requete.getPassword(),
                "Le mot de passe doit être correctement enregistré");
    }

    @Test
    public void testSetters() {
        Requete_LOGIN requete = new Requete_LOGIN("user", "1234");
        requete.setLogin("doctor");
        requete.setPassword("newpass");

        assertEquals("doctor", requete.getLogin());
        assertEquals("newpass", requete.getPassword());
    }

    @Test
    public void testValeursInvalides() {
        assertThrows(IllegalArgumentException.class,
                () -> new Requete_LOGIN(null, "password"),
                "Un login null doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_LOGIN("user", ""),
                "Un mot de passe vide doit lever une exception");

        Requete_LOGIN requete = new Requete_LOGIN("ok", "pass");
        assertThrows(IllegalArgumentException.class,
                () -> requete.setLogin(""),
                "Le setter doit refuser un login vide");
        assertThrows(IllegalArgumentException.class,
                () -> requete.setPassword(null),
                "Le setter doit refuser un mot de passe null");
    }

    @Test
    public void testToStringMasqueMotDePasse() {
        Requete_LOGIN requete = new Requete_LOGIN("admin", "secret");
        String ts = requete.toString();

        assertTrue(ts.contains("admin"), "Le toString doit contenir le login");
        assertTrue(ts.contains("********"), "Le mot de passe doit être masqué dans le toString");
        assertFalse(ts.contains("secret"), "Le mot de passe ne doit jamais apparaître en clair");
    }
}
