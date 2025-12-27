package ProtocoleMRPS.Reponse;

import protocol.*;

public class Reponse_LOGIN_REQUEST  implements Reponse{

    private boolean isValide;
    private String sel;

    public Reponse_LOGIN_REQUEST(String sel, boolean isValide) {
        this.sel = sel;
        this.isValide = isValide;
    }

    public boolean isValide() {
        return isValide;
    }

    public void setValide(boolean valide) {
        isValide = valide;
    }

    public String getSel() {
        return sel;
    }

    public void setSel(String sel) {
        this.sel = sel;
    }
}
