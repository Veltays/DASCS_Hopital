package protocol;
import java.io.*;
public interface Requete extends Serializable {
    Integer getIdConnexion();
    void setIdConnexion(Integer idConnexion);
}