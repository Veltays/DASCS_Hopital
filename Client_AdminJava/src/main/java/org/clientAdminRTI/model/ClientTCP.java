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

    public List<ClientConnected> requestClients() {
        List<ClientConnected> clients = new ArrayList<>();

        try (Socket socket = new Socket(host, port)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            // --- Envoi de la commande ---
            String request = "LIST_CLIENTS";
            String header = String.format("%04d", request.length());
            dos.write((header + request).getBytes());
            dos.flush();

            // --- Lecture de la réponse ---
            // Lire d’abord les 4 premiers caractères = longueur
            byte[] headerBytes = new byte[4];
            dis.readFully(headerBytes);
            int length = Integer.parseInt(new String(headerBytes));

            // Lire ensuite les N bytes du message
            byte[] dataBytes = new byte[length];
            dis.readFully(dataBytes);
            String response = new String(dataBytes);

            // --- Décodage ---
            for (String line : response.split("\\|")) {
                String[] p = line.split("#");
                if (p.length == 4)
                    clients.add(new ClientConnected(p[0], p[1], p[2], Integer.parseInt(p[3])));
            }

        } catch (IOException e) {
            System.err.println("[TCP] Erreur : " + e.getMessage());
        }

        return clients;
    }
}