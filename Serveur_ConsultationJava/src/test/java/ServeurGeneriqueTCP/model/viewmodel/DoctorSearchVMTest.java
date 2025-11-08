package ServeurGeneriqueTCP.model.viewmodel;

import model.viewmodel.DoctorSearchVM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorSearchVMTest {

    // --- Constructeur et getters ---
    @Test
    void constructor_ShouldAssignValuesCorrectly() {
        DoctorSearchVM vm = new DoctorSearchVM(1, 2, "Dupont");

        assertEquals(1, vm.getId());
        assertEquals(2, vm.getSpecialty_id());
        assertEquals("Dupont", vm.getLastname());
    }

    // --- Validation des IDs ---
    @Test
    void setId_ShouldThrowException_WhenNegativeOrZero() {
        DoctorSearchVM vm = new DoctorSearchVM(null, null, null);

        assertThrows(IllegalArgumentException.class, () -> vm.setId(0));
        assertThrows(IllegalArgumentException.class, () -> vm.setId(-4));
    }

    @Test
    void setSpecialtyId_ShouldThrowException_WhenNegativeOrZero() {
        DoctorSearchVM vm = new DoctorSearchVM(null, null, null);

        assertThrows(IllegalArgumentException.class, () -> vm.setSpecialty_id(0));
        assertThrows(IllegalArgumentException.class, () -> vm.setSpecialty_id(-9));
    }

    // --- Validation du nom ---
    @Test
    void setLastname_ShouldThrowException_WhenBlank() {
        DoctorSearchVM vm = new DoctorSearchVM(null, null, null);

        assertThrows(IllegalArgumentException.class, () -> vm.setLastname("   "));
    }

    @Test
    void setLastname_ShouldTrimAndAcceptValidName() {
        DoctorSearchVM vm = new DoctorSearchVM(null, null, "  Martin  ");
        assertEquals("Martin", vm.getLastname());
    }

    @Test
    void setters_ShouldAcceptNullValues() {
        DoctorSearchVM vm = new DoctorSearchVM(null, null, null);

        vm.setId(null);
        vm.setSpecialty_id(null);
        vm.setLastname(null);

        assertNull(vm.getId());
        assertNull(vm.getSpecialty_id());
        assertNull(vm.getLastname());
    }

    // --- toString ---
    @Test
    void toString_ShouldContainAllFields() {
        DoctorSearchVM vm = new DoctorSearchVM(1, 2, "Durand");
        String result = vm.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("specialty_id=2"));
        assertTrue(result.contains("Durand"));
    }
}
