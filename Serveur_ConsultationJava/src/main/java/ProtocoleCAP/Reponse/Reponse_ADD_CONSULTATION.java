package ProtocoleCAP.Reponse;
import protocol.Reponse;

public class Reponse_ADD_CONSULTATION implements Reponse {
    private static final long serialVersionUID = 1L;

    private boolean valide;
    public Reponse_ADD_CONSULTATION(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }

}
