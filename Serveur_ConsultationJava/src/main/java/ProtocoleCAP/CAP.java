package ProtocoleCAP;

import ProtocoleCAP.Reponse.*;
import ProtocoleCAP.Requete.*;
import ProtocoleCAP.Exception.CAPException;
import ServeurGeneriqueTCP.logging.Logger;
import ServeurGeneriqueTCP.model.dao.*;
import ServeurGeneriqueTCP.model.entity.Consultation;
import ServeurGeneriqueTCP.model.entity.Doctor;
import ServeurGeneriqueTCP.model.entity.Patient;
import ServeurGeneriqueTCP.model.entity.User;
import ServeurGeneriqueTCP.model.viewmodel.*;
import ServeurGeneriqueTCP.protocol.*;

import java.net.Socket;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;

public class CAP implements Protocole {

    private final HashMap<String, Socket> clientsConnectes;
    private final Logger logger;


    private final UserDAO userDAO;
    private final ConsultationDAO consultationDAO;
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;
    private final SpecialtyDAO specialtyDAO;


    public CAP(Logger log) {
        logger = log;
        clientsConnectes = new HashMap<>();

        userDAO = new UserDAO();
        consultationDAO = new ConsultationDAO();
        patientDAO = new PatientDAO();
        doctorDAO = new DoctorDAO();
        specialtyDAO = new SpecialtyDAO();


    }

    @Override
    public String getNom() {
        return "CAP";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        if (requete == null) {
            logger.Trace("Requête reçue est null !");
            return new Reponse_ERROR("Requête vide ou non reconnue.");
        }

        logger.Trace("Traitement d'une requête de type " + requete.getClass().getSimpleName());

        switch (requete) {
            case Requete_LOGIN requeteLogin -> {
                return TraiteRequeteLOGIN(requeteLogin, socket);
            }
            case Requete_ADD_CONSULTATION requeteAddConsultation -> {
                return TraiteRequeteADD_CONSULTATION(requeteAddConsultation, socket);
            }
            case Requete_ADD_PATIENT requeteAddPatient -> {
                return TraiteRequeteADD_PATIENT(requeteAddPatient, socket);
            }
            case Requete_UPDATE_CONSULTATION requeteUpdateConsultation -> {
                return TraiteRequeteUPDATE_CONSULTATION(requeteUpdateConsultation, socket);
            }
            case Requete_SEARCH_CONSULTATIONS requeteSearchConsultations -> {
                return TraiteRequeteSEARCH_CONSULTATIONS(requeteSearchConsultations, socket);
            }
            case Requete_DELETE_CONSULTATION requeteDeleteConsultation -> {
                return TraiteRequeteDELETE_CONSULTATION(requeteDeleteConsultation, socket);
            }
            case Requete_LOGOUT requeteLogout -> {
                TraiteRequeteLOGOUT(requeteLogout);
            }
            default -> {
                logger.Trace("Requête de type inconnu reçue : " + requete.getClass().getName());
                return new Reponse_ERROR("Type de requête non reconnu : " + requete.getClass().getSimpleName());
            }
        }

        return null;
    }






    // ============================================================
    // LOGIN
    // ============================================================
    private synchronized Reponse_LOGIN TraiteRequeteLOGIN(Requete_LOGIN requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());

        // chercher dans le DB user si le login/mot de passe est correct

        String Password = requete.getPassword();
        String UserName =  requete.getLogin();

        if(userDAO.checkLogin(UserName,Password))
        {
            if (clientsConnectes.containsKey(UserName)) {
                logger.Trace("Échec du login pour " + UserName + " : déjà connecté.");
                return new Reponse_LOGIN(false);
            }

            // ajouter à la liste des connectés
            clientsConnectes.put(UserName, socket);
            logger.Trace(UserName + " connecté avec succès.");
        }
        else
        {
            logger.Trace("Échec du login pour " + UserName + " : identifiants incorrects.");
            return new Reponse_LOGIN(false);
        }

