package ProtocoleCAP.Requete;

import ServeurGeneriqueTCP.protocol.Requete;

public class Requete_LOGIN implements Requete {
    private static final long serialVersionUID = 1L;

    private String login;
    private String password;

    public Requete_LOGIN(String login, String password) {
        setLogin(login);
        setPassword(password);
    }

    public String getLogin() { return login; }
    public String getPassword() { return password; }

    public void setLogin(String login) {
        if (login == null || login.isBlank())
            throw new IllegalArgumentException("Le login ne peut pas être vide ou nul.");
        this.login = login;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide ou nul.");
        this.password = password;
    }

    @Override
    public String toString() {
        return "Requete_LOGIN{" +
                "login='" + login + '\'' +
                ", password='********'}"; // Masqué pour éviter les fuites dans les logs
    }
}
