package ServeurGeneriqueTCP.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialtyTest {

    private Specialty specialty;

    @BeforeEach
    void setUp() {
        specialty = new Specialty(1, "Cardiology");
    }

    // --- Tests Getters ---
    @Test
    void getId() {
        assertEquals(1, specialty.getId());
    }

    @Test
    void getName() {
        assertEquals("Cardiology", specialty.getName());
    }

    // --- Tests Setters valides ---
    @Test
    void setId() {
        specialty.setId(2);
        assertEquals(2, specialty.getId());
    }

    @Test
    void setName() {
        specialty.setName("Neurology");
        assertEquals("Neurology", specialty.getName());
    }

    // --- Tests Exceptions ---
    @Test
    void setId_ShouldThrow_WhenInvalid() {
        assertThrows(IllegalArgumentException.class, () -> specialty.setId(0));
        assertThrows(IllegalArgumentException.class, () -> specialty.setId(-1));

    }

    @Test
    void setName_ShouldThrow_WhenNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> specialty.setName(null));
        assertThrows(IllegalArgumentException.class, () -> specialty.setName(""));
        assertThrows(IllegalArgumentException.class, () -> specialty.setName("   "));
    }

    // --- Test toString ---
    @Test
    void testToString() {
        String text = specialty.toString();
        assertTrue(text.contains("Specialty"));
        assertTrue(text.contains("id"));
        assertTrue(text.contains("name"));
        System.out.println("toString() = " + text);
    }
}
