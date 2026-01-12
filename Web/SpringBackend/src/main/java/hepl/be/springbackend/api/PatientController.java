package hepl.be.springbackend.api;

import model.dao.PatientDAO;
import model.entity.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientDAO patientDAO = new PatientDAO();

    @PostMapping
    public ResponseEntity<String> createOrUpdatePatient(
            @RequestBody Patient patient,
            @RequestParam(defaultValue = "true") boolean newPatient
    ) {

        // Validation minimale (comme avant)
        if (patient.getLastname() == null || patient.getFirstname() == null || patient.getBirthDate() == null) {
            return ResponseEntity.badRequest()
                    .body("Missing patient fields");
        }

        // UPDATE
        if (!newPatient) {
            if (patient.getId() == null) {
                return ResponseEntity.badRequest()
                        .body("patientId required for update");
            }
        } else {
            patient.setId(null); // CREATE
        }

        try {
            patientDAO.save(patient);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Database error");
        }

        // 🔹 réponse attendue par l’énoncé
        return ResponseEntity.ok(patient.getId().toString());
    }
}
