package org.clientAdminRTI;

import org.clientAdminRTI.view.ClientAdminPage;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        SwingUtilities.invokeLater(() -> {
            ClientAdminPage screen = new ClientAdminPage();
            screen.setVisible(true);
        });
    }
}

