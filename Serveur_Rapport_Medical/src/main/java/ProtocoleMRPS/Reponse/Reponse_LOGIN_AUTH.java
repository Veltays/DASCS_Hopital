package ProtocoleMRPS.Reponse;

import protocol.Reponse;

public class Reponse_LOGIN_AUTH implements Reponse {

    private boolean isValide;

    public Reponse_LOGIN_AUTH(boolean isValide) {
        this.isValide = isValide;
    }

    public boolean isValide() {
        return isValide;
    }

    public void setValide(boolean valide) {
        isValide = valide;
    }
}
