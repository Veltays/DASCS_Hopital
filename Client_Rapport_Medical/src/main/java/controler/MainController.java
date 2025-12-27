package controler;

import ProtocoleMRPS.Requete.*;
import com.sun.tools.javac.Main;
import view.*;
import model.*;
import protocol.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;


public class MainController {

    private final MainView view;
    private final ConnectServer connectServer;

    public MainController (MainView view, ConnectServer connectServer)
    {
        this.view=view;
        this.connectServer = connectServer;

        //Afficher le login au lancement
        showLoginDialog();

        view.getADDREPORTButton().addActionListener(new AddReportDialog());
    }

    private void showLoginDialog() {
        LoginDialog dialog = new LoginDialog();

        // Action bouton OK
        dialog.onLogin(e -> {
            String username = dialog.getLogin();
            String password = dialog.getPassword();
            Boolean NewUser = dialog.isNewUser();


            if (username.isEmpty() || password.isEmpty()) {
                dialog.showError("Veuillez remplir tous les champs.");
                return;
            }

            try {
                System.out.println("**********LOGIN***********");
                Requete requete = new Requete_LOGIN_REQUEST(username);
                System.out.println("[CLIENT] Envoi de la requête de login...");

                if (connectServer.LoginRequest(requete,username, password)) {
                    System.out.println("[CLIENT] Connexion réussie ");
                    dialog.dispose();
                    view.setVisible(true);

                } else {
                    dialog.showError("Identifiants incorrects ");
                }
            } catch (Exception ex) {
                dialog.showError("Erreur de connexion : " + ex.getMessage());
            }
        });

        // Action bouton Cancel
        dialog.onCancel(e -> {
            dialog.dispose();
            System.exit(0);
        });

        dialog.setVisible(true);
    }


         class AddReportDialog implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("*** Ajout d'un rapport ***");

                //ouvrir la boite de dialogue

                ADD_REPORT dialog = new ADD_REPORT();

                dialog.setVisible(true);

                // récupérer les données
                // + un moyen pour récuperer l'id du patient???
                String id = dialog.getIdReport();
                String idPatient = dialog.getIdPatient();
                String date = dialog.getDateReport();
                String description = dialog.getDescription();

                if(id.isEmpty() || idPatient.isEmpty() || date.isEmpty() || description.isEmpty())
                {
                    JOptionPane.showMessageDialog(null,"Tous les champs doivent être remplis!", "Erreur",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                System.out.println("[CLIENT] Envoi de la requête au serveur...");
                Requete requete = new Requete_ADD_REPORT(Integer.parseInt(id), Integer.parseInt(idPatient), LocalDate.parse(date),description, sessionKey);

                if(connectServer.AddReport(requete))
                {
                    JOptionPane.showMessageDialog(null, "Rapport ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Échec de l'ajout du rapport.", "Erreur", JOptionPane.ERROR_MESSAGE);

                }

            }

        }

}
