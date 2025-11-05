package ServeurGeneriqueTCP.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileLoggerTest {

    private final Path logFile = Path.of("server.log");

    @BeforeEach
    void cleanLogFile() throws IOException {
        Files.deleteIfExists(logFile);
    }

    @Test
    void trace_ShouldWriteMessageToFile() throws IOException {
        FileLogger logger = new FileLogger();
        logger.Trace("Test log message");

        assertTrue(Files.exists(logFile));

        List<String> lines = Files.readAllLines(logFile);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Test log message"));
        assertTrue(lines.get(0).matches("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\].*"));
    }

    @Test
    void trace_ShouldWriteWarning_WhenMessageIsNull() throws IOException {
        FileLogger logger = new FileLogger();
        logger.Trace(null);

        List<String> lines = Files.readAllLines(logFile);
        assertTrue(lines.get(0).contains("WARN"));
    }

    @Test
    void trace_ShouldWriteWarning_WhenMessageIsBlank() throws IOException {
        FileLogger logger = new FileLogger();
        logger.Trace("   ");

        List<String> lines = Files.readAllLines(logFile);
        assertTrue(lines.get(0).contains("WARN"));
    }

    @Test
    void trace_ShouldAppendMessages() throws IOException {
        FileLogger logger = new FileLogger();
        logger.Trace("First message");
        logger.Trace("Second message");

        List<String> lines = Files.readAllLines(logFile);
        assertEquals(2, lines.size());
        assertTrue(lines.get(1).contains("Second message"));
    }
}
