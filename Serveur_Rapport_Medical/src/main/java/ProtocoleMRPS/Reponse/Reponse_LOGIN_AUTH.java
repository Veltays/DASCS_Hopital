package ProtocoleMRPS.Reponse;

import protocol.Reponse;

public class Reponse_LOGIN_AUTH implements Reponse {
    private static final long serialVersionUID = 1L;

    private boolean isValide;
    private Integer idConnexion;

    public Reponse_LOGIN_AUTH(boolean isValide, Integer idConnexion) {
        this.isValide = isValide;
        this.idConnexion = idConnexion;
    }

    public Integer getIdConnexion() {
        return idConnexion;
    }

    public void setIdConnexion(Integer idConnexion) {
        this.idConnexion = idConnexion;
    }

    public boolean isValide() {
        return isValide;
    }

    public void setValide(boolean valide) {
        isValide = valide;
    }
    public String getMessage() {
        return "si tu vois ça c que ta eu la flemme de le faire";
    }
}
