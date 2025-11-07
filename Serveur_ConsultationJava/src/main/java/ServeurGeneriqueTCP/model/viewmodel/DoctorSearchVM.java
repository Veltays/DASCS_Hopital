package ServeurGeneriqueTCP.model.viewmodel;

public class DoctorSearchVM {

    private Integer id;
    private Integer specialty_id;
    private String lastname;
    private Integer user_id;

    // --- Constructeur ---
    public DoctorSearchVM(Integer id, Integer specialty_id, String lastname) {
        setId(id);
        setSpecialty_id(specialty_id);
        setLastname(lastname);
    }

    public DoctorSearchVM() {

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
    public Integer getUser_id() {
        return null;
    }

    // --- Setters avec validations ---
    public void setId(Integer id) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("L'id du docteur doit être positif.");
        this.id = id;
    }

    public void setSpecialty_id(Integer specialty_id) {
        if (specialty_id != null && specialty_id <= 0)
            throw new IllegalArgumentException("L'id de la spécialité doit être positif.");
        this.specialty_id = specialty_id;
    }

    public void setLastname(String lastname) {
        if (lastname != null && lastname.isBlank())
            throw new IllegalArgumentException("Le nom de famille ne peut pas être vide.");
        this.lastname = lastname != null ? lastname.trim() : null;
    }
    public  void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "DoctorSearchVM{" +
                "id=" + id +
                ", specialty_id=" + specialty_id +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
