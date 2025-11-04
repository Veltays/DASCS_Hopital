package ProtocoleCAP.Requete;
import ServeurGeneriqueTCP.protocol.Requete;
public class Requete_ADD_PATIENT implements Requete {

    String FirstName;
    String LastName;

    public Requete_ADD_PATIENT(String firstName, String lastName) {
        FirstName = firstName;
        LastName = lastName;
    }


    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}



