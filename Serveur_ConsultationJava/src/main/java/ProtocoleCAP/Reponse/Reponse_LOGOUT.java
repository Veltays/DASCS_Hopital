package ProtocoleCAP.Reponse;
import protocol.Reponse;

public class Reponse_LOGOUT implements Reponse {
    private static final long serialVersionUID = 1L;
    private boolean valide;



    public Reponse_LOGOUT(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }



}
