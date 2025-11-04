package ProtocoleCAP.Requete;

import ServeurGeneriqueTCP.protocol.Requete;

import java.util.Date;

public class Requete_SEARCH_CONSULTATIONS implements Requete {

    Integer idPatient;
    Date Date;


    public Requete_SEARCH_CONSULTATIONS(Integer idPatient, Date date) {
        this.idPatient = idPatient;
        Date = date;
    }

    public Integer getIdPatient() {
        return idPatient;
    }

    public Date getDate() {
        return Date;
    }


    public void setIdPatient(Integer idPatient) {
        this.idPatient = idPatient;
    }

    public void setDate(Date date) {
        Date = date;
    }
}

