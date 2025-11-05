package ServeurGeneriqueTCP.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class ConsultationTest {

    private Consultation consultation;
    private Date now;
    private Date future;

    @BeforeEach
    void setUp() {
        now = new Date();
        future = new Date(System.currentTimeMillis() + 100000); // dans 100 secondes
        consultation = new Consultation(1, 10, 100, "08:00", future, "Rendez-vous annuel");
    }

    // ----------------------------
    // TESTS GETTERS
    // ----------------------------

    @Test
    void getId() {
        assertEquals(1, consultation.getId());
    }

    @Test
    void getDoctor_id() {
        assertEquals(10, consultation.getDoctor_id());
    }

    @Test
    void getPatient_id() {
        assertEquals(100, consultation.getPatient_id());
    }

    @Test
    void getDate() {
        assertEquals(future, consultation.getDate());
    }

    @Test
    void getReason() {
        assertEquals("Rendez-vous annuel", consultation.getReason());
    }

    @Test
    void getHour() {
        assertEquals("08:00", consultation.getHour());
    }

    // ----------------------------
    // TESTS SETTERS (valeurs valides)
    // ----------------------------

    @Test
    void setId() {
        consultation.setId(2);
        assertEquals(2, consultation.getId());
    }

    @Test
    void setDoctor_id() {
        consultation.setDoctor_id(20);
        assertEquals(20, consultation.getDoctor_id());
    }

    @Test
    void setPatient_id() {
        consultation.setPatient_id(200);
        assertEquals(200, consultation.getPatient_id());
    }

    @Test
    void setDate() {
        Date newDate = new Date(System.currentTimeMillis() + 200000);
        consultation.setDate(newDate);
        assertEquals(newDate, consultation.getDate());
    }

    @Test
    void setReason() {
        consultation.setReason("Nouvelle raison");
        assertEquals("Nouvelle raison", consultation.getReason());
    }

    @Test
    void setHour() {
        consultation.setHour("09:30");
        assertEquals("09:30", consultation.getHour());
    }

    // ----------------------------
    // TESTS EXCEPTIONS (valeurs invalides)
    // ----------------------------

    @Test
    void setId_ShouldThrow_WhenNegativeOrZero() {
        assertThrows(IllegalArgumentException.class, () -> consultation.setId(0));
        assertThrows(IllegalArgumentException.class, () -> consultation.setId(-1));
    }


    @Test
    void setDoctor_id_ShouldThrow_WhenNegativeOrZero() {
        assertThrows(IllegalArgumentException.class, () -> consultation.setDoctor_id(0));
        assertThrows(IllegalArgumentException.class, () -> consultation.setDoctor_id(-5));
    }

    @Test
    void setDoctor_id_ShouldThrow_WhenNull() {
        assertThrows(IllegalArgumentException.class, () -> consultation.setDoctor_id(null));
    }

    @Test
    void setPatient_id_ShouldThrow_WhenNegativeOrZero() {
        assertThrows(IllegalArgumentException.class, () -> consultation.setPatient_id(0));
        assertThrows(IllegalArgumentException.class, () -> consultation.setPatient_id(-10));
    }


    @Test
    void setDate_ShouldThrow_WhenNull() {
        assertThrows(IllegalArgumentException.class, () -> consultation.setDate(null));
    }


    @Test
    void setReason_ShouldThrow_WhenNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> consultation.setReason(null));
        assertThrows(IllegalArgumentException.class, () -> consultation.setReason(""));
        assertThrows(IllegalArgumentException.class, () -> consultation.setReason("   "));
    }

    @Test
    void setHour_ShouldThrow_WhenNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> consultation.setHour(null));
        assertThrows(IllegalArgumentException.class, () -> consultation.setHour(""));
        assertThrows(IllegalArgumentException.class, () -> consultation.setHour("   "));
    }

    // ----------------------------
    // TEST TOSTRING
    // ----------------------------

    @Test
    void testToString() {
        String text = consultation.toString();
        assertTrue(text.contains("Consultation"));
        assertTrue(text.contains("doctor_id"));
        assertTrue(text.contains("patient_id"));
        assertTrue(text.contains("hour"));
        assertTrue(text.contains("reason"));
        assertTrue(text.contains("date"));
        System.out.println("✅ toString() = " + text);
    }
}
