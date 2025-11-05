package ServeurGeneriqueTCP.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient patient;
    private Date birthDate;

    @BeforeEach
    void setUp() {
        birthDate = new Date(System.currentTimeMillis() - 1_000_000_000); // ~11 jours avant
        patient = new Patient(1, "Durand", "Sophie", birthDate);
    }

    // --- Tests Getters ---
    @Test
    void getId() {
        assertEquals(1, patient.getId());
    }

    @Test
    void getLastname() {
        assertEquals("Durand", patient.getLastname());
    }

    @Test
    void getFirstname() {
        assertEquals("Sophie", patient.getFirstname());
    }

    @Test
    void getBirthDate() {
        assertEquals(birthDate, patient.getBirthDate());
    }

    // --- Tests Setters valides ---
    @Test
    void setId() {
        patient.setId(2);
        assertEquals(2, patient.getId());
    }

    @Test
    void setLastname() {
        patient.setLastname("Martin");
        assertEquals("Martin", patient.getLastname());
    }

    @Test
    void setFirstname() {
        patient.setFirstname("Claire");
        assertEquals("Claire", patient.getFirstname());
    }

    @Test
    void setBirthDate() {
        Date newDate = new Date(System.currentTimeMillis() - 2_000_000_000);
        patient.setBirthDate(newDate);
        assertEquals(newDate, patient.getBirthDate());
    }

    // --- Tests exceptions invalides ---
    @Test
    void setId_ShouldThrow_WhenInvalid() {
        assertThrows(IllegalArgumentException.class, () -> patient.setId(0));
        assertThrows(IllegalArgumentException.class, () -> patient.setId(-1));
        assertThrows(IllegalArgumentException.class, () -> patient.setId(null));
    }

    @Test
    void setLastname_ShouldThrow_WhenNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> patient.setLastname(null));
        assertThrows(IllegalArgumentException.class, () -> patient.setLastname(""));
        assertThrows(IllegalArgumentException.class, () -> patient.setLastname("   "));
    }

    @Test
    void setFirstname_ShouldThrow_WhenNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> patient.setFirstname(null));
        assertThrows(IllegalArgumentException.class, () -> patient.setFirstname(""));
        assertThrows(IllegalArgumentException.class, () -> patient.setFirstname("   "));
    }

    @Test
    void setBirthDate_ShouldThrow_WhenNull() {
        assertThrows(IllegalArgumentException.class, () -> patient.setBirthDate(null));
    }

    @Test
    void setBirthDate_ShouldThrow_WhenFutureDate() {
        Date future = new Date(System.currentTimeMillis() + 100_000);
        assertThrows(IllegalArgumentException.class, () -> patient.setBirthDate(future));
    }

    // --- Test toString ---
    @Test
    void testToString() {
        String result = patient.toString();
        assertTrue(result.contains("Patient"));
        assertTrue(result.contains("id"));
        assertTrue(result.contains("lastname"));
        assertTrue(result.contains("firstname"));
        assertTrue(result.contains("birth_date"));
        System.out.println("toString() = " + result);
    }
}
