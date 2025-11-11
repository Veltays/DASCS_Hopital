package ServeurGeneriqueTCP.model.entity;

import model.entity.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor(1, 10, "Dupont", "Jean", rs.getInt("user_id"));
    }

    // --- Tests getters ---
    @Test
    void getId() {
        assertEquals(1, doctor.getId());
    }

    @Test
    void getSpecialty_id() {
        assertEquals(10, doctor.getSpecialty_id());
    }

    @Test
    void getLastname() {
        assertEquals("Dupont", doctor.getLastname());
    }

    @Test
    void getFirstname() {
        assertEquals("Jean", doctor.getFirstname());
    }

    // --- Tests setters valides ---
    @Test
    void setId() {
        doctor.setId(2);
        assertEquals(2, doctor.getId());
    }

    @Test
    void setSpecialty_id() {
        doctor.setSpecialty_id(20);
        assertEquals(20, doctor.getSpecialty_id());
    }

    @Test
    void setLastname() {
        doctor.setLastname("Martin");
        assertEquals("Martin", doctor.getLastname());
    }

    @Test
    void setFirstname() {
        doctor.setFirstname("Luc");
        assertEquals("Luc", doctor.getFirstname());
    }

    // --- Tests exceptions setters invalides ---

    @Test
    void setId_ShouldThrow_WhenOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> doctor.setId(0));
        assertThrows(IllegalArgumentException.class, () -> doctor.setId(-1));
    }

    @Test
    void setSpecialty_id_ShouldThrow_WhenNullOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> doctor.setSpecialty_id(null));
        assertThrows(IllegalArgumentException.class, () -> doctor.setSpecialty_id(0));
        assertThrows(IllegalArgumentException.class, () -> doctor.setSpecialty_id(-5));
    }

    @Test
    void setLastname_ShouldThrow_WhenNullOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> doctor.setLastname(null));
        assertThrows(IllegalArgumentException.class, () -> doctor.setLastname(""));
        assertThrows(IllegalArgumentException.class, () -> doctor.setLastname("   "));
    }

    @Test
    void setFirstname_ShouldThrow_WhenNullOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> doctor.setFirstname(null));
        assertThrows(IllegalArgumentException.class, () -> doctor.setFirstname(""));
        assertThrows(IllegalArgumentException.class, () -> doctor.setFirstname("   "));
    }

    // --- Test toString ---
    @Test
    void testToString() {
        String text = doctor.toString();
        assertTrue(text.contains("Doctor"));
        assertTrue(text.contains("id"));
        assertTrue(text.contains("specialty_id"));
        assertTrue(text.contains("lastname"));
        assertTrue(text.contains("firstname"));
        System.out.println("toString() = " + text);
    }
}
