package ProtocoleMRPS.Reponse;

import protocol.Reponse;

public class Reponse_LOGIN implements Reponse
{
    private boolean valide;

    public Reponse_LOGIN(boolean v)
    {
        valide = v;

    }

    public boolean isValide() {
        return valide;
    }


}
