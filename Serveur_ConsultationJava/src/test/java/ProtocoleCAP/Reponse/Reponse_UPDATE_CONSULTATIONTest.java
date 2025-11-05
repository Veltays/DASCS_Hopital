package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Reponse_UPDATE_CONSULTATIONTest {

    @Test
    public void testReponseValideTrue() {
        Reponse_UPDATE_CONSULTATION reponse = new Reponse_UPDATE_CONSULTATION(true);
        assertTrue(reponse.isValide(),
                "La réponse UPDATE_CONSULTATION devrait être valide (true) lorsque la mise à jour réussit");
    }

    @Test
    public void testReponseValideFalse() {
        Reponse_UPDATE_CONSULTATION reponse = new Reponse_UPDATE_CONSULTATION(false);
        assertFalse(reponse.isValide(),
                "La réponse UPDATE_CONSULTATION devrait être invalide (false) lorsque la mise à jour échoue");
    }
}
