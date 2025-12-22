package model.viewmodel;

import java.time.LocalDate;

public class ConsultationSearchVM {

    private Integer id;
    private Integer doctor_id;
    private Integer patient_id;
    private LocalDate date;

    // --- Constructeur ---
    public ConsultationSearchVM(Integer id, Integer doctor_id, Integer patient_id) {
        setId(id);
        setDoctor_id(doctor_id);
        setPatient_id(patient_id);
    }


    public ConsultationSearchVM() {

    }



    // --- Getters ---
    public Integer getId() {
        return id;
    }

    public Integer getDoctor_id() {
        return doctor_id;
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public LocalDate getDate() {
        return date;
    }


    // --- Setters avec vérifications ---
    public void setId(Integer id) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("L'id de la consultation doit être positif.");
        this.id = id;
    }

    public void setDoctor_id(Integer doctor_id) {
        if (doctor_id != null && doctor_id <= 0)
            throw new IllegalArgumentException("L'id du docteur doit être positif.");
        this.doctor_id = doctor_id;
    }

    public void setPatient_id(Integer patient_id) {
        if (patient_id != null && patient_id <= 0)
            throw new IllegalArgumentException("L'id du patient doit être positif.");
        this.patient_id = patient_id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ConsultationSearchVM{" +
                "id=" + id +
                ", doctor_id=" + doctor_id +
                ", patient_id=" + patient_id +
                '}';
    }
}
