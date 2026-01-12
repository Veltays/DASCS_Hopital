package model.entity;

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
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login.trim();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
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
