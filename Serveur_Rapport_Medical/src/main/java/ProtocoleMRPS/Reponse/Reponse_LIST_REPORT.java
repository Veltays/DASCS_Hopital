package ProtocoleMRPS.Reponse;

import protocol.Reponse;
import java.io.Serializable;

public class Reponse_LIST_REPORT implements Reponse, Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] dataChiffree;
    private String hmac;
    private boolean ok;

    public Reponse_LIST_REPORT(byte[] dataChiffree, String hmac, boolean ok) {
        this.dataChiffree = dataChiffree;
        this.hmac = hmac;
        this.ok = ok;
    }

    public byte[] getDataChiffree() {
        return dataChiffree;
    }

    public String getHmac() {
        return hmac;
    }

    public boolean isOk() {
        return ok;
    }
}
