package protocol;
import java.net.Socket;
import protocol.*;

public interface Protocole
{
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException;
}