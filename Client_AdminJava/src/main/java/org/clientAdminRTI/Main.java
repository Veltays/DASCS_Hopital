package org.clientAdminRTI;

import org.clientAdminRTI.controller.MainController;
import org.clientAdminRTI.model.ClientTCP;
import org.clientAdminRTI.view.MainPage;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainPage view = new MainPage();
            ClientTCP client = new ClientTCP("192.168.135.52", 6768);
            try {
                new MainController(view, client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            view.setVisible(true);
        });
    }
}
