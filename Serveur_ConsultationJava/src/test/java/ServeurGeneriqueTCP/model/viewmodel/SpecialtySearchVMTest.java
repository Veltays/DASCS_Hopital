package ServeurGeneriqueTCP.model.viewmodel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpecialtySearchVMTest {

    // --- Constructeur ---
    @Test
    void constructor_ShouldAssignValuesCorrectly() {
        SpecialtySearchVM vm = new SpecialtySearchVM(1, "Cardiologie");

        assertEquals(1, vm.getId());
        assertEquals("Cardiologie", vm.getName());
    }

    // --- Validation ID ---
    @Test
    void setId_ShouldThrowException_WhenZeroOrNegative() {
        SpecialtySearchVM vm = new SpecialtySearchVM(null, null);

        assertThrows(IllegalArgumentException.class, () -> vm.setId(0));
        assertThrows(IllegalArgumentException.class, () -> vm.setId(-3));
    }

    @Test
    void setId_ShouldAcceptNullOrPositive() {
        SpecialtySearchVM vm = new SpecialtySearchVM(null, null);
        vm.setId(5);
        assertEquals(5, vm.getId());

        vm.setId(null);
        assertNull(vm.getId());
    }

    // --- Validation name ---
    @Test
    void setName_ShouldThrowException_WhenBlank() {
        SpecialtySearchVM vm = new SpecialtySearchVM(null, null);
        assertThrows(IllegalArgumentException.class, () -> vm.setName("   "));
    }

    @Test
    void setName_ShouldTrimAndAcceptValidName() {
        SpecialtySearchVM vm = new SpecialtySearchVM(null, "  Neurologie  ");
        assertEquals("Neurologie", vm.getName());
    }

    @Test
    void setName_ShouldAcceptNull() {
        SpecialtySearchVM vm = new SpecialtySearchVM(null, null);
        assertNull(vm.getName());
    }

    // --- toString ---
    @Test
    void toString_ShouldContainAllFields() {
        SpecialtySearchVM vm = new SpecialtySearchVM(2, "Dermatologie");
        String result = vm.toString();

        assertTrue(result.contains("id=2"));
        assertTrue(result.contains("Dermatologie"));
    }
}
