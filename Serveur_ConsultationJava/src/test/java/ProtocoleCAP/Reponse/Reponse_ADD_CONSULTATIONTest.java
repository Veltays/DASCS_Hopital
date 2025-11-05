package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Reponse_ADD_CONSULTATIONTest {

    @Test
    public void testReponseValideTrue() {
        Reponse_ADD_CONSULTATION reponse = new Reponse_ADD_CONSULTATION(true);
        assertTrue(reponse.isValide(), "La réponse devrait être valide (true)");
    }

    @Test
    public void testReponseValideFalse() {
        Reponse_ADD_CONSULTATION reponse = new Reponse_ADD_CONSULTATION(false);
        assertFalse(reponse.isValide(), "La réponse devrait être invalide (false)");
    }
}