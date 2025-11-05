package ServeurGeneriqueTCP.logging;

import java.io.BufferedWriter;
import java.time.LocalDateTime;

public class FileLogger implements Logger {
    @Override
    public void Trace(String message) {
        BufferedWriter bw = null;
        try {
            java.io.FileWriter fw = new java.io.FileWriter("server.log", true);
            bw = new BufferedWriter(fw);
            String timestamp = LocalDateTime.now().toString();
            bw.write("[" + timestamp + "] " + message);
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
