package ProtocoleCAP.Reponse;
import model.entity.Consultation;
import ServeurGeneriqueTCP.protocol.Reponse;

import java.util.ArrayList;

public class Reponse_SEARCH_CONSULTATIONS implements Reponse{
    private static final long serialVersionUID = 1L;



    private ArrayList<Consultation> consultationsList;

    public Reponse_SEARCH_CONSULTATIONS(){
        consultationsList = new ArrayList<>();
    }

    public void addConsultation(Consultation c){
        consultationsList.add(c);
    }
    public ArrayList<Consultation> getConsultationsList() {
        return consultationsList;
    }

}
