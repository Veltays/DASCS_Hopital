package org.example.model.viewmodel;


import org.example.model.entity.Consultation;

import java.util.Date;

public class ConsultationSearchVM {

    Integer id;
    Integer doctor_id;

    Integer patient_id;
    Date date;
    String reason;

    public ConsultationSearchVM(Integer id, Integer doctor_id, Integer patient_id, Date date, String reason) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.date = date;
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
