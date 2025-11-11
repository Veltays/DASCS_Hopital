package ProtocoleCAP.Requete;

import ServeurGeneriqueTCP.protocol.Requete;

import java.time.LocalDate;
import java.util.Date;

public class Requete_ADD_CONSULTATION implements Requete {
    private static final long serialVersionUID = 1L;


    private LocalDate date;
    private String heure;
    private String duree;
    private Integer nombreConsultations;

    public Requete_ADD_CONSULTATION(LocalDate date, String heure, String duree, Integer nombreConsultations) {
        setDate(date);
        setHeure(heure);
        setDuree(duree);
        setNombreConsultations(nombreConsultations);
    }



    // 🔹 GETTERS
    public LocalDate getDate() { return date; }
    public String getHeure() { return heure; }
    public String getDuree() { return duree; }
    public Integer getNombreConsultations() { return nombreConsultations; }

    // 🔹 SETTERS (avec validations simples)
    public void setDate(LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("La date ne peut pas être nulle.");
        this.date = date;
    }

    public void setHeure(String heure) {
        if (heure == null || heure.isBlank())
            throw new IllegalArgumentException("L'heure ne peut pas être vide.");
        this.heure = heure;
    }

    public void setDuree(String duree) {
        if (duree == null || duree.isBlank())
            throw new IllegalArgumentException("La durée ne peut pas être vide.");
        this.duree = duree;
    }

    public void setNombreConsultations(Integer nombreConsultations) {
        if (nombreConsultations == null || nombreConsultations <= 0)
            throw new IllegalArgumentException("Le nombre de consultations doit être positif.");
        this.nombreConsultations = nombreConsultations;
    }

    @Override
    public String toString() {
        return "Requete_ADD_CONSULTATION{" +
                "date=" + date +
                ", heure='" + heure + '\'' +
                ", duree='" + duree + '\'' +
                ", nombreConsultations=" + nombreConsultations +
                '}';
    }
}
