package ProtocoleCAP.Requete;
import ServeurGeneriqueTCP.protocol.Requete;

public class Requete_LOGIN implements Requete
{
    private String login;
    private String password;
    public Requete_LOGIN(String l, String p) {
        login = l;
        password = p;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
}