package org.clientAdminRTI.model;

public class ClientConnected {
    private final String ip;
    private final String nom;
    private final String prenom;
    private final int id;

    public ClientConnected(String ip, String nom, String prenom, int id) {
        this.ip = ip;
        this.nom = nom;
        this.prenom = prenom;
        this.id = id;
    }

    public String getIp() { return ip; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public int getId() { return id; }
}
