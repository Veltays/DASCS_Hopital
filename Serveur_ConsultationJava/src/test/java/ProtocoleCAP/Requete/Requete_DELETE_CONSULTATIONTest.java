package ProtocoleCAP.Requete;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Requete_DELETE_CONSULTATIONTest {

    @Test
    public void testCreationValide() {
        Requete_DELETE_CONSULTATION requete = new Requete_DELETE_CONSULTATION(5);

        assertEquals(5, requete.getIdConsultation(),
                "L'identifiant de la consultation doit être correctement enregistré");
    }

    @Test
    public void testSetterValide() {
        Requete_DELETE_CONSULTATION requete = new Requete_DELETE_CONSULTATION(1);
        requete.setIdConsultation(10);

        assertEquals(10, requete.getIdConsultation(),
                "Le setter doit modifier correctement la valeur de l'identifiant");
    }

    @Test
    public void testValeurInvalide() {
        assertThrows(IllegalArgumentException.class,
                () -> new Requete_DELETE_CONSULTATION(null),
                "Un identifiant null doit lever une exception");

        assertThrows(IllegalArgumentException.class,
                () -> new Requete_DELETE_CONSULTATION(0),
                "Un identifiant égal à 0 doit lever une exception");

        Requete_DELETE_CONSULTATION requete = new Requete_DELETE_CONSULTATION(1);
        assertThrows(IllegalArgumentException.class,
                () -> requete.setIdConsultation(-5),
                "Un identifiant négatif doit lever une exception");
    }

    @Test
    public void testToStringContientChamps() {
        Requete_DELETE_CONSULTATION requete = new Requete_DELETE_CONSULTATION(42);
        String toString = requete.toString();

        assertTrue(toString.contains("42"),
                "Le toString() doit contenir l'identifiant de la consultation");
        assertTrue(toString.contains("Requete_DELETE_CONSULTATION"),
                "Le toString() doit contenir le nom de la classe");
    }
}
