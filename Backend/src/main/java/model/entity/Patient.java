package model.entity;

import java.util.Date;

public class Patient implements Entity {

    private Integer id;
    private String lastname;
    private String firstname;
    private Date birth_date;

    public Patient(Integer id, String lastname, String firstname, Date birth_date) {
        setId(id);
        setLastname(lastname);
        setFirstname(firstname);
        setBirthDate(birth_date);
    }

    public Patient() {

    }

    // --- Getters ---
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

    // --- Setters avec vérifications ---
    public void setId(Integer id) {
        this.id = id;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname.trim();
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname.trim();
    }

    public void setBirthDate(Date birth_date) {
        this.birth_date = birth_date;
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", birth_date=" + birth_date +
                '}';
    }
}
