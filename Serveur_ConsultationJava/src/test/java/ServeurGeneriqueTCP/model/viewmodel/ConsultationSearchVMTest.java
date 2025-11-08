package ServeurGeneriqueTCP.model.viewmodel;

import model.viewmodel.ConsultationSearchVM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationSearchVMTest {

    // --- Constructeur et getters ---
    @Test
    void constructor_ShouldAssignValuesCorrectly() {
        ConsultationSearchVM vm = new ConsultationSearchVM(1, 2, 3);

        assertEquals(1, vm.getId());
        assertEquals(2, vm.getDoctor_id());
        assertEquals(3, vm.getPatient_id());
    }

    // --- Vérifications des IDs positifs ---
    @Test
    void setId_ShouldThrowException_WhenNegativeOrZero() {
        ConsultationSearchVM vm = new ConsultationSearchVM(null, null, null);

        assertThrows(IllegalArgumentException.class, () -> vm.setId(0));
        assertThrows(IllegalArgumentException.class, () -> vm.setId(-5));
    }

    @Test
    void setDoctorId_ShouldThrowException_WhenNegativeOrZero() {
        ConsultationSearchVM vm = new ConsultationSearchVM(null, null, null);

        assertThrows(IllegalArgumentException.class, () -> vm.setDoctor_id(0));
        assertThrows(IllegalArgumentException.class, () -> vm.setDoctor_id(-10));
    }

    @Test
    void setPatientId_ShouldThrowException_WhenNegativeOrZero() {
        ConsultationSearchVM vm = new ConsultationSearchVM(null, null, null);

        assertThrows(IllegalArgumentException.class, () -> vm.setPatient_id(0));
        assertThrows(IllegalArgumentException.class, () -> vm.setPatient_id(-3));
    }

    // --- Vérifications valides ---
    @Test
    void setters_ShouldAcceptPositiveOrNullValues() {
        ConsultationSearchVM vm = new ConsultationSearchVM(null, null, null);

        vm.setId(5);
        vm.setDoctor_id(10);
        vm.setPatient_id(15);

        assertEquals(5, vm.getId());
        assertEquals(10, vm.getDoctor_id());
        assertEquals(15, vm.getPatient_id());

        // Remise à null autorisée
        vm.setId(null);
        vm.setDoctor_id(null);
        vm.setPatient_id(null);

        assertNull(vm.getId());
        assertNull(vm.getDoctor_id());
        assertNull(vm.getPatient_id());
    }

    // --- toString ---
    @Test
    void toString_ShouldContainAllFields() {
        ConsultationSearchVM vm = new ConsultationSearchVM(1, 2, 3);
        String result = vm.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("doctor_id=2"));
        assertTrue(result.contains("patient_id=3"));
    }
}
