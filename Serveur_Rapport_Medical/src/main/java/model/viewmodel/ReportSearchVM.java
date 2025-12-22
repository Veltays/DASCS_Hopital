package model.viewmodel;

import java.time.LocalDate;

public class ReportSearchVM
{
    private Integer id;
    private Integer idPatient;
    private Integer idMedecin;
    private LocalDate date;
    private String description;

    public ReportSearchVM(Integer id, Integer idPatient, Integer idMedecin, LocalDate date, String description) {
        this.id = id;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
        this.date = date;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdPatient() {
        return idPatient;
    }

    public Integer getIdMedecin() {
        return idMedecin;
    }

    public LocalDate getDateReport() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdPatient(Integer idPatient) {
        this.idPatient = idPatient;
    }

    public void setIdMedecin(Integer idMedecin) {
        this.idMedecin = idMedecin;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return "si tu vois ça c'est que j'ai eu la flemme de finir";
    }
}
