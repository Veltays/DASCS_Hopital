package org.clientAdminRTI.model;

import java.io.*;
import java.net.*;
import java.util.*;

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

        try (Socket socket = new Socket()) {

            socket.connect(new InetSocketAddress(host, port), 1000);
            socket.setSoTimeout(1000);

            try (BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
                 BufferedInputStream in = new BufferedInputStream(socket.getInputStream())) {

                // --- Construction du message ---
                String data = "GETAUDIT";
                String header = String.format("%04d", data.length());

                // Envoi
                out.write(header.getBytes());
                out.write(data.getBytes());
                out.flush();

                System.out.println("[TCP] Envoyé : " + header + data);

                // --- Lecture du header ---
                byte[] headerBuf = in.readNBytes(4);
                if (headerBuf.length != 4) {
                    throw new IOException("Impossible de lire la taille de la réponse");
                }

                int tailleReponse = Integer.parseInt(new String(headerBuf));

                // --- Lecture du payload ---
                byte[] dataBuf = in.readNBytes(tailleReponse);

                if (dataBuf.length != tailleReponse) {
                    System.err.println("[TCP] Réponse tronquée (" + dataBuf.length + "/" + tailleReponse + ")");
                }

                String reponse = new String(dataBuf);
                System.out.println("[TCP] Réponse brute : " + reponse);

                clients = parseClients(reponse);

            } catch (IOException e) {
                System.err.println("[TCP] Erreur lors de l’échange : " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("[TCP] Erreur de connexion : " + e.getMessage());
        }

        return clients;
    }

    /**
     * Parse une chaîne brute en liste de clients connectés.
     */
    private List<ClientConnected> parseClients(String data) {
        List<ClientConnected> list = new ArrayList<>();

        if (data == null || data.isEmpty()) return list;

        String[] clientsRaw = data.split("#");

        for (String entry : clientsRaw) {
            if (entry.trim().isEmpty()) continue;

            String id = "", nom = "", prenom = "", ip = "";

            String[] parts = entry.split(",");

            for (String part : parts) {
                String[] keyValue = part.split(":");

                if (keyValue.length != 2) continue;

                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                switch (key) {
                    case "ID":     id = value; break;
                    case "Nom":    nom = value; break;
                    case "Prenom": prenom = value; break;
                    case "IP":     ip = value; break;
                }
            }

            if (!id.isEmpty() && !ip.isEmpty()) {
                list.add(new ClientConnected(id, nom, prenom, ip));
            }
        }

        return list;
    }
}
