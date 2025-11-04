package ServeurGeneriqueTCP.model.viewmodel;

public class PatientSearchVM {

    Integer id;
    String firstname;
    String lastname;

    public PatientSearchVM(Integer id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;}

    public Integer getId() {
        return id;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
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
}
