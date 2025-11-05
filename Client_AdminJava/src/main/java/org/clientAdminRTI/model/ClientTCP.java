package org.clientAdminRTI.model;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
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

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1000); // Timeout de connexion : 1s
            socket.setSoTimeout(1000); // Timeout de lecture : 1s

            try (OutputStream out = socket.getOutputStream();
                 InputStream in = socket.getInputStream()) {

                // Préparation du message
                String data = "GETAUDIT";
                int length = data.length();

                // Format à 4 chiffres (ex: 0009)
                String header = String.format("%04d", length);

                // Envoi de la taille + message (ex: 0009GETAUDIT)
                out.write(header.getBytes());
                out.write(data.getBytes());
                out.flush();



                System.out.println("[TCP] Envoyé : " + header + data);

                // Lecture de la réponse (même protocole)
                byte[] tailleBuf = new byte[4];
                if (in.read(tailleBuf) != 4) {
                    throw new IOException("Impossible de lire la taille de la réponse");
                }
                System.out.println("[TCP] HEADER LU : " + Arrays.toString(tailleBuf));

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

            } catch (SocketTimeoutException e) {
                System.err.println("[TCP] Timeout atteint : " + e.getMessage());
            } catch (IOException e) {
                System.err.println("[TCP] Erreur d’E/S : " + e.getMessage());
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
