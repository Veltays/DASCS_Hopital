package ServeurGeneriqueTCP.model.entity;

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
        if (id != null && id <= 0)
            throw new IllegalArgumentException("ID must be positive and non-null.");
        this.id = id;
    }

    public void setLastname(String lastname) {
        if (lastname == null || lastname.isBlank())
            throw new IllegalArgumentException("Lastname cannot be null or empty.");
        this.lastname = lastname.trim();
    }

    public void setFirstname(String firstname) {
        if (firstname == null || firstname.isBlank())
            throw new IllegalArgumentException("Firstname cannot be null or empty.");
        this.firstname = firstname.trim();
    }

    public void setBirthDate(Date birth_date) {
        if (birth_date == null)
            throw new IllegalArgumentException("Birth date cannot be null.");

        Date now = new Date();
        if (birth_date.after(now))
            throw new IllegalArgumentException("Birth date cannot be in the future.");

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
