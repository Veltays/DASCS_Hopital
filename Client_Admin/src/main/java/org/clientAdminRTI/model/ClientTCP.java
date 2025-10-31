package org.clientAdminRTI.model;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClientTCP {
    private final String host;
    private final int port;

    public ClientTCP(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Envoie la requête GETAUDIT et renvoie la liste des clients reçus.
     */
    public List<ClientConnected> sendRequeteAudit() {
        List<ClientConnected> clients = new ArrayList<>();

        try (Socket socket = new Socket(host, port);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

            String data = "GETAUDIT";
            int length = data.length();

            // Format à 4 chiffres (ex: 0009)
            String header = String.format("%04d", length);

            // Envoi de la taille + message
            out.write(header.getBytes());
            out.write(data.getBytes());
            out.flush();

            System.out.println("[TCP] Envoyé : " + header + data);

            // Lecture de la réponse (même protocole)
            byte[] tailleBuf = new byte[4];
            if (in.read(tailleBuf) != 4) {
                throw new IOException("Impossible de lire la taille de la réponse");
            }

            int tailleReponse = Integer.parseInt(new String(tailleBuf));
            byte[] dataBuf = new byte[tailleReponse];
            int lus = in.read(dataBuf);
            if (lus != tailleReponse) {
                System.err.println("[TCP] Attention : réponse tronquée (" + lus + "/" + tailleReponse + ")");
            }

            String reponse = new String(dataBuf);
            System.out.println("[TCP] Réponse brute du serveur : " + reponse);

            // 🔍 Parser la réponse en objets
            clients = parseClients(reponse);

        } catch (IOException e) {
            System.err.println("[TCP] Erreur lors de l'envoi de GETAUDIT : " + e.getMessage());
        }

        return clients;
    }

    /**
     * Transforme une chaîne du type :
     * ID:1,Nom:Durand,Prenom:Paul,IP:192.168.1.10#ID:2,...
     * en une liste de ClientConnected.
     */
    private List<ClientConnected> parseClients(String data) {
        List<ClientConnected> list = new ArrayList<>();

        if (data == null || data.isEmpty()) {
            return list;
        }

        // Chaque client est séparé par #
        String[] clientsRaw = data.split("#");
        for (String entry : clientsRaw) {
            if (entry.trim().isEmpty()) continue;

            String id = "", nom = "", prenom = "", ip = "";

            // Chaque attribut est séparé par des virgules
            String[] parts = entry.split(",");
            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    switch (key) {
                        case "ID":
                            id = value;
                            break;
                        case "Nom":
                            nom = value;
                            break;
                        case "Prenom":
                            prenom = value;
                            break;
                        case "IP":
                            ip = value;
                            break;
                    }
                }
            }

            if (!id.isEmpty() && !ip.isEmpty()) {
                list.add(new ClientConnected(id, nom, prenom, ip));
            }
        }

        return list;
    }
}
