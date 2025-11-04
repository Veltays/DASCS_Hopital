package ProtocoleCAP.Reponse;
import ServeurGeneriqueTCP.protocol.Reponse;

public class Reponse_ADD_CONSULTATION implements Reponse{
    private boolean valide;
    public Reponse_ADD_CONSULTATION(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }

}