        return new Reponse_LOGIN(true);
    }





    // ============================================================
    // ADD_CONSULTATION
    // ============================================================
    private synchronized Reponse_ADD_CONSULTATION TraiteRequeteADD_CONSULTATION(Requete_ADD_CONSULTATION requete, Socket socket)
    {
        try {
            logger.Trace("Ajout d'une consultation par " + socket.getInetAddress());

            // 1 - Récupérer le login
            String login = getLoginBySocket(socket);
            if (login == null) {
                throw new CAPException("Socket non reconnu — utilisateur introuvable.");
            }

            // 2 - Charger l'utilisateur
            User userLogin = userDAO.getByLogin(login);
            if (userLogin == null) {
                throw new CAPException("Utilisateur introuvable pour le login : " + login);
            }
            int userId = userLogin.getId();


            // 3 - Trouver le docteur associé à cet utilisateur
            DoctorSearchVM doctorSearch = new DoctorSearchVM();
            doctorSearch.setUser_id(userId);
            doctorDAO.load(doctorSearch);

            if (doctorDAO.getList().isEmpty()) {
                throw new CAPException("Aucun docteur associé à cet utilisateur (" + login + ")");
            }

            // 4 - Recupérer l'ID du docteur
            Doctor doctor = doctorDAO.getList().get(0);
            Integer idDoctor = doctor.getId();




            // 5 - Boucle de création des consultations
            LocalTime heureDebut = LocalTime.parse(requete.getHeure());
            Duration interval = Duration.ofMinutes(30);

            requete.getHeure();
            for (int i = 0; i < requete.getNombreConsultations(); i++) {
                LocalTime NewHeure = heureDebut.plusMinutes(i * interval.toMinutes());

                if(NewHeure.isAfter(LocalTime.parse("17:00")))
                {
                    throw new CAPException("Heure de consultation dépasse la limite autorisée (17:00).");
                }


                Consultation newConsultation = new Consultation();
                newConsultation.setDoctor_id(idDoctor);
                newConsultation.setPatient_id(null);
                newConsultation.setHour(NewHeure.toString());
                newConsultation.setDate(requete.getDate());

                consultationDAO.save(newConsultation);
            }


            logger.Trace("✅ Consultations créées avec succès pour le docteur " + doctor.getLastname() + doctor.getFirstname());
            return new Reponse_ADD_CONSULTATION(true);
        }
        catch (CAPException e)
        {
            logger.Trace("Erreur logique lors de la création : " + e.getMessage());
            return new Reponse_ADD_CONSULTATION(false);
        }
        catch (SQLException e) {
            logger.Trace("💾 Erreur SQL : " + e.getMessage());
            return new Reponse_ADD_CONSULTATION(false);
        }
        catch (Exception e)
        {
            logger.Trace("⚠️ Erreur inattendue : " + e.getMessage());
            return new Reponse_ADD_CONSULTATION(false);
        }
    }

    String getLoginBySocket(Socket socket) {
        for (var entry : clientsConnectes.entrySet()) {
            if (entry.getValue().equals(socket)) {
                return entry.getKey();
            }
        }
        return null; // si pas trouvé
    }






    // ============================================================
    // ADD_PATIENT
    // ============================================================
    private synchronized Reponse_ADD_PATIENT TraiteRequeteADD_PATIENT(Requete_ADD_PATIENT requete, Socket socket) {
        try
        {
            logger.Trace("Ajout d'un patient : " + requete);


            Patient patient = new Patient();
            patient.setLastname(requete.getLastName());
            patient.setFirstname(requete.getFirstName());

            PatientDAO patientDAO = new PatientDAO();
            patientDAO.save(patient);


            return new Reponse_ADD_PATIENT(1); // simulate success
        }
        catch (SQLException e)
        {
            logger.Trace("💾 Erreur SQL : " + e.getMessage());
            return new Reponse_ADD_PATIENT(-1);
        }

    }








    // ============================================================
    // UPDATE_CONSULTATION
    // ============================================================
    private synchronized Reponse_UPDATE_CONSULTATION TraiteRequeteUPDATE_CONSULTATION(Requete_UPDATE_CONSULTATION requete, Socket socket) {

        try
        {
            logger.Trace("Mise à jour consultation ID=" + requete.getIdConsultation());
            Integer idCons = requete.getIdConsultation();

            Consultation cons1 = consultationDAO.getById(idCons);

            if(cons1 == null)
            {
                logger.Trace("Consultation ID=" + idCons + " non trouvée.");
                throw new CAPException("Erreur : Consultation non trouvée.");
            }

            if(cons1.getPatient_id() == null)
            {
                // attribue un patient
                cons1.setPatient_id(requete.getIdPatient());
                cons1.setReason(requete.getNouvelleRaison());
            }
            else
            {
               // modifie date et heure
                cons1.setDate(requete.getNouvelleDate());
                cons1.setHour(requete.getNouvelleHeure());
            }

            consultationDAO.save(cons1);

            return new Reponse_UPDATE_CONSULTATION(true);
        } catch (CAPException e) {
            logger.Trace("Erreur logique lors de la mise a jour : " + e.getMessage());
            return new Reponse_UPDATE_CONSULTATION(false);
        }
        catch (SQLException e) {
            logger.Trace("💾 Erreur SQL : " + e.getMessage());
            return new Reponse_UPDATE_CONSULTATION(false);
        }

    }







    // ============================================================
    // SEARCH_CONSULTATIONS
    // ============================================================
    private synchronized Reponse_SEARCH_CONSULTATIONS TraiteRequeteSEARCH_CONSULTATIONS(Requete_SEARCH_CONSULTATIONS requete, Socket socket) {
        try{
            logger.Trace("Recherche consultations (critères : " + requete + ")");


            ConsultationSearchVM consultationSearchVM = new ConsultationSearchVM();
            consultationSearchVM.setId(requete.getIdPatient());
            consultationSearchVM.setDate(requete.getDate());

            consultationDAO.load(consultationSearchVM);

            if (consultationDAO.getList().isEmpty()) {
                throw new CAPException("Aucune Consultation trouvé associé a cette utilistateur et a cette date");
            }

            Reponse_SEARCH_CONSULTATIONS response = new Reponse_SEARCH_CONSULTATIONS();

            for(Consultation cons : consultationDAO.getList())
            {
                logger.Trace("Consultation trouvée : " + cons);
                response.addConsultation(cons);
            }

            return response;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (CAPException e) {
            logger.Trace("Erreur logique lors de la création : " + e.getMessage());
            return new Reponse_SEARCH_CONSULTATIONS();
        }


    }








    // ============================================================
    // DELETE_CONSULTATION
    // ============================================================
    private synchronized Reponse_DELETE_CONSULTATION TraiteRequeteDELETE_CONSULTATION(Requete_DELETE_CONSULTATION requete, Socket socket) {

        try{
            Integer idCons = requete.getIdConsultation();
            consultationDAO.delete(idCons);


            logger.Trace("Suppression consultation ID=" + requete.getIdConsultation());

            return new Reponse_DELETE_CONSULTATION(true);
        } catch (Exception e) {
            logger.Trace("Erreur Inconnu : " + e.getMessage());
            return new Reponse_DELETE_CONSULTATION(false);
        }

    }







    // ============================================================
    // 🔹 LOGOUT
    // ============================================================
    private synchronized void TraiteRequeteLOGOUT(Requete_LOGOUT requete) throws FinConnexionException {
        logger.Trace("RequeteLOGOUT reçue de " + requete.getLogin());
        clientsConnectes.remove(requete.getLogin());
        logger.Trace(requete.getLogin() + " correctement déloggé");
    }
}
