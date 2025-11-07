package ProtocoleCAP.Reponse;
import protocol.Reponse;
public class Reponse_ADD_PATIENT implements Reponse{
    private static final long serialVersionUID = 1L;


    private Integer idAttribuer;

    public Reponse_ADD_PATIENT(Integer idAttribuer) {
        this.idAttribuer = idAttribuer;
    }

    public Integer getIdAttribuer() {
        return idAttribuer;
    }

}
