package ServeurGeneriqueTCP.model.dao;
import ServeurGeneriqueTCP.model.entity.User;


import java.sql.*;
import java.util.ArrayList;

public class UserDAO {

    private final ConnectDB connectDB;

    public UserDAO() {
        connectDB = new ConnectDB();
    }

    public UserDAO(ConnectDB mockConnectDB) {
        connectDB = mockConnectDB;
    }

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

    public boolean checkLogin(String login, String password) {
        String sql = "SELECT 1 FROM users WHERE login = ? AND password = ?";
        try (PreparedStatement stmt = connectDB.getConn().prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}