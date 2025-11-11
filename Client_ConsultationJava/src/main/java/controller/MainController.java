package controller;

import ProtocoleCAP.Reponse.Reponse_LOGIN;
import ProtocoleCAP.Requete.*;
import model.ConnectServer;
import model.entity.Consultation;
import protocol.Requete;
import view.AddConsultationDialog;
import view.AddPatientDialog;
import view.Client;
import view.LoginDialog;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainController {

    private final Client view;
    private final ConnectServer connectServer;

    public MainController(Client view, ConnectServer connectServer) throws IOException {
        this.view = view;
        this.connectServer = connectServer;

        // Afficher le login au lancement
        showLoginDialog();

        // Ensuite, on attache les listeners de la vue principale
        view.getBoutonLogout().addActionListener(new Logout());
        view.getBoutonAddConsultation().addActionListener(new AddConsultation());
        view.getBoutonAddPatient().addActionListener(new AddPatient());
        view.getBoutonDeleteConsultation().addActionListener(new DeleteConsultation());
        view.getBoutonUpdateConsultation().addActionListener(new UpdateConsultation());

        view.setWindowCloseListener(() -> {
            // Effectuer le logout ici
            Requete requete = new Requete_LOGOUT();
            connectServer.Logout(requete);
            try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            view.dispose();
            System.exit(0);
        });
    }



    // ============================================================
    // 🔐 LOGIN (fenêtre de connexion Swing)
    // ============================================================
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
                Requete requete = new Requete_LOGIN(username, password,NewUser);
                System.out.println("[CLIENT] Envoi de la requête de login...");

                if (connectServer.Login(requete)) {
                    System.out.println("[CLIENT] Connexion réussie ");
                    dialog.dispose();
                    view.setVisible(true);
                    LoadAllConsultations();

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

    private void LoadAllConsultations() {
        System.out.println("**********LOAD_ALL_CONSULTATIONS**********");
        Requete requete = new Requete_SEARCH_CONSULTATIONS(null,null);
        List<Consultation> consultations = connectServer.SearchConsultation(requete);

        Object[][] data = new Object[consultations.size()][6];

        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getDate();
            data[i][2] = c.getHour();
            data[i][3] = c.getPatient_id() != null ? c.getPatient_id() : "Libre";
            data[i][4] = c.getReason() != null ? c.getReason() : "";
            data[i][5] = c.getDoctor_id();
        }

        // Appel de la vue
        view.updateConsultationTable(data);
    }


    // ============================================================
    // LOGOUT
    // ============================================================
    class Logout implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("**********LOGOUT**********");
            Requete requete = new Requete_LOGOUT();

            connectServer.Logout(requete);
            view.setVisible(false);
            showLoginDialog();
        }
    }


    // ============================================================
    // ADD_CONSULTATION
    // ============================================================
    class AddConsultation implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("**********ADD_CONSULTATION**********");

            // Ouvre la boîte de dialogue Swing
            AddConsultationDialog dialog = new AddConsultationDialog();
            dialog.setVisible(true);


            // Récupère les données saisies
            String date = dialog.getDate();
            String heure = dialog.getHeure();
            String duree = dialog.getDuree();
            String nombreStr = dialog.getNombre();

            if (date.isEmpty() || heure.isEmpty() || duree.isEmpty() || nombreStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int nombre = Integer.parseInt(nombreStr);

                System.out.println("[CLIENT] Envoi de la requête au serveur...");
                Requete requete = new Requete_ADD_CONSULTATION(LocalDate.parse(date), heure, duree, nombre);
                connectServer.AddConsultation(requete);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Le nombre de consultations doit être un entier valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }





    // ============================================================
    // ADD_PATIENT
    // ============================================================
    class AddPatient implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("**********ADD_PATIENT**********");

            AddPatientDialog dialog = new AddPatientDialog();
            dialog.setVisible(true);

            // Récupérer les données saisies
            String nom = dialog.getNom();
            String prenom = dialog.getPrenom();

            if( nom.isEmpty() || prenom.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try
            {
                System.out.println("[CLIENT] Envoi de la requête au serveur...");
                Requete requete = new Requete_ADD_PATIENT(prenom, nom);
                connectServer.AddPatient(requete);
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        }
    }


    // ============================================================
    // SEARCH_CONSULTATION
    // ============================================================
    class SearchConsultation implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("**********SEARCH_CONSULTATION**********");
            System.out.println("Entrer l'id du patient à rechercher");
            Scanner sc = new Scanner(System.in);
            String id = sc.nextLine();
            System.out.println("Entrer la date de la consultation");
            String date = sc.nextLine();

            System.out.println("[CLIENT] Envoi de la requête au serveur...");
            Requete requete = new Requete_SEARCH_CONSULTATIONS(Integer.parseInt(id), LocalDate.parse(date));
            connectServer.SearchConsultation(requete);
        }
    }

    // ============================================================
    // DELETE_CONSULTATION
    // ============================================================
    class DeleteConsultation implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            System.out.println("**********DELETE_CONSULTATION**********");
            int selectedRow = view.getTableConsultations().getSelectedRow();
            Object value = view.getTableConsultations().getValueAt(selectedRow, 0);
            int id = Integer.parseInt(value.toString());

            System.out.println("[CLIENT] Envoi de la requête au serveur...");
            Requete requete = new Requete_DELETE_CONSULTATION(id);
            connectServer.DeleteConsultation(requete);

            // Rafraîchir la table après suppression
            LoadAllConsultations();
        }
    }




    // ============================================================
    // UPDATE_CONSULTATION
    // ============================================================
    class UpdateConsultation implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("**********UPDATE_CONSULTATION**********");
            Scanner sc = new Scanner(System.in);
            System.out.println("Entrer l'id de la consultation à modifier");
            String id = sc.nextLine();
            System.out.println("Entrer la nouvelle date (format : yyyy-MM-dd)");
            String nouvelleDate = sc.nextLine();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(nouvelleDate);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("Entrer la nouvelle heure (HH:mm)");
            String heure = sc.nextLine();
            System.out.println("Entrer l'id du patient");
            String idPatient = sc.nextLine();
            System.out.println("Entrer la nouvelle raison");
            String raison = sc.nextLine();

            System.out.println("[CLIENT] Envoi de la requête au serveur...");
            Requete requete = new Requete_UPDATE_CONSULTATION(
                    Integer.parseInt(id),
                    date,
                    heure,
                    Integer.parseInt(idPatient),
                    raison
            );

            connectServer.UpdateConsultation(requete);
        }
    }


}
