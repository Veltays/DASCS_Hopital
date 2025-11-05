package ServeurGeneriqueTCP.model.entity;

public class Doctor implements Entity {

    private Integer id;
    private Integer specialty_id;
    private String lastname;
    private String firstname;

    public Doctor(Integer id, Integer specialty_id, String lastname, String firstname) {
        setId(id);
        setSpecialty_id(specialty_id);
        setLastname(lastname);
        setFirstname(firstname);
    }

    // --- Getters ---
    public Integer getId() {
        return id;
    }

    public Integer getSpecialty_id() {
        return specialty_id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    // --- Setters avec vérifications ---
    public void setId(Integer id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID must be positive and non-null.");
        this.id = id;
    }

    public void setSpecialty_id(Integer specialty_id) {
        if (specialty_id == null || specialty_id <= 0)
            throw new IllegalArgumentException("Specialty ID must be positive and non-null.");
        this.specialty_id = specialty_id;
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

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", specialty_id=" + specialty_id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                '}';
    }
}
