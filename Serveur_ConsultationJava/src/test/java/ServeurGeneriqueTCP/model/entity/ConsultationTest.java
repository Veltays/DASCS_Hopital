package ServeurGeneriqueTCP.model.entity;

import model.entity.Consultation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationTest {

    @Test
    void testConstructorAndGetters() {
        LocalDate date = LocalDate.of(2025, 1, 10);

        Consultation c = new Consultation(
                1,          // id
                2,          // doctor_id
                3,          // patient_id
                "14:00",    // hour
                date,       // LocalDate
                "Check-up", // reason
                "30"        // duration
        );

        assertEquals(1, c.getId());
        assertEquals(2, c.getDoctor_id());
        assertEquals(3, c.getPatient_id());
        assertEquals("14:00", c.getHour());
        assertEquals(date, c.getDate());
        assertEquals("Check-up", c.getReason());
        assertEquals("30", c.getDuration());
    }

    @Test
    void testSetters() {
        Consultation c = new Consultation();

        LocalDate date = LocalDate.of(2025, 2, 20);

        c.setId(5);
        c.setDoctor_id(9);
        c.setPatient_id(11);
        c.setHour("09:30");
        c.setDate(date);
        c.setReason("Routine");
        c.setDuration("45");

        assertEquals(5, c.getId());
        assertEquals(9, c.getDoctor_id());
        assertEquals(11, c.getPatient_id());
        assertEquals("09:30", c.getHour());
        assertEquals(date, c.getDate());
        assertEquals("Routine", c.getReason());
        assertEquals("45", c.getDuration());
    }

    @Test
    void testToStringContainsFields() {
        Consultation c = new Consultation(
                10, 4, 7,
                "15:00",
                LocalDate.of(2025, 3, 5),
                "Control",
                "60"
        );

        String ts = c.toString();

        assertTrue(ts.contains("10"));
        assertTrue(ts.contains("4"));
        assertTrue(ts.contains("7"));
        assertTrue(ts.contains("15:00"));
        assertTrue(ts.contains("2025-03-05"));
        assertTrue(ts.contains("Control"));
        assertTrue(ts.contains("Consultation"));
    }
}
