package protocol;

import java.io.Serializable;

public interface Requete extends Serializable {

    Integer getIdConnexion();

    void setIdConnexion(Integer idConnexion);
}
