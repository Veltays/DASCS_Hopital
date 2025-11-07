package ProtocoleCAP.Reponse;
import model.entity.Consultation;
import protocol.Reponse;

import java.util.ArrayList;

public class Reponse_SEARCH_CONSULTATIONS implements Reponse{

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
