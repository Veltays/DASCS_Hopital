package ProtocoleMRPS.Reponse;
import protocol.Reponse;


public class Reponse_ERROR implements Reponse {
    private final String message;

    public Reponse_ERROR(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }

    @Override
    public String toString() {
        return "Reponse_ERROR{" + "message='" + message + '\'' + '}';
    }
}