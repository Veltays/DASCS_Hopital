package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textPatientID;
    private JTextField textDate;

    public SearchDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        // ✅ Taille et centrage
        setPreferredSize(new Dimension(400, 200));
        pack();
        setLocationRelativeTo(null); // centre la fenêtre sur l’écran


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    ///////////////////////////////////////////
    // GETTERS
    /////////////////////////////////////////////

    public String getIdPatient() { return textPatientID.getText().trim(); }
    public String getDate() { return textDate.getText().trim(); }
    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SearchDialog dialog = new SearchDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
