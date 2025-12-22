package model.viewmodel;

public class PatientSearchVM {

    private Integer id;
    private String firstname;
    private String lastname;

    // --- Constructeur ---
    public PatientSearchVM(Integer id, String firstname, String lastname) {
        setId(id);
        setFirstname(firstname);
        setLastname(lastname);
    }

    // --- Getters ---
    public Integer getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    // --- Setters avec validations ---
    public void setId(Integer id) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("L'id du patient doit être positif.");
        this.id = id;
    }

    public void setFirstname(String firstname) {
        if (firstname != null && firstname.isBlank())
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        this.firstname = firstname != null ? firstname.trim() : null;
    }

    public void setLastname(String lastname) {
        if (lastname != null && lastname.isBlank())
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        this.lastname = lastname != null ? lastname.trim() : null;
    }

    @Override
    public String toString() {
        return "PatientSearchVM{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
