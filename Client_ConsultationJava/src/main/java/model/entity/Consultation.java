package model.entity;

import java.util.Date;

public class Consultation implements Entity  {
    private static final long serialVersionUID = 1L;


    private Integer id;
    private Integer doctor_id;
    private Integer patient_id;
    private String hour;
    private Date date;
    private String reason;

    public Consultation(Integer id, Integer doctor_id, Integer patient_id, String hour, Date date, String reason) {
        setId(id);
        setDoctor_id(doctor_id);
        setPatient_id(patient_id);
        setHour(hour);
        setDate(date);
        setReason(reason);
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

    public Date getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public String getHour() {
        return hour;
    }

    // SETTERS AVEC VALIDATIONS
    public void setId(Integer id) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("ID must be positive if provided.");
        this.id = id;
    }

    public void setDoctor_id(Integer doctor_id) {
        if (doctor_id == null || doctor_id <= 0)
            throw new IllegalArgumentException("Doctor ID must be positive and non-null.");
        this.doctor_id = doctor_id;
    }

    public void setPatient_id(Integer patient_id) {
        if (patient_id <= 0)
            throw new IllegalArgumentException("Patient ID must be positive and non-null.");
        this.patient_id = patient_id;
    }

    public void setDate(Date date) {
        if (date == null)
            throw new IllegalArgumentException("Date cannot be null.");

        Date now = new Date();
        long diff = date.getTime() - now.getTime();

        // autorise aujourd'hui si la différence est inférieure à 24h dans le passé
        if (diff < -86400000) {
            throw new IllegalArgumentException("Date must be today or in the future.");
        }

        this.date = date;
    }

    public void setReason(String reason) {
        if (reason == null || reason.isBlank())
            throw new IllegalArgumentException("Reason cannot be null or empty.");
        this.reason = reason;
    }

    public void setHour(String hour) {
        if (hour == null || hour.isBlank())
            throw new IllegalArgumentException("Hour cannot be null or empty.");
        this.hour = hour;
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
