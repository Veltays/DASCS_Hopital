package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Reponse_ERRORTest {

    @Test
    public void testMessageCorrect() {
        String message = "Erreur lors de l’ajout de la consultation";
        Reponse_ERROR reponse = new Reponse_ERROR(message);

        assertEquals(message, reponse.getMessage(),
                "Le message retourné doit correspondre à celui passé au constructeur");
    }

    @Test
    public void testMessageNull() {
        Reponse_ERROR reponse = new Reponse_ERROR(null);
        assertNull(reponse.getMessage(),
                "Le message doit être null si aucun texte n’a été fourni");
    }

    @Test
    public void testToStringFormat() {
        String message = "Erreur générique";
        Reponse_ERROR reponse = new Reponse_ERROR(message);
        String expected = "Reponse_ERROR{message='Erreur générique'}";

        assertEquals(expected, reponse.toString(),
                "Le format du toString() doit correspondre au modèle attendu");
    }
}
