package ProtocoleMRPS.Requete;

import protocol.Requete;

import java.time.LocalDate;
public class Requete_ADD_REPORT implements Requete
{
    private Integer id;
    private Integer idPatient;
    private LocalDate date;
    private String description;
    // cryptés symétriquement + signature du médecin

    public Requete_ADD_REPORT(Integer id, Integer idPatient, LocalDate date, String description) {
        this.id = id;
        this.idPatient = idPatient;
        this.date = date;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(Integer idPatient) {
        this.idPatient = idPatient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
