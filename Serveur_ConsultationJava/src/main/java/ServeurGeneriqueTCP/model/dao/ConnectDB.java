package ServeurGeneriqueTCP.model.dao;

import ServeurGeneriqueTCP.utils.ConfigLoader;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectDB {
    private static Connection conn = null;
    private static final Logger logger = Logger.getLogger(ConnectDB.class.getName());

    public ConnectDB() {
        if (conn == null) {
            try {
                ConfigLoader config = new ConfigLoader();

                String host = config.get("DB.HOST");
                String name = config.get("DB.NAME");
                String user = config.get("DB.USER");
                String pass = config.get("DB.PASSWORD");

                String url = "jdbc:mysql://" + host + "/" + name + "?useSSL=false&serverTimezone=UTC";

                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, pass);
                logger.info("Connexion BD établie avec succès !");
            } catch (ClassNotFoundException | SQLException e) {
                logger.log(Level.SEVERE, "Erreur de connexion BD", e);
            }
        }
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