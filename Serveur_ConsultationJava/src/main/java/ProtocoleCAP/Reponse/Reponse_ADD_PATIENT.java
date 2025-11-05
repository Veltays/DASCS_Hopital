package ProtocoleCAP.Reponse;
import ServeurGeneriqueTCP.protocol.Reponse;
public class Reponse_ADD_PATIENT implements Reponse{

    private Integer idAttribuer;

    public Reponse_ADD_PATIENT(Integer idAttribuer) {
        this.idAttribuer = idAttribuer;
    }

    public Integer getIdAttribuer() {
        return idAttribuer;
    }

}
