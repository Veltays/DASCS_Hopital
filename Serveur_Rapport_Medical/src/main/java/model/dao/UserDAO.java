package model.dao;

import model.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDAO {

    private final ConnectDB connectDB;

    public UserDAO() {
        connectDB = new ConnectDB();
    }

    public UserDAO(ConnectDB mockConnectDB) {
        connectDB = mockConnectDB;
    }

    // ============================================================
    // Charger tous les utilisateurs
    // ============================================================
    public ArrayList<User> loadAll() {
        ArrayList<User> list = new ArrayList<>();
        String sql = "SELECT id, login, password, role FROM users";
        try (Statement stmt = connectDB.getConn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ============================================================
    // Récupérer un utilisateur par login
    // ============================================================
    public User getByLogin(String login) {
        String sql = "SELECT id, login, password, role FROM users WHERE login = ?";
        try (PreparedStatement stmt = connectDB.getConn().prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ============================================================
    // Vérifier si le login existe en base de donnée
    // ============================================================
    public boolean checkLogin(String login) {
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try (PreparedStatement stmt = connectDB.getConn().prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============================================================
    // 🔹 Insérer le mot de passe à la première connexion
    // ============================================================
    public boolean setPasswordForFirstLogin(String login, String password) {
        String sql = "UPDATE users SET password = ? WHERE login = ? AND password IS NULL";
        try (PreparedStatement stmt = connectDB.getConn().prepareStatement(sql)) {
            stmt.setString(1, password);
            stmt.setString(2, login);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("[UserDAO] ✅ Premier mot de passe défini pour " + login);
                return true;
            } else {
                System.out.println("[UserDAO] ⚠️ Aucun utilisateur à activer ou mot de passe déjà défini.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============================================================
    // 🔹 Vérifier ou insérer selon le type d'utilisateur
    // ============================================================
    public boolean checkOrCreateUser(String login, String password, boolean isNewUser) {
        if (isNewUser) {
            // Cas d’un utilisateur déjà existant mais sans mot de passe (première connexion)
            return setPasswordForFirstLogin(login, password);
        } else {
            // Cas normal : vérifie les identifiants
            return checkLogin(login);
        }
    }

    public String getPasswordByLogin(String login) throws SQLException {
        String sql = "SELECT password FROM users WHERE login = ?";
        try (PreparedStatement ps = connectDB.getConn().prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                } else {
                    return null; // login inexistant
                }
            }
        }
    }

}
