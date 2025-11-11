package ProtocoleCAP.Requete;

import protocol.Requete;

import java.time.LocalDate;
import java.util.Date;

public class Requete_SEARCH_CONSULTATIONS implements Requete {
    private static final long serialVersionUID = 1L;



    private Integer idPatient;
    private LocalDate date;

    public Requete_SEARCH_CONSULTATIONS(Integer idPatient, LocalDate date) {
        setIdPatient(idPatient);
        setDate(date);
    }

    public Integer getIdPatient() {
        return idPatient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setIdPatient(Integer idPatient) {
//        if (idPatient == null || idPatient <= 0)
//            throw new IllegalArgumentException("L'identifiant du patient doit être positif et non nul.");
        this.idPatient = idPatient;
    }

    public void setDate(LocalDate date) {
//        if (date == null)
//            throw new IllegalArgumentException("La date ne peut pas être nulle.");
        this.date = date;
    }

    @Override
    public String toString() {
        return "Requete_SEARCH_CONSULTATIONS{" +
                "idPatient=" + idPatient +
                ", date=" + date +
                '}';
    }
}
