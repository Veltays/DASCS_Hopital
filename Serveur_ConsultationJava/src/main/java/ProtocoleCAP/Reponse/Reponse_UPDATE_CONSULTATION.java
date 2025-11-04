package ProtocoleCAP.Reponse;
import ServeurGeneriqueTCP.protocol.Reponse;
public class Reponse_UPDATE_CONSULTATION implements Reponse{
    private boolean valide;
    public Reponse_UPDATE_CONSULTATION(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }

}
