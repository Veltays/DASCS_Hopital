package org.clientAdminRTI.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientAdminPage extends JFrame {

    private JPanel mainPanel;
    private JTable tableClients;
    private JButton rechercherButton;
    private DefaultTableModel model;

    public ClientAdminPage() {
        super("Consultation Admin");

        // ======== Configuration de la fenêtre ========
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // ======== Création du panel principal ========
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // ======== Bouton rechercher ========
        rechercherButton = new JButton("Récupérer les clients connectés");
        mainPanel.add(rechercherButton, BorderLayout.NORTH);

        // ======== Table + modèle ========
        String[] colonnes = {"Adresse IP", "Nom", "Prénom", "ID Patient"};
        model = new DefaultTableModel(colonnes, 0);
        tableClients = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tableClients);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ======== Action du bouton ========
        rechercherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Simulation de récupération des clients
                model.setRowCount(0); // vide la table avant d’ajouter les nouvelles lignes
                model.addRow(new Object[]{"192.168.0.12", "Dupont", "Jean", "101"});
                model.addRow(new Object[]{"192.168.0.15", "Martin", "Sophie", "102"});
                model.addRow(new Object[]{"192.168.0.17", "Durand", "Paul", "103"});
                JOptionPane.showMessageDialog(ClientAdminPage.this, "Liste mise à jour !");
            }
        });
    }

}
