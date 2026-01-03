package model.entity;

public class Doctor implements Entity {

    private Integer id;
    private Integer specialty_id;
    private String lastname;
    private String firstname;
    private Integer user_id;

    public Doctor(Integer id, Integer specialty_id, String lastname, String firstname, int userId) {
        setId(id);
        setSpecialty_id(specialty_id);
        setLastname(lastname);
        setFirstname(firstname);
        setUser_id(userId);
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


    public Integer getUser_id() {
        return user_id;
    }


    // --- Setters avec vérifications ---
    public void setId(Integer id) {
        this.id = id;
    }

    public void setSpecialty_id(Integer specialty_id) {
        this.specialty_id = specialty_id;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname.trim();
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname.trim();
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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
