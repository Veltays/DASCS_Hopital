package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddPatientDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textNom;
    private JTextField textPrenom;
    private JLabel LabelNom;
    private JLabel LabelPrenom;

    public AddPatientDialog() {
        setTitle("➕ Ajouter un patient"); // ✅ Titre clair
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // ✅ Taille et centrage
        setPreferredSize(new Dimension(400, 200));
        pack();
        setLocationRelativeTo(null); // centre la fenêtre sur l’écran

        // Boutons
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // Fermer via la croix
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { onCancel(); }
        });

        // Fermer avec Échap
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    public String getNom() { return textNom.getText().trim(); }
    public String getPrenom() { return textPrenom.getText().trim(); }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddPatientDialog dialog = new AddPatientDialog();
            dialog.setVisible(true);
        });
    }
}