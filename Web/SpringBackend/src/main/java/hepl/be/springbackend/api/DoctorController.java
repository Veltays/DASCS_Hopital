package hepl.be.springbackend.api;

import model.dao.DoctorDAO;
import model.entity.Doctor;
import model.viewmodel.DoctorSearchVM;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorDAO doctorDAO = new DoctorDAO();

    @GetMapping
    public List<Doctor> getDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer specialty
    ) throws SQLException {

        // 1️⃣ critères de recherche
        DoctorSearchVM doctorSearchVM = new DoctorSearchVM();

        if (name != null) {
            doctorSearchVM.setLastname(name);
        }

        if (specialty != null) {
            doctorSearchVM.setSpecialty_id(specialty);
        }

        // 2️⃣ récupération des données
        return doctorDAO.load(doctorSearchVM);
    }
}
