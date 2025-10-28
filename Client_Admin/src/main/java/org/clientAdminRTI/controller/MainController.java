package org.clientAdminRTI.controller;

import org.clientAdminRTI.model.ClientConnected;
import org.clientAdminRTI.model.ClientTCP;
import org.clientAdminRTI.view.MainPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainController {

    private final MainPage view;
    private final ClientTCP clientTCP;

    public MainController(MainPage view, ClientTCP clientTCP) {
        this.view = view;
        this.clientTCP = clientTCP;

        // Lier le bouton à l’action du contrôleur
        view.addSearchListener(new SearchListener());
    }

    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<ClientConnected> clients = clientTCP.requestClients();
            if (clients.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Aucun client connecté !");
            }
            view.updateTable(clients);
        }
    }
}
