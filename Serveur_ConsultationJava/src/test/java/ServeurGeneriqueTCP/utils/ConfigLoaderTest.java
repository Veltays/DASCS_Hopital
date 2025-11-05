package ServeurGeneriqueTCP.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

    private final ConfigLoader configLoader = new ConfigLoader();

    @ParameterizedTest
    @ValueSource(strings = {
            "PORT_CONSULTATION",
            "TAILLE_POOL",
            "DB.HOST",
            "DB.PORT",
            "DB.NAME",
            "DB.USER",
            "DB.PASSWORD"
    })
    void testConfigKeysNotNullOrEmpty(String key) {
        String value = configLoader.get(key);

        assertNotNull(value, "La valeur de " + key + " ne doit pas être null");
        assertFalse(value.isEmpty(), "La valeur de " + key + " ne doit pas être vide");

        System.out.println("OK" + key + " = " + value);
    }
}