package controller;

import ProtocoleCAP.Requete.*;
import com.sun.source.util.SourcePositions;
import model.ConnectServer;
import protocol.Requete;
import view.MaPage;
import model.ConnectServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLOutput;

public class MainController {

    private final MaPage view;
    private final ConnectServer connectServer;
    public MainController(MaPage view, ConnectServer connectServer) throws IOException
    {
        this.view=view;
        this.connectServer=connectServer;

        view.getBoutonLogin().addActionListener(new Login());
        view.getBoutonLogout().addActionListener(new Logout());
        view.getBoutonAddConsultation().addActionListener(new AddConsultation());
        view.getBoutonAddPatient().addActionListener(new AddPatient());
        view.getBoutonDeleteConsultation().addActionListener(new DeleteConsultation());
        view.getBoutonSearchConsultation().addActionListener(new SearchConsultation());
        view.getBoutonUpdateConsultation().addActionListener(new UpdateConsultation());

    }

    class Login implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********LOGIN***********");
            Scanner sc = new Scanner(System.in);
            System.out.println("Entrer votre nom d'utilisateur : ");
            String nom = sc.nextLine();
            System.out.println("Entrer votre mot de passe : ");
            String mdp = sc.nextLine();
            System.out.println("Envoi de la requête au serveur...");
            Requete requete = new Requete_LOGIN(nom,mdp);
            connectServer.Login(requete);

        }
    }

    class Logout implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********LOGOUT**********");
            System.out.println("Entrer votre login que vs voulez logout????? gurl");
            Scanner sc = new Scanner(System.in);
            String nom = sc.nextLine();
            connectServer.Logout(nom);
        }
    }

    class AddConsultation implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********ADD_CONSULTATION**********");
            Scanner sc = new Scanner(System.in);
            System.out.println("Entrer la date");
            String date = sc.nextLine();
            System.out.println("Entrer l'heure");
            String heure = sc.nextLine();
            System.out.println("Entrer la durée des consultations");
            String duree = sc.nextLine();
            System.out.println("Entrer le nombre de consultations consécutives");
            String nombre = sc.nextLine();

            Requete requete = new Requete_ADD_CONSULTATION(LocalDate.parse(date), heure,duree, Integer.parseInt(nombre));

            System.out.println("[CLIENT] Envoi de la requête au serveur");
            connectServer.AddConsultation(requete);


        }
    }
    class AddPatient implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********ADD_PATIENT**********");
            Scanner sc = new Scanner(System.in);
            System.out.println("Entrer le nom du patient à ajouter");
            String nom = sc.nextLine();
            System.out.println("Entrer le prénom du patient à ajouter");
            String prenom = sc.nextLine();

            System.out.println("[CLIENT] Envoi de la requête au serveur");
            Requete requete = new Requete_ADD_PATIENT(prenom,nom);
            connectServer.AddPatient(requete);

        }

    }

    class DeleteConsultation implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********DELETE_CONSULTATION**********");
            Scanner sc  = new Scanner(System.in);
            System.out.println("Entrer l'id de la consultation à supprimer");
            String id = sc.nextLine();

            System.out.println("[CLIENT] Envoi de la requête au serveur");
            Requete requete = new Requete_DELETE_CONSULTATION(Integer.parseInt(id));
            connectServer.Delete_Consultation(requete);

        }
    }

    class SearchConsultation implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********SEARCH_CONSULTATION**********");
            System.out.println("Entrer l'id du patient à rechercher");
            Scanner sc = new Scanner(System.in);
            String id = sc.nextLine();
            System.out.println("Entrer la date de la consultation");
            String date = sc.nextLine();

            System.out.println("[CLIENT] Envoi de la requête au serveur");
            Requete requete = new Requete_SEARCH_CONSULTATIONS(Integer.parseInt(id),LocalDate.parse(date));
            connectServer.Search_Consultation(requete);
        }
    }

    class UpdateConsultation implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********UPDATE_CONSULTATION**********");
            System.out.println("entrer l'id de la consultation à modifier");
            Scanner sc = new Scanner(System.in);
            String id = sc.nextLine();
            System.out.println("entrer le nouvelle date");
            String nouvelledate = sc.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                date = sdf.parse(nouvelledate);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("entrer la nouvelle heure");
            String heure = sc.nextLine();
            System.out.println("entrer l'id du patient");
            String idpatient = sc.nextLine();
            System.out.println("entrer la nouvelle raison");
            String raison = sc.nextLine();
            System.out.println("[CLIENT] Envoi de la requête au serveur");


            Requete requete= new Requete_UPDATE_CONSULTATION(Integer.parseInt(id),date,
                    heure, Integer.parseInt(idpatient), raison);

            connectServer.Update_Consultation(requete);

        }
    }
}
