package ServeurGeneriqueTCP.model.entity;

public class Doctor {
    Integer id;
    Integer specialty_id;
    String lastname;
    String firstname;



    public Integer getId() {
        return id;
    }
    public int getSpecialty_id() {
        return specialty_id;
    }
    public String getLastname() {
        return lastname;
    }
    public String getFirstname() {
        return firstname;
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
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


}
