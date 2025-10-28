package org.clientAdminRTI;

import org.clientAdminRTI.controller.MainController;
import org.clientAdminRTI.model.ClientTCP;
import org.clientAdminRTI.view.MainPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainPage view = new MainPage();
            ClientTCP client = new ClientTCP("192.168.0.43", 6768);
            new MainController(view, client);
            view.setVisible(true);
        });
    }
}
