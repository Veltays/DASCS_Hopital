package view;

import javax.swing.*;
import java.awt.event.*;

public class AddConsultationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField TextDate;
    private JTextField TextHeure;
    private JTextField TextDuree;
    private JTextField TextNombreCons;
    private JLabel LabelDate;
    private JLabel labelHeure;
    private JLabel LabelDuree;
    private JLabel LabelNombre;

    public AddConsultationDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Ajouter des consultations");
        pack();
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
    }

    // ============================================================
    // Getters pour le contrôleur
    // ============================================================
    public String getDate() { return TextDate.getText().trim(); }
    public String getHeure() { return TextHeure.getText().trim(); }
    public String getDuree() { return TextDuree.getText().trim(); }
    public String getNombre() { return TextNombreCons.getText().trim(); }

    // ============================================================
    // Setters (facultatifs, pour préremplir si besoin)
    // ============================================================
    public void setDate(String date) { TextDate.setText(date); }
    public void setHeure(String heure) { TextHeure.setText(heure); }
    public void setDuree(String duree) { TextDuree.setText(duree); }
    public void setNombre(String nombre) { TextNombreCons.setText(nombre); }

    // ============================================================
    //  Méthodes utilitaires
    // ============================================================
    private void onOK() { dispose(); }
    private void onCancel() { dispose(); }

    /** Ouvre la fenêtre et renvoie true si l'utilisateur a cliqué sur OK. */
    public boolean showDialog() {
        setVisible(true);
        return true;
    }
}
