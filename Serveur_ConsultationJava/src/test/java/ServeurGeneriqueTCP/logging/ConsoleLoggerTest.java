package ServeurGeneriqueTCP.logging;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleLoggerTest {

    @Test
    void trace_ShouldPrintMessageWithTimestamp() {
        // Capture de la sortie console
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        ConsoleLogger logger = new ConsoleLogger();
        logger.Trace("Test message");

        String logOutput = output.toString().trim();

        assertTrue(logOutput.contains("Test message"));
        assertTrue(logOutput.matches("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\].*"));

        // Restaure la console
        System.setOut(System.out);
    }

    @Test
    void trace_ShouldWarnWhenMessageIsNull() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        ConsoleLogger logger = new ConsoleLogger();
        logger.Trace(null);

        String logOutput = output.toString().trim();
        assertTrue(logOutput.contains("WARN"));
        assertTrue(logOutput.contains("vide ou null"));

        System.setOut(System.out);
    }

    @Test
    void trace_ShouldWarnWhenMessageIsBlank() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        ConsoleLogger logger = new ConsoleLogger();
        logger.Trace("   ");

        String logOutput = output.toString().trim();
        assertTrue(logOutput.contains("WARN"));
        System.setOut(System.out);
    }
}