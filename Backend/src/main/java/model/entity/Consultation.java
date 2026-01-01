package model.entity;

import java.time.LocalDate;
import java.util.Date;

public class Consultation implements Entity {
    private static final long serialVersionUID = 1L;


    private Integer id;
    private Integer doctor_id;
    private Integer patient_id;
    private String hour;
    private LocalDate date;
    private String Duration;
    private String reason;




    public Consultation(Integer id, Integer doctor_id, Integer patient_id, String hour, LocalDate date, String reason, String duration) {
        setId(id);
        setDoctor_id(doctor_id);
        setPatient_id(patient_id);
        setHour(hour);
        setDate(date);
        setReason(reason);
        setDuration(duration);
    }

    public Consultation() {

    }

    // 🔹 GETTERS
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

    public String getReason() {
        return reason;
    }

    public String getHour() {
        return hour;
    }

    public String getDuration() {
        return Duration;
    }



    // SETTERS AVEC VALIDATIONS
    public void setId(Integer id) {
        this.id = id;
    }

    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }


    // 🔹 TO STRING
    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", doctor_id=" + doctor_id +
                ", patient_id=" + patient_id +
                ", hour='" + hour + '\'' +
                ", date=" + date +
                ", reason='" + reason + '\'' +
                '}';
    }
}
