package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;
import ServeurGeneriqueTCP.model.entity.Consultation;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class Reponse_SEARCH_CONSULTATIONSTest {

    @Test
    public void testListeInitialeVide() {
        Reponse_SEARCH_CONSULTATIONS reponse = new Reponse_SEARCH_CONSULTATIONS();

        assertNotNull(reponse.getConsultationsList(),
                "La liste des consultations ne doit pas être null après initialisation");
        assertTrue(reponse.getConsultationsList().isEmpty(),
                "La liste des consultations doit être vide à la création de la réponse");
    }

    @Test
    public void testAjoutConsultation() {
        Reponse_SEARCH_CONSULTATIONS reponse = new Reponse_SEARCH_CONSULTATIONS();

        Consultation c = new Consultation(
                1,     // id
                2,     // doctor_id
                3,     // patient_id
                "10:00", // hour
                new Date(), // date (aujourd’hui)
                "Contrôle général" // reason
        );

        reponse.addConsultation(c);

        ArrayList<Consultation> liste = reponse.getConsultationsList();
        assertEquals(1, liste.size(), "Une seule consultation doit être présente après ajout");
        assertSame(c, liste.get(0), "L'objet Consultation ajouté doit correspondre à celui récupéré");
    }

    @Test
    public void testAjoutPlusieursConsultations() {
        Reponse_SEARCH_CONSULTATIONS reponse = new Reponse_SEARCH_CONSULTATIONS();

        Consultation c1 = new Consultation(1, 2, 3, "09:00", new Date(), "Première");
        Consultation c2 = new Consultation(2, 2, 4, "11:00", new Date(), "Deuxième");

        reponse.addConsultation(c1);
        reponse.addConsultation(c2);

        ArrayList<Consultation> liste = reponse.getConsultationsList();
        assertEquals(2, liste.size(), "Deux consultations doivent être présentes après deux ajouts");
        assertTrue(liste.contains(c1) && liste.contains(c2), "La liste doit contenir les deux consultations ajoutées");
    }
}
