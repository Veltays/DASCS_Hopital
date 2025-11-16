package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UpdateConsultationsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField TextDate;
    private JTextField TextHeure;
    private JLabel LabelDate;
    private JLabel Labelheure;
    private JLabel LabelPatient;
    private JLabel LabelRaison;
    private JTextField TextIdPatient;
    private JTextField TextRaison;

    // 👉 Constructeur par défaut (nécessaire pour le main et le FormDesigner)
    public UpdateConsultationsDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setPreferredSize(new Dimension(400, 200));
        pack();
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { onCancel(); }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // 👉 Constructeur pour pré-remplir les champs
    public UpdateConsultationsDialog(String date, String heure, String patient, String raison) {
        this(); // on appelle le constructeur par défaut !

        TextDate.setText(date);
        TextHeure.setText(heure);
        TextIdPatient.setText(patient);
        TextRaison.setText(raison);
    }

    // ============================================================
    // Getters pour le contrôleur
    // ============================================================
    public String getDate() { return TextDate.getText().trim(); }
    public String getHeure() { return TextHeure.getText().trim(); }
    public String getIdPatient() { return TextIdPatient.getText().trim(); }
    public String getRaison() { return TextRaison.getText().trim(); }

    private void onOK() { dispose(); }
    private void onCancel() { dispose(); }

    public static void main(String[] args) {
        UpdateConsultationsDialog dialog = new UpdateConsultationsDialog(); // OK maintenant
        dialog.setVisible(true);
        System.exit(0);
    }
}
