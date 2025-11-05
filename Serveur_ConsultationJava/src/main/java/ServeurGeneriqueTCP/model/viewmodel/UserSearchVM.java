package ServeurGeneriqueTCP.model.viewmodel;

public class UserSearchVM {
    private String login;
    private String role;

    public UserSearchVM() {}

    public UserSearchVM(String login, String role) {
        this.login = login;
        this.role = role;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

}
