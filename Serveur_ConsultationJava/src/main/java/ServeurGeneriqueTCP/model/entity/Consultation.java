package ServeurGeneriqueTCP.model.entity;
import java.util.Date;


public class Consultation {

    Integer id;
    Integer doctor_id;
    Integer patient_id;
    String hour;
    Date date;
    String reason;

    public Consultation(Integer id, Integer doctor_id, Integer patient_id, String hour, Date date, String reason) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.hour = hour;
        this.date = date;
        this.reason = reason;
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
    public Date getDate() {
        return date;
    }
    public String getReason() {
        return reason;
    }
    public String getHour() {
        return hour;
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
    public void setDate(Date date) {
        this.date = date;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public void setHour(String hour) {
        this.hour = hour;
    }

}
