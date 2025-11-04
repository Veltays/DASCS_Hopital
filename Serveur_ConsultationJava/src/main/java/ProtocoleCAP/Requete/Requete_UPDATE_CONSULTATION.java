package ProtocoleCAP.Requete;

import ServeurGeneriqueTCP.protocol.Requete;

import java.util.Date;

public class Requete_UPDATE_CONSULTATION implements Requete {

    Integer idCons;
    Date newDate;
    Date NewHours;
    Integer PatientId;
    String NewReason;

    public Requete_UPDATE_CONSULTATION(Integer idCons, Date newDate, Date newHours, Integer patientId, String newReason) {
        this.idCons = idCons;
        this.newDate = newDate;
        NewHours = newHours;
        PatientId = patientId;
        NewReason = newReason;
    }

    public Integer getIdCons() {
        return idCons;
    }

    public Date getNewDate() {
        return newDate;
    }

    public Date getNewHours() {
        return NewHours;
    }

    public Integer getPatientId() {
        return PatientId;
    }

    public String getNewReason() {
        return NewReason;
    }

    public void setIdCons(Integer idCons) {
        this.idCons = idCons;
    }

    public void setNewDate(Date newDate) {
        this.newDate = newDate;
    }

    public void setNewHours(Date newHours) {
        NewHours = newHours;
    }

    public void setPatientId(Integer patientId) {
        PatientId = patientId;
    }

    public void setNewReason(String newReason) {
        NewReason = newReason;
    }
}

