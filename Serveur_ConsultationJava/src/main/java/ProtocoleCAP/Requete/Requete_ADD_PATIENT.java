package ProtocoleCAP.Requete;

import protocol.RequeteCAP;

public class Requete_ADD_PATIENT extends RequeteCAP {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;

    public Requete_ADD_PATIENT(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Requete_ADD_PATIENT{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

}
