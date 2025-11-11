package ProtocoleCAP.Requete;

import ServeurGeneriqueTCP.protocol.Requete;

import java.time.LocalDate;
import java.util.Date;

public class Requete_UPDATE_CONSULTATION implements Requete {

    private static final long serialVersionUID = 1L;


    private Integer idConsultation;
    private LocalDate nouvelleDate;
    private String nouvelleHeure;
    private Integer idPatient;
    private String nouvelleRaison;

    public Requete_UPDATE_CONSULTATION(Integer idConsultation, LocalDate nouvelleDate, String nouvelleHeure,
                                       Integer idPatient, String nouvelleRaison) {
        setIdConsultation(idConsultation);
        setNouvelleDate(nouvelleDate);
        setNouvelleHeure(nouvelleHeure);
        setIdPatient(idPatient);
        setNouvelleRaison(nouvelleRaison);
    }

    // 🔹 GETTERS
    public Integer getIdConsultation() { return idConsultation; }
    public LocalDate getNouvelleDate() { return nouvelleDate; }
    public String getNouvelleHeure() { return nouvelleHeure; }
    public Integer getIdPatient() { return idPatient; }
    public String getNouvelleRaison() { return nouvelleRaison; }

    // 🔹 SETTERS avec validations
    public void setIdConsultation(Integer idConsultation) {
        if (idConsultation == null || idConsultation <= 0)
            throw new IllegalArgumentException("L'identifiant de la consultation doit être positif et non nul.");
        this.idConsultation = idConsultation;
    }

    public void setNouvelleDate(LocalDate nouvelleDate) {
        if (nouvelleDate == null)
            throw new IllegalArgumentException("La date ne peut pas être nulle.");
        this.nouvelleDate = nouvelleDate;
    }

    public void setNouvelleHeure(String nouvelleHeure) {
        if (nouvelleHeure == null)
            throw new IllegalArgumentException("L'heure ne peut pas être nulle.");
        this.nouvelleHeure = nouvelleHeure;
    }

    public void setIdPatient(Integer idPatient) {
//        if (idPatient == null || idPatient <= 0)
//            throw new IllegalArgumentException("L'identifiant du patient doit être positif et non nul.");
        this.idPatient = idPatient;
    }

    public void setNouvelleRaison(String nouvelleRaison) {
        if (nouvelleRaison == null || nouvelleRaison.isBlank())
            throw new IllegalArgumentException("La raison ne peut pas être vide ou nulle.");
        this.nouvelleRaison = nouvelleRaison;
    }

    @Override
    public String toString() {
        return "Requete_UPDATE_CONSULTATION{" +
                "idConsultation=" + idConsultation +
                ", nouvelleDate=" + nouvelleDate +
                ", nouvelleHeure=" + nouvelleHeure +
                ", idPatient=" + idPatient +
                ", nouvelleRaison='" + nouvelleRaison + '\'' +
                '}';
    }
}
