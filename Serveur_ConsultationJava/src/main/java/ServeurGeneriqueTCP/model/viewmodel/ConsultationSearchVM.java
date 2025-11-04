package ServeurGeneriqueTCP.model.viewmodel;


import java.util.Date;

public class ConsultationSearchVM {

    Integer id;
    Integer doctor_id;
    Integer patient_id;

    public ConsultationSearchVM(Integer id, Integer doctor_id, Integer patient_id) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
    }

    public Integer getId() {
        return id;
    }
    public Integer getDoctor_id() {
        return doctor_id;
    }
    public Integer getPatient_id() {
        return patient_id;
    }


    public void setId(Integer id) {
        this.id = id;
    }
    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }
    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

}
