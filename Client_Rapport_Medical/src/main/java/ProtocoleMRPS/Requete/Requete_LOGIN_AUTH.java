package ProtocoleMRPS.Requete;

import protocol.Requete;

public class Requete_LOGIN_AUTH implements Requete {
    private static final long serialVersionUID = 1L;

    private String username;
    private String digest; // hash(login + password + sel);

    public Requete_LOGIN_AUTH(String username, String digest) {
        this.username = username;
        this.digest = digest;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}
