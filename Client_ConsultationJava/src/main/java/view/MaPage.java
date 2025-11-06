package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MaPage extends JFrame {

    private JButton boutonAddConsultation;
    private JButton boutonAddPatient;
    private JButton boutonDeleteConsultation;
    private JButton boutonLogin;
    private JButton boutonLogout;
    private JButton boutonSearchConsultation;
    private JButton boutonUpdateConsultation;

    public MaPage() {
        super("Client Consultation");

        // --- Création des boutons ---
        boutonAddConsultation = new JButton("Add Consultation");
        boutonAddPatient = new JButton("Add Patient");
        boutonDeleteConsultation = new JButton("Delete Consultation");
        boutonLogin = new JButton("Login");
        boutonLogout = new JButton("Logout");
        boutonSearchConsultation = new JButton("Search Consultation");
        boutonUpdateConsultation = new JButton("Update Consultation");

        // --- Création du panneau principal ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        // FlowLayout → les boutons s'alignent automatiquement à l’horizontale
        // 10 px d’espacement horizontal, 20 px vertical

        // --- Ajout des boutons dans le panneau ---
        mainPanel.add(boutonLogin);
        mainPanel.add(boutonLogout);
        mainPanel.add(boutonAddConsultation);
        mainPanel.add(boutonAddPatient);
        mainPanel.add(boutonSearchConsultation);
        mainPanel.add(boutonDeleteConsultation);
        mainPanel.add(boutonUpdateConsultation);


        // --- Paramètres de la fenêtre ---
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // ajuste automatiquement la taille de la fenêtre
        setLocationRelativeTo(null); // centre la fenêtre à l’écran
        setVisible(true);

    }

    public JButton getBoutonLogin() { return boutonLogin;}

    public JButton getBoutonAddConsultation() {return boutonAddConsultation;
    }

    public JButton getBoutonAddPatient() {return boutonAddPatient;
    }

    public JButton getBoutonDeleteConsultation() {return boutonDeleteConsultation;
    }

    public JButton getBoutonLogout() {return boutonLogout;
    }

    public JButton getBoutonSearchConsultation() {return boutonSearchConsultation;
    }

    public JButton getBoutonUpdateConsultation() {return boutonUpdateConsultation;
    }

    public void addListener(ActionListener listener)
    {
        boutonLogin.addActionListener(listener);
        boutonLogout.addActionListener(listener);
        boutonAddConsultation.addActionListener(listener);
        boutonAddPatient.addActionListener(listener);
        boutonDeleteConsultation.addActionListener(listener);
        boutonSearchConsultation.addActionListener(listener);
        boutonUpdateConsultation.addActionListener(listener);
    }
}
