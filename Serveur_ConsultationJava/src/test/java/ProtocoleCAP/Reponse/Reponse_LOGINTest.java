package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Reponse_LOGINTest {

    @Test
    public void testReponseValideTrue() {
        Reponse_LOGIN reponse = new Reponse_LOGIN(true);
        assertTrue(reponse.isValide(),
                "La réponse LOGIN devrait être valide (true) lorsque l'authentification réussit");
    }

    @Test
    public void testReponseValideFalse() {
        Reponse_LOGIN reponse = new Reponse_LOGIN(false);
        assertFalse(reponse.isValide(),
                "La réponse LOGIN devrait être invalide (false) lorsque l'authentification échoue");
    }
}
