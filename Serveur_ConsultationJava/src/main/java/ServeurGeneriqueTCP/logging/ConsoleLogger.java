package ServeurGeneriqueTCP.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements Logger {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void Trace(String message) {
        if (message == null || message.isBlank()) {
            System.out.println("[WARN] Message de log vide ou null.");
            return;
        }

        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[" + timestamp + "] " + message.trim());
    }
}