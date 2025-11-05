package ServeurGeneriqueTCP.model.viewmodel;

public class UserSearchVM {
    private String login;
    private String role;

    // --- Constructeurs ---
    public UserSearchVM() {}

    public UserSearchVM(String login, String role) {
        setLogin(login);
        setRole(role);
    }

    // --- Getters ---
    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }

    // --- Setters avec validations ---
    public void setLogin(String login) {
        if (login != null && login.isBlank())
            throw new IllegalArgumentException("Le login ne peut pas être vide.");
        this.login = login != null ? login.trim() : null;
    }

    public void setRole(String role) {
        if (role != null && role.isBlank())
            throw new IllegalArgumentException("Le rôle ne peut pas être vide.");

        if (role != null) {
            role = role.trim().toLowerCase();
            if (!role.equals("admin") && !role.equals("doctor") && !role.equals("patient"))
                throw new IllegalArgumentException("Le rôle doit être 'admin', 'doctor' ou 'patient'.");
        }

        this.role = role;
    }

    @Override
    public String toString() {
        return "UserSearchVM{" +
                "login='" + login + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
