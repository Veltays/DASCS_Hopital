package ProtocoleMRPS.Requete;

import protocol.Requete;


public class Requete_LOGIN_REQUEST implements Requete
{
    // envoyée par le client

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


