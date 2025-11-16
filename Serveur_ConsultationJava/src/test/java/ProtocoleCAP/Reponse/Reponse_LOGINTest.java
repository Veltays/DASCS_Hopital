package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Reponse_LOGINTest {

    @Test
    public void testReponseValideTrue() {
        Reponse_LOGIN reponse = new Reponse_LOGIN(true, 123);

        assertTrue(reponse.isValide(),
                "La réponse LOGIN devrait être valide (true)");
        assertEquals(123, reponse.getIdConnexion(),
                "L'idConnexion doit être correctement enregistré");
    }

    @Test
    public void testReponseValideFalse() {
        Reponse_LOGIN reponse = new Reponse_LOGIN(false, -1);

        assertFalse(reponse.isValide(),
                "La réponse LOGIN devrait être invalide (false)");
        assertEquals(-1, reponse.getIdConnexion(),
                "Même en cas d'échec, l'idConnexion doit être stocké comme passé");
    }

    @Test
    public void testSetterIdConnexion() {
        Reponse_LOGIN reponse = new Reponse_LOGIN(true, 10);

        reponse.setIdConnexion(42);

        assertEquals(42, reponse.getIdConnexion(),
                "Le setter doit mettre à jour correctement l'idConnexion");
    }
}