package ProtocoleMRPS.Reponse;

public class Reponse_EDIT_REPORT {

    private static final long serialVersionUID = 1L;

    private boolean isValid;

    public Reponse_EDIT_REPORT(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
