package ProtocoleMRPS.Reponse;

import protocol.Reponse;

import java.io.Serializable;

public class Reponse_ADD_REPORT implements Reponse, Serializable {
    private static final long serialVersionUID = 1L;
    private boolean isValide;

    public boolean isValide() {
        return isValide;
    }

    public void setValide(boolean valide) {
        isValide = valide;
    }

    public Reponse_ADD_REPORT(boolean isValide) {
        this.isValide = isValide;
    }
}
