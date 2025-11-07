package ProtocoleCAP.Requete;

import protocol.Requete;

public class Requete_DELETE_CONSULTATION implements Requete {

    private Integer idConsultation;

    public Requete_DELETE_CONSULTATION(Integer idConsultation) {
        setIdConsultation(idConsultation);
    }

    public Integer getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(Integer idConsultation) {
        if (idConsultation == null || idConsultation <= 0)
            throw new IllegalArgumentException("L'identifiant de la consultation doit être positif et non nul.");
        this.idConsultation = idConsultation;
    }

    @Override
    public String toString() {
        return "Requete_DELETE_CONSULTATION{" +
                "idConsultation=" + idConsultation +
                '}';
    }
}
