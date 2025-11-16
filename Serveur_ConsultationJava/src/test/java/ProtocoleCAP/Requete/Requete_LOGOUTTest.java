package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_LOGOUTTest {

    @Test
    public void testConstructeurParDefaut() {
        Requete_LOGOUT requete = new Requete_LOGOUT();
        assertNotNull(requete, "La requête ne doit pas être null");
    }

    @Test
    public void testIdConnexionHeriteDeLaClasseMere() {
        Requete_LOGOUT requete = new Requete_LOGOUT();

        requete.setIdConnexion(123);   // hérité de RequeteCAP
        assertEquals(123, requete.getIdConnexion(),
                "L'idConnexion doit être stocké via la classe mère RequeteCAP");
    }

    @Test
    public void testToStringContientNomClasse() {
        Requete_LOGOUT requete = new Requete_LOGOUT();
        String ts = requete.toString();

        assertTrue(ts.contains("Requete_LOGOUT"),
                "Le toString doit contenir le nom de la classe");
    }
}