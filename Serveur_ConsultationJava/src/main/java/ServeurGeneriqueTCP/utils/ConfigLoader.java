package ServeurGeneriqueTCP.utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties = new Properties();

    public ConfigLoader() {
        Dotenv dotenv = Dotenv.load();



        try (InputStream input = getClass().getResourceAsStream("/serveurConsultation.properties")) {
            if (input == null)
                throw new RuntimeException("Fichier properties introuvable !");
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de chargement du fichier properties", e);
        }

        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (value != null && value.contains("${")) {
                value = value
                        .replace("${DB_HOST}", dotenv.get("DB_HOST"))
                        .replace("${DB_PORT}", dotenv.get("DB_PORT"))
                        .replace("${DB_NAME}", dotenv.get("DB_NAME"))
                        .replace("${DB_USER}", dotenv.get("DB_USER"))
                        .replace("${DB_PASS}", dotenv.get("DB_PASS"));
                properties.setProperty(key, value);
            }
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public Connection LoadConnection() throws SQLException {
        String url = "jdbc:mysql://" + get("db.host") + "/" + get("db.name");
        return DriverManager.getConnection(url, get("db.user"), get("db.password"));
    }

}
