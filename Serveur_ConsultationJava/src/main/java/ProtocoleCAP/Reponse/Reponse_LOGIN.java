package ProtocoleCAP.Reponse;
import ServeurGeneriqueTCP.protocol.Reponse;

public class Reponse_LOGIN implements Reponse{
    private static final long serialVersionUID = 1L;
    private boolean valide;
    public Reponse_LOGIN(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }
}
