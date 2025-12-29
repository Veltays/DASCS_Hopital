package ProtocoleMRPS.Reponse;

import protocol.Reponse;

public class Reponse_HANDSHAKE implements Reponse {

    private boolean ok;
    private String message;

    public String getMessage() {
        return message;
    }

    public boolean isOk()
    {
        return ok;
    }

    public Reponse_HANDSHAKE(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }
}
