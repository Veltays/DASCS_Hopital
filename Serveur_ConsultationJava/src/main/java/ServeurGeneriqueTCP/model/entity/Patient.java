package ServeurGeneriqueTCP.model.entity;
import java.util.Date;

public class Patient {
    Integer id;
    String lastname;
    String firstname;
    Date dateNaissance;


    public Integer getId() {
        return id;
    }
    public String getLastname() {
        return lastname;
    }
    public String getFirstname() {
        return firstname;
    }
    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

}
