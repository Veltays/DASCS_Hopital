package ProtocoleCAP.Requete;

import ServeurGeneriqueTCP.protocol.Requete;

public class Requete_DELETE_CONSULTATION implements Requete {

    Integer idCons;


    public Requete_DELETE_CONSULTATION(Integer idCons) {
        this.idCons = idCons;
    }


    public Integer getIdCons() {
        return idCons;
    }

    public void setIdCons(Integer idCons) {
        this.idCons = idCons;
    }
}
