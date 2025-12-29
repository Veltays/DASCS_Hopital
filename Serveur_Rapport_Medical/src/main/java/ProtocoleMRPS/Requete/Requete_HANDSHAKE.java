package ProtocoleMRPS.Requete;

import protocol.RequeteMRPS;

public class Requete_HANDSHAKE extends RequeteMRPS {
    private byte[] cleSessionChiffree; // clé de session chiffrée avec rsa


    public Requete_HANDSHAKE(byte[] cleSessionChiffree) {
        this.cleSessionChiffree = cleSessionChiffree;
    }

    public byte[] getCleSessionChiffree() {
        return cleSessionChiffree;
    }

    public void setCleSessionChiffree(byte[] cleSessionChiffree) {
        this.cleSessionChiffree = cleSessionChiffree;
    }
}
