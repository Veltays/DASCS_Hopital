package ProtocoleMRPS.Reponse;

import protocol.Reponse;

public class Reponse_LOGOUT implements Reponse {

    private static final long serialVersionUID = 1L;

    private boolean ok;
    private String message;

    public Reponse_LOGOUT(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }

}
