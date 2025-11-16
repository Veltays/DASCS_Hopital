package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Reponse_LOGOUTTest {

    @Test
    public void testReponseValideTrue() {
        Reponse_LOGOUT reponse = new Reponse_LOGOUT(true);
        assertTrue(reponse.isValide(),
                "La réponse LOGOUT devrait être valide (true)");
    }

    @Test
    public void testReponseValideFalse() {
        Reponse_LOGOUT reponse = new Reponse_LOGOUT(false);
        assertFalse(reponse.isValide(),
                "La réponse LOGOUT devrait être invalide (false)");
    }
}
