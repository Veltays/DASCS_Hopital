package ProtocoleMRPS.Reponse;

import java.io.Serializable;

public class Reponse_ADD_REPORT implements Serializable {
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
