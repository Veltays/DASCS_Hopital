package ServeurGeneriqueTCP.model.viewmodel;


import java.util.Date;

public class PatientSearchVM {

    Integer id;
    String firstname;
    String lastname;
    Date dateNaissance;

    public PatientSearchVM(Integer id, String firstname, String lastname, Date dateNaissance) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateNaissance = dateNaissance;
    }

    public Integer getId() {
        return id;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public Date getDateNaissance() {
        return dateNaissance;
    }



    public void setId(Integer id) {
        this.id = id;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}
