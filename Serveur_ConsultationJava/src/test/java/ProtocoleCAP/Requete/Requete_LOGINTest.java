package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_LOGINTest {

    @Test
    public void testCreationValide() {
        Requete_LOGIN requete = new Requete_LOGIN("admin", "secret", false);

        assertEquals("admin", requete.getLogin(),
                "Le login doit être correctement enregistré");
        assertEquals("secret", requete.getPassword(),
                "Le mot de passe doit être correctement enregistré");
        assertFalse(requete.isNewUser(),
                "Le flag newUser doit être correctement enregistré");
    }

    @Test
    public void testSetters() {
        Requete_LOGIN requete = new Requete_LOGIN("user", "1234", true);

        requete.setLogin("doctor");
        requete.setPassword("newpass");
        requete.setNewUser(false);

        assertEquals("doctor", requete.getLogin());
        assertEquals("newpass", requete.getPassword());
        assertFalse(requete.isNewUser());
    }

    @Test
    public void testValeursInvalides() {
        assertThrows(IllegalArgumentException.class,
                () -> new Requete_LOGIN(null, "password", false),
                "Un login null doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_LOGIN("user", "", false),
                "Un mot de passe vide doit lever une exception");

        Requete_LOGIN requete = new Requete_LOGIN("ok", "pass", true);

        assertThrows(IllegalArgumentException.class,
                () -> requete.setLogin(""),
                "Le setter doit refuser un login vide");

        assertThrows(IllegalArgumentException.class,
                () -> requete.setPassword(null),
                "Le setter doit refuser un mot de passe null");
    }

    @Test
    public void testToStringMasqueMotDePasse() {
        Requete_LOGIN requete = new Requete_LOGIN("admin", "secret", false);
        String ts = requete.toString();

        assertTrue(ts.contains("admin"),
                "Le toString doit contenir le login");

        assertTrue(ts.contains("********"),
                "Le mot de passe doit être masqué dans le toString");

        assertFalse(ts.contains("secret"),
                "Le mot de passe ne doit jamais apparaître en clair");
    }
}