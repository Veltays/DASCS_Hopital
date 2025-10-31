package org.clientAdminRTI.model;

public class ClientConnected {
    private final String id;
    private final String nom;
    private final String prenom;
    private final String ip;

    public ClientConnected(String id, String nom, String prenom, String ip) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.ip = ip;
    }

    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getIp() { return ip; }
}
