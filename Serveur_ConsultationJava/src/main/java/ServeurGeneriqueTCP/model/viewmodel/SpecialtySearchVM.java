package ServeurGeneriqueTCP.model.viewmodel;

public class SpecialtySearchVM {

    Integer id;
    String name;

    public SpecialtySearchVM(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setId(Integer id) {
        this.id = id;
    }


}
