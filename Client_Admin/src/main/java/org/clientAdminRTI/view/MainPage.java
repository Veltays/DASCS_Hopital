package org.clientAdminRTI.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import org.clientAdminRTI.model.ClientConnected;

public class MainPage extends JFrame {
    private JPanel mainPanel;
    private JTable tableClients;
    private JButton rechercherButton;
    private DefaultTableModel model;

    public MainPage() {
        super("Consultation Admin");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = (JPanel) getContentPane();
        panel.setLayout(new BorderLayout());

        rechercherButton = new JButton("Récupérer les clients connectés");
        panel.add(rechercherButton, BorderLayout.NORTH);

        String[] columns = {"Adresse IP", "Nom", "Prénom", "ID Patient"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void addSearchListener(ActionListener listener) {
        rechercherButton.addActionListener(listener);
    }

    public void updateTable(List<ClientConnected> clients) {
        model.setRowCount(0);
        for (ClientConnected c : clients) {
            model.addRow(new Object[]{c.getIp(), c.getNom(), c.getPrenom(), c.getId()});
        }
    }
}