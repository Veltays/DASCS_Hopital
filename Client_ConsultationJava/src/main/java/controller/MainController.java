package controller;


import com.sun.source.util.SourcePositions;
import model.ConnectServer;
import view.MaPage;
import model.ConnectServer;
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
            System.out.println("**********LOGIN***********");Scanner sc = new Scanner(System.in);
            System.out.println("Entrer votre nom d'utilisateur : ");
            String nom = sc.nextLine();
            System.out.println("Entrer votre mot de passe : ");
            String mdp = sc.nextLine();
            String requete = "LOGIN#" + nom + "#" + mdp;
            System.out.println("Envoi de la requête " + requete + " au serveur...");

            connectServer.Login(requete);


        }
    }

    class Logout implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********LOGOUT**********");
        }
    }

    class AddConsultation implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********ADD_CONSULTATION**********");
        }
    }
    class AddPatient implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********ADD_PATIENT**********");
        }

    }

    class DeleteConsultation implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********DELETE_CONSULTATION**********");
        }
    }

    class SearchConsultation implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********SEARCH_CONSULTATION**********");
        }
    }

    class UpdateConsultation implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("**********UPDATE_CONSULTATION**********");
        }
    }


}
