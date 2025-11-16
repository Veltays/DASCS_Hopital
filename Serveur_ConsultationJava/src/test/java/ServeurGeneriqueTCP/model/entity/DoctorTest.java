package ServeurGeneriqueTCP.model.entity;

import model.entity.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor(1, 10, "Dupont", "Jean", 5);
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

    @Test
    void getUser_id() {
        assertEquals(5, doctor.getUser_id());
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

    @Test
    void setUser_id() {
        doctor.setUser_id(12);
        assertEquals(12, doctor.getUser_id());
    }

    // --- Test toString ---
    @Test
    void testToString() {
        String text = doctor.toString();
        assertTrue(text.contains("Doctor"));
        assertTrue(text.contains("id=1"));
        assertTrue(text.contains("specialty_id=10"));
        assertTrue(text.contains("lastname='Dupont'"));
        assertTrue(text.contains("firstname='Jean'"));
    }
}
