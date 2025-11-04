package ServeurGeneriqueTCP.model.viewmodel;

public class DoctorSearchVM {

    Integer id;
    Integer specialty_id;
    String lastname;


    public DoctorSearchVM(Integer id, Integer specialty_id, String lastname) {
        this.id = id;
        this.specialty_id = specialty_id;
        this.lastname = lastname;
    }



    public Integer getId() {
        return id;
    }
    public String getLastname() {
        return lastname;
    }
    public Integer getSpecialty_id() {
        return specialty_id;
    }


    public void setSpecialty_id(Integer specialty_id) {
        this.specialty_id = specialty_id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
