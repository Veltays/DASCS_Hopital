package ProtocoleCAP.Reponse;
import protocol.Reponse;

public class Reponse_DELETE_CONSULTATION implements Reponse{
    private boolean valide;
    public Reponse_DELETE_CONSULTATION(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }


}
