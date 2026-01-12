package model.entity;

public class Specialty implements Entity {

    private Integer id;
    private String name;

    public Specialty(Integer id, String name) {
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

    // --- Setters avec vérifications ---
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Specialty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
