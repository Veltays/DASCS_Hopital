package ProtocoleCAP.Requete;
import ServeurGeneriqueTCP.protocol.Requete;

import java.util.Date;

public class Requete_ADD_CONSULTATION implements Requete
{
    private Date date;
    private String heure;
    private String duree;
    private Integer nombreConsultation;

    public Requete_ADD_CONSULTATION(Date date, String heure, String duree, Integer nombreConsultation) {
        this.date = date;
        this.heure = heure;
        this.duree = duree;
        this.nombreConsultation = nombreConsultation;
    }

    public Date getDate() {
        return date;
    }
    public String getHeure() {
        return heure;
    }
    public String getDuree() {
        return duree;
    }
    public Integer getNombreConsultation() {
        return nombreConsultation;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public void setHeure(String heure) {
        this.heure = heure;
    }
    public void setDuree(String duree) {
        this.duree = duree;
    }
    public void setNombreConsultation(Integer nombreConsultation) {
        this.nombreConsultation = nombreConsultation;
    }
}