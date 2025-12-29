package ProtocoleMRPS.Requete;

import protocol.Requete;
import protocol.RequeteMRPS;


public class Requete_LOGIN_REQUEST extends RequeteMRPS
{
    // envoyée par le client
    private static final long serialVersionUID = 1L;
    private String username;


    public Requete_LOGIN_REQUEST(String username)
    {
        this.username=username;

    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}


