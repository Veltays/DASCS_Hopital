package ProtocoleMRPS.Requete;

import protocol.*;
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
}
