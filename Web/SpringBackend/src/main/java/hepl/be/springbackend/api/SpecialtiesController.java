package hepl.be.springbackend.api;

import model.dao.SpecialtyDAO;
import model.entity.Specialty;
import model.viewmodel.SpecialtySearchVM;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtiesController {

    private final SpecialtyDAO specialtyDAO = new SpecialtyDAO();

    @GetMapping
    public List<Specialty> getAllSpecialties() throws SQLException {

        SpecialtySearchVM vm = new SpecialtySearchVM();
        return specialtyDAO.load(vm);
    }
}