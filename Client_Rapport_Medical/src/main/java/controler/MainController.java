package controler;

import ProtocoleMRPS.Requete.*;
import model.entity.Report;
import org.bouncycastle.cert.ocsp.Req;
import view.*;
import model.*;
import protocol.*;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.List;

public class MainController {

    private final MainView view;
    private final ConnectServer connectServer;

    public MainController (MainView view, ConnectServer connectServer)
    {
        this.view = view;
        this.connectServer = connectServer;

        // Afficher le login au lancement
        showLoginDialog();

        view.getADDREPORTButton().addActionListener(new AddReportDialog());
        view.getLOGOUTButton().addActionListener(new Logout());
        view.getEDITREPORTButton().addActionListener(new EditReportDialog());
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Requete requete = new Requete_LOGOUT();
                connectServer.Logout(requete);
                System.exit(0);
            }
        });
    }

    //////////////////////////////////////////////
    ////////////////// login ///////////////////
    /////////////////////////////////////////////

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

                if (connectServer.LoginRequest(requete, username, password)) {
                    System.out.println("[CLIENT] Connexion réussie ");
                    // quand le login est réussi on affiche directement les rapports du médecin

                    loadReports();

                    dialog.dispose();
                    view.setVisible(true);

                } else {
                    dialog.showError("Identifiants incorrects ");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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

    //////////////////////////////////////////////
    ////////////////// logout ///////////////////
    /////////////////////////////////////////////

    class Logout implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e)
        {
            System.out.println("[CLIENT] Traitement de la requête LOGOUT");

            Requete requete = new Requete_LOGOUT();

            connectServer.Logout(requete);
            view.setVisible(false);
            showLoginDialog();
        }
    }

    //////////////////////////////////////////////
    ///////////////// add report ////////////////
    /////////////////////////////////////////////

    class AddReportDialog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("*** Ajout d'un rapport ***");

            ADD_REPORT dialog = new ADD_REPORT();
            dialog.setVisible(true);

            // récupérer les données
            String id = dialog.getIdReport();
            String idPatient = dialog.getIdPatient();
            String date = dialog.getDateReport();
            String description = dialog.getDescription();

            if (id.isEmpty() || idPatient.isEmpty() || date.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Tous les champs doivent être remplis!",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // récupérer la clé de session depuis ConnectServer
            SecretKey sessionKey = connectServer.getSessionKey();
            if (sessionKey == null) {
                JOptionPane.showMessageDialog(null,
                        "Clé de session absente (problème de login/handshake).",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("[CLIENT] Envoi de la requête au serveur...");
            System.out.println("[CLIENT DEBUG] id=" + id + ", idPatient=" + idPatient);

            Requete requete;
            try {
                requete = new Requete_ADD_REPORT(
                        Integer.parseInt(id),
                        Integer.parseInt(idPatient),
                        LocalDate.parse(date),
                        description,
                        sessionKey
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erreur de chiffrement : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (connectServer.AddReport(requete)) {
                JOptionPane.showMessageDialog(null,
                        "Rapport ajouté avec succès.",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Échec de l'ajout du rapport.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //////////////////////////////////////////////
    ///////////////// edit report ////////////////
    /////////////////////////////////////////////

    class EditReportDialog implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("******** Edit Report*************");


        }
    }

    //////////////////////////////////////////////
    ///////////////// list report ////////////////
    /////////////////////////////////////////////

    private void loadReports() throws Exception {
        System.out.println("**** REQUETE LIST REPORT *****");

        List<Report> reports = connectServer.ListReport();
        DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
        model.setRowCount(0);  // vider la table

        for (Report r : reports) {
            model.addRow(new Object[]{
                    r.getId(),
                    r.getIdPatient(),
                    r.getDate(),
                    r.getDescription()
            });
        }
    }
}
