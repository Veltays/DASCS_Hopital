package ServeurGeneriqueTCP.model.dao;

import ServeurGeneriqueTCP.utils.ConfigLoader;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectDB {
    private static Connection conn = null;
    private static final Logger logger = Logger.getLogger(ConnectDB.class.getName());

    public static Connection getConnection() {
        if (conn == null) {
            try {
                ConfigLoader config = new ConfigLoader();

                String host = config.get("db.host");
                String name = config.get("db.name");
                String user = config.get("db.user");
                String pass = config.get("db.password");

                String url = "jdbc:mysql://" + host + "/" + name + "?useSSL=false&serverTimezone=UTC";

                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, pass);
                logger.info("Connexion BD établie avec succès !");
            } catch (ClassNotFoundException | SQLException e) {
                logger.log(Level.SEVERE, "Erreur de connexion BD", e);
            }
        }
        return conn;
    }

    public Connection getConn() {
        return conn;
    }


    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                logger.info("Connexion BD fermée.");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Erreur lors de la fermeture BD", e);
        }
    }




}