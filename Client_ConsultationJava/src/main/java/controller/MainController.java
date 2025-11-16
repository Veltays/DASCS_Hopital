package controller;

import ProtocoleCAP.Reponse.Reponse_LOGIN;
import ProtocoleCAP.Requete.*;
import model.ConnectServer;
import model.entity.Consultation;
import protocol.Requete;
import view.*;

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
        view.getBoutonSearchConsultation().addActionListener(new SearchConsultation());

        view.setWindowCloseListener(() -> {
            try {
                Requete requete = new Requete_LOGOUT();
                connectServer.Logout(requete);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            view.dispose();
        });
    }



    // ============================================================
    // LOGIN (fenêtre de connexion Swing)
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
                if(connectServer.AddConsultation(requete))
                {
                    JOptionPane.showMessageDialog(null, "Consultations ajoutées avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    // Rafraîchir la table après ajout
                    LoadAllConsultations();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Échec de l'ajout des consultations.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

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
                Integer Idpatient = connectServer.AddPatient(requete);
                if(Idpatient != -1 )
                {
                    JOptionPane.showMessageDialog(null, "Patient" + Idpatient+ "ajouté avec succès. ", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Échec de l'ajout du patient.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
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

            SearchDialog dialog = new SearchDialog();
            dialog.setVisible(true);

            String patientID = dialog.getIdPatient();
            String date = dialog.getDate();

            try {
                Integer idPatient = (patientID != null && !patientID.isEmpty()) ? Integer.parseInt(patientID) : null;
                LocalDate localDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;

                Requete requete = new Requete_SEARCH_CONSULTATIONS(idPatient, localDate);
                List<Consultation> consultations = connectServer.SearchConsultation(requete);

                if (consultations.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Aucune consultation trouvée.", "Résultat", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    loadConsultationsInJTable(consultations);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }



    // ============================================================
    // DELETE_CONSULTATION
    // ============================================================
    class DeleteConsultation implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("**********DELETE_CONSULTATION**********");

            int[] selectedRows = view.getTableConsultations().getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(null,
                        "Veuillez sélectionner au moins une consultation à supprimer.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Supprimer " + selectedRows.length + " consultation(s) ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            for (int row : selectedRows) {
                Object value = view.getTableConsultations().getValueAt(row, 0);
                int id = Integer.parseInt(value.toString());

                System.out.println("[CLIENT] Envoi de la requête de suppression pour ID=" + id);
                Requete requete = new Requete_DELETE_CONSULTATION(id);
                connectServer.DeleteConsultation(requete);
            }

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

            int selectedRow = view.getTableConsultations().getSelectedRow();

            Object nvdate = view.getTableConsultations().getValueAt(selectedRow,1);
            Object nvheure = view.getTableConsultations().getValueAt(selectedRow,2);
            Object nvidpatient = view.getTableConsultations().getValueAt(selectedRow,3);
            Object nvraison = view.getTableConsultations().getValueAt(selectedRow,4);

            // ouvre la boite de dialogue
            UpdateConsultationsDialog dialog = new UpdateConsultationsDialog(nvdate.toString(), nvheure.toString(), nvidpatient.toString(), nvraison.toString());
            dialog.setVisible(true);

            //Recuperer les données
            String nouvelleDate = dialog.getDate();
            String nouvelleHeure = dialog.getHeure();
            String idPatient = dialog.getIdPatient();
            String raison = dialog.getRaison();

            selectedRow = view.getTableConsultations().getSelectedRow();
            Object value = view.getTableConsultations().getValueAt(selectedRow, 0);
            int idConsultation = Integer.parseInt(value.toString());




            if(nouvelleDate.isEmpty() || nouvelleHeure.isEmpty() || idPatient.isEmpty() || raison.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {

                System.out.println("[CLIENT] Envoi de la requête au serveur...");
                Requete requete = new Requete_UPDATE_CONSULTATION(idConsultation, LocalDate.parse(nouvelleDate), nouvelleHeure, Integer.parseInt(idPatient), raison);
                if(connectServer.UpdateConsultation(requete))
                {
                    JOptionPane.showMessageDialog(null, "Consultation modifiée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    // Rafraîchir la table après ajout
                    LoadAllConsultations();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Échec de la modification des consultations.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

            }  catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        }
    }



    // ============================================================
    // UTILITAIRE
    // ============================================================

    private void LoadAllConsultations() {
        System.out.println("**********LOAD_ALL_CONSULTATIONS**********");
        Requete requete = new Requete_SEARCH_CONSULTATIONS(null,null);
        List<Consultation> consultations = connectServer.SearchConsultation(requete);

        loadConsultationsInJTable(consultations);
    }



    private void loadConsultationsInJTable(List<Consultation> consultations) {
        Object[][] data = new Object[consultations.size()][7];

        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getDate();
            data[i][2] = c.getHour();
            data[i][3] = c.getPatient_id() != null ? c.getPatient_id() : "Libre";
            data[i][4] = c.getReason() != null ? c.getReason() : "";
            data[i][5] = c.getDoctor_id();
            data[i][6] = c.getDuration();
        }

        view.updateConsultationTable(data);
    }




}
