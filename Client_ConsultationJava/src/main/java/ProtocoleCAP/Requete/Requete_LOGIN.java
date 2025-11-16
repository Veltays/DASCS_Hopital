package ProtocoleCAP.Requete;

import protocol.RequeteCAP;

public class Requete_LOGIN extends RequeteCAP {
    private static final long serialVersionUID = 1L;
    private String login;
    private String password;
    private boolean NewUser;

    public Requete_LOGIN(String login, String password,boolean NewUser) {
        setLogin(login);
        setPassword(password);
        setNewUser(NewUser);
    }


    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public boolean isNewUser() {
        return NewUser;
    }




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

    public void setNewUser(boolean newUser) {
        NewUser = newUser;
    }



    @Override
    public String toString() {
        return "Requete_LOGIN{" +
                "login='" + login + '\'' +
                ", password='********'}"; // Masqué pour éviter les fuites dans les logs
    }


}
