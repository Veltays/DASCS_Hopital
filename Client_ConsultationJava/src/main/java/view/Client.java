package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Client extends JFrame {
    // === Composants créés via le Form Designer ===
    private JButton ADDPATIENT;
    private JButton DELETECONSULTATION;
    private JButton UPDATECONSULTATION;
    private JButton ADDConsultation;
    private JButton LOGOUTButton;
    private JTable TableOfConsultation;
    private JPanel contentPane;
    private JButton SEARCHCONSULTATION;
    private WindowCloseListener windowCloseListener;


    private JButton boutonLogin;
    private JButton boutonLogout;
    private JButton boutonSearchConsultation;

    public Client() {
        super("Client Consultation");

        // ---- Liaison du panel principal (généré par le Form Designer) ----
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e)
            {
                if (windowCloseListener != null)
                    windowCloseListener.onWindowClose();
            }
        });
    }

    public void setWindowCloseListener(WindowCloseListener listener){
        this.windowCloseListener = listener;
    }

    public interface WindowCloseListener {
        void onWindowClose();
    }

    // ============================================================
    // 🧩 GETTERS (pour le contrôleur)
    // ============================================================
    public JButton getBoutonLogout() { return LOGOUTButton; }

    public JButton getBoutonAddConsultation() { return ADDConsultation; }
    public JButton getBoutonAddPatient() { return ADDPATIENT; }
    public JButton getBoutonDeleteConsultation() { return DELETECONSULTATION; }
    public JButton getBoutonUpdateConsultation() { return UPDATECONSULTATION; }
    public JTable getTableConsultations() { return TableOfConsultation; }

    // ============================================================
    // Méthode pour ajouter un ActionListener global
    // ============================================================
    public void addListener(ActionListener listener) {
        boutonLogin.addActionListener(listener);
        boutonLogout.addActionListener(listener);
        boutonSearchConsultation.addActionListener(listener);
        ADDPATIENT.addActionListener(listener);
        DELETECONSULTATION.addActionListener(listener);
        UPDATECONSULTATION.addActionListener(listener);
        ADDConsultation.addActionListener(listener);

    }

    // ============================================================
    // Méthode pour actualiser la JTable
    // ============================================================
    public void updateConsultationTable(Object[][] data) {
        String[] colonnes = {"ID", "Date", "Heure", "Patient", "Raison", "Docteur"};
        TableOfConsultation.setModel(new javax.swing.table.DefaultTableModel(data, colonnes));
        TableOfConsultation.revalidate();
        TableOfConsultation.repaint();
    }

    // ============================================================
    // Méthode pour afficher un message utilisateur
    // ============================================================
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
