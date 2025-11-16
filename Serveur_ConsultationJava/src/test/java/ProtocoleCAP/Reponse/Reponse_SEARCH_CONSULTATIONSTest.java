package ProtocoleCAP.Reponse;

import org.junit.jupiter.api.Test;
import model.entity.Consultation;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class Reponse_SEARCH_CONSULTATIONSTest {

    @Test
    public void testListeInitialeVide() {
        Reponse_SEARCH_CONSULTATIONS reponse = new Reponse_SEARCH_CONSULTATIONS();

        assertNotNull(reponse.getConsultationsList(),
                "La liste des consultations ne doit pas être null après initialisation");
        assertTrue(reponse.getConsultationsList().isEmpty(),
                "La liste doit être vide à la création");
    }

    @Test
    public void testAjoutConsultation() {
        Reponse_SEARCH_CONSULTATIONS reponse = new Reponse_SEARCH_CONSULTATIONS();

        Consultation c = new Consultation(
                1,         // id
                2,         // doctor_id
                3,         // patient_id
                "10:00",   // hour
                LocalDate.of(2025, 1, 10), // date
                "Contrôle général",         // reason
                "30"      // duration
        );

        reponse.addConsultation(c);

        ArrayList<Consultation> liste = reponse.getConsultationsList();
        assertEquals(1, liste.size());
        assertSame(c, liste.get(0));
    }

    @Test
    public void testAjoutPlusieursConsultations() {
        Reponse_SEARCH_CONSULTATIONS reponse = new Reponse_SEARCH_CONSULTATIONS();

        Consultation c1 = new Consultation(
                1, 2, 3, "09:00",
                LocalDate.of(2025, 1, 11),
                "Première",
                "30"
        );

        Consultation c2 = new Consultation(
                2, 2, 4, "11:00",
                LocalDate.of(2025, 1, 12),
                "Deuxième",
                "45"
        );

        reponse.addConsultation(c1);
        reponse.addConsultation(c2);

        ArrayList<Consultation> liste = reponse.getConsultationsList();
        assertEquals(2, liste.size());
        assertTrue(liste.contains(c1));
        assertTrue(liste.contains(c2));
    }
}