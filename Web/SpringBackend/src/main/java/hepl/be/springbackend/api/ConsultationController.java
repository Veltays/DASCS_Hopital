package hepl.be.springbackend.api;

import model.dao.ConsultationDAO;
import model.dao.DoctorDAO;
import model.entity.Consultation;
import model.entity.Doctor;
import model.viewmodel.ConsultationSearchVM;
import model.viewmodel.DoctorSearchVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    // =====================================================
    // GET /api/consultations
    // =====================================================
    @GetMapping
    public ResponseEntity<List<Consultation>> getConsultations(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer doctor_id,
            @RequestParam(required = false) String doctor,
            @RequestParam(required = false) Integer specialty_id
    ) {

        ConsultationSearchVM consultationVM = new ConsultationSearchVM();
        DoctorSearchVM doctorVM = new DoctorSearchVM();

        // filtres directs consultation
        if (date != null) {
            consultationVM.setDate(LocalDate.parse(date));
        }
        if (patientId != null) {
            consultationVM.setPatient_id(patientId);
        }
        if (doctor_id != null) {
            consultationVM.setDoctor_id(doctor_id);
        }

        // -------------------------------------------------
        // filtre via doctor / specialty
        // -------------------------------------------------
        if (doctor != null || specialty_id != null) {

            if (doctor != null) {
                doctorVM.setLastname(doctor);
            }
            if (specialty_id != null) {
                doctorVM.setSpecialty_id(specialty_id);
            }

            try {
                ArrayList<Doctor> doctors = doctorDAO.load(doctorVM);
                if (doctors.isEmpty()) {
                    return ResponseEntity.ok(List.of());
                }

                ArrayList<Consultation> all = new ArrayList<>();
                for (Doctor d : doctors) {
                    consultationVM.setDoctor_id(d.getId());
                    all.addAll(consultationDAO.load(consultationVM));
                }
                return ResponseEntity.ok(all);

            } catch (SQLException e) {
                return ResponseEntity.internalServerError().build();
            }
        }

        // -------------------------------------------------
        // récupération standard
        // -------------------------------------------------
        try {
            ArrayList<Consultation> consultations = consultationDAO.load(consultationVM);

            // même logique que ton code : si pas patientId → seulement dispo
            if (patientId == null) {
                ArrayList<Consultation> available = new ArrayList<>();
                for (Consultation c : consultations) {
                    if (c.getPatient_id() == 0) {
                        available.add(c);
                    }
                }
                return ResponseEntity.ok(available);
            }

            return ResponseEntity.ok(consultations);

        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // =====================================================
    // PUT /api/consultations?id=...
    // =====================================================
    @PutMapping
    public ResponseEntity<String> assignConsultation(
            @RequestParam Integer id,
            @RequestBody Consultation body
    ) {

        try {
            consultationDAO.load(new ConsultationSearchVM());
            Consultation cons = consultationDAO.getById(id);

            if (cons == null) {
                return ResponseEntity.ok("Consultation dont exist");
            }

            cons.setPatient_id(body.getPatient_id());
            cons.setReason(body.getReason());

            consultationDAO.save(cons);
            return ResponseEntity.ok("yes");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("no");
        }
    }

    // =====================================================
    // DELETE /api/consultations?id=...
    // =====================================================
    @DeleteMapping
    public ResponseEntity<String> freeConsultation(
            @RequestParam Integer id
    ) {

        try {
            consultationDAO.load(new ConsultationSearchVM());
            Consultation cons = consultationDAO.getById(id);

            if (cons == null) {
                return ResponseEntity.ok("no");
            }

            cons.setPatient_id(null);
            cons.setReason(null);
            consultationDAO.save(cons);

            return ResponseEntity.ok("yes");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("no");
        }
    }
}
