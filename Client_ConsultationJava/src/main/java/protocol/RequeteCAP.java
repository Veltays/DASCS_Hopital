package protocol;

public abstract class RequeteCAP implements Requete {
    private Integer idConnexion;

    @Override
    public Integer getIdConnexion() {
        return idConnexion;
    }

    @Override
    public void setIdConnexion(Integer idConnexion) {
        this.idConnexion = idConnexion;
    }
}
