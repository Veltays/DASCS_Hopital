package ServeurGeneriqueTCP.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "jdoe", "password123", "admin");
    }

    // --- Tests getters ---
    @Test
    void getId() {
        assertEquals(1, user.getId());
    }

    @Test
    void getLogin() {
        assertEquals("jdoe", user.getLogin());
    }

    @Test
    void getPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    void getRole() {
        assertEquals("admin", user.getRole());
    }

    // --- Tests setters valides ---
    @Test
    void setId() {
        user.setId(2);
        assertEquals(2, user.getId());
    }

    @Test
    void setLogin() {
        user.setLogin("alice");
        assertEquals("alice", user.getLogin());
    }

    @Test
    void setPassword() {
        user.setPassword("secure99");
        assertEquals("secure99", user.getPassword());
    }

    @Test
    void setRole() {
        user.setRole("doctor");
        assertEquals("doctor", user.getRole());
    }

    // --- Tests exceptions setters invalides ---

    @Test
    void setId_ShouldThrow_WhenInvalid() {
        assertThrows(IllegalArgumentException.class, () -> user.setId(0));
        assertThrows(IllegalArgumentException.class, () -> user.setId(-1));
        assertThrows(IllegalArgumentException.class, () -> user.setId(null));
    }

    @Test
    void setLogin_ShouldThrow_WhenNullOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> user.setLogin(null));
        assertThrows(IllegalArgumentException.class, () -> user.setLogin(""));
        assertThrows(IllegalArgumentException.class, () -> user.setLogin("   "));
    }

    @Test
    void setPassword_ShouldThrow_WhenTooShortOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(null));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(""));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("   "));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("123"));
    }

    @Test
    void setRole_ShouldThrow_WhenInvalid() {
        assertThrows(IllegalArgumentException.class, () -> user.setRole(null));
        assertThrows(IllegalArgumentException.class, () -> user.setRole(""));
        assertThrows(IllegalArgumentException.class, () -> user.setRole("   "));
        assertThrows(IllegalArgumentException.class, () -> user.setRole("guest"));
    }

    // --- Test toString ---
    @Test
    void testToString() {
        String text = user.toString();
        assertTrue(text.contains("User"));
        assertTrue(text.contains("id"));
        assertTrue(text.contains("login"));
        assertTrue(text.contains("role"));
        System.out.println("toString() = " + text);
    }
}