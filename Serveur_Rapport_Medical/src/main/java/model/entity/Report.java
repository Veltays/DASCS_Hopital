package model.entity;

import java.time.LocalDate;

public class Report implements Entity
{
    private Integer id;

    private Integer idPatient;

    private Integer idMedecin;

    private LocalDate date;

    private String description;


    public Report(Integer idPatient, Integer idMedecin, LocalDate date, String description) {
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
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

    public Integer getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(Integer idMedecin) {
        this.idMedecin = idMedecin;
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

    @Override

    public String toString()
    {
        return "Report{" +
                "id=" + id +
                ", idpatient=" + idPatient +
                ", idmedecin='" + idMedecin + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
