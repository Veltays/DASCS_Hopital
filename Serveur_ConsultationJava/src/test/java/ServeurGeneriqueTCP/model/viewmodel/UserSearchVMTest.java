package ServeurGeneriqueTCP.model.viewmodel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserSearchVMTest {

    // --- Constructeur ---
    @Test
    void constructor_ShouldAssignValuesCorrectly() {
        UserSearchVM vm = new UserSearchVM("admin", "doctor");

        assertEquals("admin", vm.getLogin());
        assertEquals("doctor", vm.getRole());
    }

    // --- Validation login ---
    @Test
    void setLogin_ShouldThrowException_WhenBlank() {
        UserSearchVM vm = new UserSearchVM();
        assertThrows(IllegalArgumentException.class, () -> vm.setLogin("   "));
    }

    @Test
    void setLogin_ShouldTrimAndAcceptValidValue() {
        UserSearchVM vm = new UserSearchVM();
        vm.setLogin("  alice  ");
        assertEquals("alice", vm.getLogin());
    }

    @Test
    void setLogin_ShouldAcceptNull() {
        UserSearchVM vm = new UserSearchVM();
        vm.setLogin(null);
        assertNull(vm.getLogin());
    }

    // --- Validation role ---
    @Test
    void setRole_ShouldThrowException_WhenBlank() {
        UserSearchVM vm = new UserSearchVM();
        assertThrows(IllegalArgumentException.class, () -> vm.setRole("   "));
    }

    @Test
    void setRole_ShouldThrowException_WhenInvalidRole() {
        UserSearchVM vm = new UserSearchVM();
        assertThrows(IllegalArgumentException.class, () -> vm.setRole("manager"));
    }

    @Test
    void setRole_ShouldNormalizeAndAcceptValidRole() {
        UserSearchVM vm = new UserSearchVM();
        vm.setRole("  DOCTOR  ");
        assertEquals("doctor", vm.getRole());
    }

    @Test
    void setRole_ShouldAcceptNull() {
        UserSearchVM vm = new UserSearchVM();
        vm.setRole(null);
        assertNull(vm.getRole());
    }

    // --- toString ---
    @Test
    void toString_ShouldContainAllFields() {
        UserSearchVM vm = new UserSearchVM("bob", "admin");
        String result = vm.toString();

        assertTrue(result.contains("bob"));
        assertTrue(result.contains("admin"));
    }
}
