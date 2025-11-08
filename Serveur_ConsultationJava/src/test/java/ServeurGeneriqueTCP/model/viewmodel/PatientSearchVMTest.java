package ServeurGeneriqueTCP.model.viewmodel;

import model.viewmodel.PatientSearchVM;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PatientSearchVMTest {

    // --- Constructeur ---
    @Test
    void constructor_ShouldAssignValuesCorrectly() {
        PatientSearchVM vm = new PatientSearchVM(1, "Alice", "Dupont");

        assertEquals(1, vm.getId());
        assertEquals("Alice", vm.getFirstname());
        assertEquals("Dupont", vm.getLastname());
    }

    // --- Validation ID ---
    @Test
    void setId_ShouldThrowException_WhenZeroOrNegative() {
        PatientSearchVM vm = new PatientSearchVM(null, null, null);

        assertThrows(IllegalArgumentException.class, () -> vm.setId(0));
        assertThrows(IllegalArgumentException.class, () -> vm.setId(-2));
    }

    @Test
    void setId_ShouldAcceptNullOrPositive() {
        PatientSearchVM vm = new PatientSearchVM(null, null, null);
        vm.setId(3);
        assertEquals(3, vm.getId());

        vm.setId(null);
        assertNull(vm.getId());
    }

    // --- Validation firstname ---
    @Test
    void setFirstname_ShouldThrowException_WhenBlank() {
        PatientSearchVM vm = new PatientSearchVM(null, null, null);
        assertThrows(IllegalArgumentException.class, () -> vm.setFirstname("   "));
    }

    @Test
    void setFirstname_ShouldTrimValue_WhenValid() {
        PatientSearchVM vm = new PatientSearchVM(null, "  Bob  ", null);
        assertEquals("Bob", vm.getFirstname());
    }

    // --- Validation lastname ---
    @Test
    void setLastname_ShouldThrowException_WhenBlank() {
        PatientSearchVM vm = new PatientSearchVM(null, null, null);
        assertThrows(IllegalArgumentException.class, () -> vm.setLastname(" "));
    }

    @Test
    void setLastname_ShouldTrimValue_WhenValid() {
        PatientSearchVM vm = new PatientSearchVM(null, null, "  Martin  ");
        assertEquals("Martin", vm.getLastname());
    }

    // --- toString ---
    @Test
    void toString_ShouldContainAllFields() {
        PatientSearchVM vm = new PatientSearchVM(1, "Claire", "Bernard");
        String result = vm.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("Claire"));
        assertTrue(result.contains("Bernard"));
    }
}
