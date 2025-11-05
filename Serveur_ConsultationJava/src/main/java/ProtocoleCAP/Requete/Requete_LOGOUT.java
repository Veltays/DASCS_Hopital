package ProtocoleCAP.Requete;

import ServeurGeneriqueTCP.protocol.Requete;

public class Requete_LOGOUT implements Requete {

    private String login;

    public Requete_LOGOUT(String login) {
        setLogin(login);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login == null || login.isBlank())
            throw new IllegalArgumentException("Le login ne peut pas être vide ou nul.");
        this.login = login;
    }

    @Override
    public String toString() {
        return "Requete_LOGOUT{" +
                "login='" + login + '\'' +
                '}';
    }
}
