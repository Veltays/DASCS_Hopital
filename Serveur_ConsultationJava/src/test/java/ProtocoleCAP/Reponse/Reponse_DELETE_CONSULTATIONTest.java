package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Reponse_DELETE_CONSULTATIONTest {

    @Test
    public void testReponseValideTrue() {
        Reponse_DELETE_CONSULTATION reponse = new Reponse_DELETE_CONSULTATION(true);
        assertTrue(reponse.isValide(),
                "La réponse devrait être valide (true) lorsqu'une suppression réussit");
    }

    @Test
    public void testReponseValideFalse() {
        Reponse_DELETE_CONSULTATION reponse = new Reponse_DELETE_CONSULTATION(false);
        assertFalse(reponse.isValide(),
                "La réponse devrait être invalide (false) lorsqu'une suppression échoue");
    }
}