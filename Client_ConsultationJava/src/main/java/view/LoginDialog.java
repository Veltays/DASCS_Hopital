package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField FieldLogin;
    private JPasswordField FieldPassword;
    private JRadioButton NewUser;

    public LoginDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Connexion au serveur");
        pack();
        setLocationRelativeTo(null);
    }

    // --- Getters pour les champs ---
    public String getLogin() {
        return FieldLogin.getText().trim();
    }

    public String getPassword() {
        return new String(FieldPassword.getPassword());
    }

    public boolean isNewUser() {
        return NewUser.isSelected();
    }

    public void onLogin(ActionListener listener) {
        buttonOK.addActionListener(listener);
    }

    public void onCancel(ActionListener listener) {
        buttonCancel.addActionListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}