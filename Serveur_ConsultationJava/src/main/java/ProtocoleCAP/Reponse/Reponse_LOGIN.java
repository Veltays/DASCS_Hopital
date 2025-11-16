package ProtocoleCAP.Reponse;
import protocol.Reponse;

public class Reponse_LOGIN implements Reponse {
    private static final long serialVersionUID = 1L;
    private boolean valide;
    private Integer IdConnexion;


    public Reponse_LOGIN(boolean v,Integer idConnexion) {
        valide = v;
        IdConnexion = idConnexion;
    }
    public boolean isValide() {
        return valide;
    }

    public Integer getIdConnexion() {
        return IdConnexion;
    }

    public void setIdConnexion(Integer idConnexion) {
        IdConnexion = idConnexion;
    }
}
