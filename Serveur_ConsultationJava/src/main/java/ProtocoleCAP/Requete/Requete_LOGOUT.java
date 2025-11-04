package ProtocoleCAP.Requete;

import ServeurGeneriqueTCP.protocol.Requete;

public class Requete_LOGOUT implements Requete {
    private String login;
    public Requete_LOGOUT(String l) {
        login = l;
    }
    public String getLogin() {
        return login;
    }
}
