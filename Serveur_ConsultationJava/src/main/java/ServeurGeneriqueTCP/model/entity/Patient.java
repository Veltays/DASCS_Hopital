package ServeurGeneriqueTCP.model.entity;
import java.util.Date;

public class Patient {
    Integer id;
    String lastname;
    String firstname;
    Date birth_date;

    public Patient(Integer id, String lastname, String firstname, Date birth_date) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birth_date = birth_date;
    }

    public Integer getId() {
        return id;
    }
    public String getLastname() {
        return lastname;
    }
    public String getFirstname() {
        return firstname;
    }
    public Date getBirthDate() {
        return birth_date;
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
    public void getBirthDate(Date birth_date) {
        this.birth_date = birth_date;
    }

}
