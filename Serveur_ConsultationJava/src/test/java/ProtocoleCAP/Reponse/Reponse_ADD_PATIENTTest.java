package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Reponse_ADD_PATIENTTest {

    @Test
    public void testIdAttribueCorrect() {
        Integer id = 42;
        Reponse_ADD_PATIENT reponse = new Reponse_ADD_PATIENT(id);
        assertEquals(id, reponse.getIdAttribuer(),
                "L'identifiant retourné doit correspondre à celui passé au constructeur");
    }

    @Test
    public void testIdAttribueNull() {
        Reponse_ADD_PATIENT reponse = new Reponse_ADD_PATIENT(null);
        assertNull(reponse.getIdAttribuer(),
                "L'identifiant devrait être null si aucun id n'est attribué");
    }
}