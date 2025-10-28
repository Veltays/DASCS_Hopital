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

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("LIST_CLIENTS#");
            String response = in.readLine();

            if (response != null && !response.isEmpty()) {
                for (String line : response.split("\\|")) {
                    String[] p = line.split(";");
                    if (p.length == 4) {
                        clients.add(new ClientConnected(p[0], p[1], p[2], Integer.parseInt(p[3])));
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("[TCP] Erreur : " + e.getMessage());
        }

        return clients;
    }
}