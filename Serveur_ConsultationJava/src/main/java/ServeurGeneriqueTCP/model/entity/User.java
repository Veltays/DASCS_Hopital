package ServeurGeneriqueTCP.model.entity;

public class User implements Entity {

    private Integer id;
    private String login;
    private String password;
    private String role;

    public User() {}

    public User(Integer id, String login, String password, String role) {
        setId(id);
        setLogin(login);
        setPassword(password);
        setRole(role);
    }

    // --- Getters ---
    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // --- Setters avec validations ---
    public void setId(Integer id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID must be positive and non-null.");
        this.id = id;
    }

    public void setLogin(String login) {
        if (login == null || login.isBlank())
            throw new IllegalArgumentException("Login cannot be null or empty.");
        this.login = login.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be null or empty.");
        if (password.length() < 6)
            throw new IllegalArgumentException("Password must contain at least 6 characters.");
        this.password = password;
    }

    public void setRole(String role) {
        if (role == null || role.isBlank())
            throw new IllegalArgumentException("Role cannot be null or empty.");

        role = role.trim().toLowerCase();
        if (!role.equals("admin") && !role.equals("doctor") && !role.equals("patient"))
            throw new IllegalArgumentException("Role must be one of: admin, doctor, patient.");
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
