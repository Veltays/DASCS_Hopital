package ProtocoleCAP.Requete;

import ServeurGeneriqueTCP.protocol.Requete;

public class Requete_LOGOUT implements Requete {
    private static final long serialVersionUID = 1L;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String login;

    public Requete_LOGOUT() {
    }


    @Override
    public String toString() {
        return "Requete_LOGOUT{}";
    }
}
