package model.viewmodel;

public class SpecialtySearchVM {

    private Integer id;
    private String name;

    // --- Constructeur ---
    public SpecialtySearchVM(Integer id, String name) {
        setId(id);
        setName(name);
    }

    // --- Getters ---
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // --- Setters avec validations ---
    public void setId(Integer id) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("L'id de la spécialité doit être positif.");
        this.id = id;
    }

    public void setName(String name) {
        if (name != null && name.isBlank())
            throw new IllegalArgumentException("Le nom de la spécialité ne peut pas être vide.");
        this.name = name != null ? name.trim() : null;
    }

    @Override
    public String toString() {
        return "SpecialtySearchVM{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
