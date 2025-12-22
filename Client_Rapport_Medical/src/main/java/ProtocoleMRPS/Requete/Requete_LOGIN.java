package ProtocoleMRPS.Requete;

import protocol.Requete;

public class Requete_LOGIN implements Requete
{
    private String username;
    private String password;
    private boolean NewUser;

    public Requete_LOGIN(String username, String password, Boolean newUser)
    {
        this.username=username;
        this.password=password;
        this.NewUser = newUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNewUser() {
        return NewUser;
    }

    public void setNewUser(boolean newUser) {
        NewUser = newUser;
    }

}
