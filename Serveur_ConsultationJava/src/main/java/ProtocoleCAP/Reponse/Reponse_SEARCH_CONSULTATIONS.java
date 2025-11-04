package ProtocoleCAP.Reponse;
import ServeurGeneriqueTCP.model.entity.Consultation;
import ServeurGeneriqueTCP.protocol.Reponse;

import java.util.ArrayList;

public class Reponse_SEARCH_CONSULTATIONS implements Reponse{

    private ArrayList<Consultation> consultationsList;

    public Reponse_SEARCH_CONSULTATIONS(){
        consultationsList = new ArrayList<>();
    }

}
