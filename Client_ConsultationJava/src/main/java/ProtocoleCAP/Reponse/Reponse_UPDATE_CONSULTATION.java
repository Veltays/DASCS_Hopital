package ProtocoleCAP.Reponse;
import protocol.Reponse;
public class Reponse_UPDATE_CONSULTATION implements Reponse{
    private static final long serialVersionUID = 1L;

    private boolean valide;
    public Reponse_UPDATE_CONSULTATION(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }

}
