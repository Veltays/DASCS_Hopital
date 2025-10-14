#include "AccessBD.h"
#include <stdio.h>
#include <stdlib.h>
#include <cstring>
#include "patient.h"
#include <string.h>

using namespace std;


MYSQL *connectToDatabase(void)
{
    MYSQL *connexion = mysql_init(NULL);

    if (mysql_real_connect(connexion,
                           "localhost",
                           "Student",
                           "PassStudent1_",
                           "PourStudent",
                           0, NULL, 0) == NULL)
    {
        fprintf(stderr, "Erreur de connexion à la BD: %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("[ACCESSBD] Connexion établie avec succès à la BD.\n");
    return connexion;
}

int estPresent(const char *nom)
{

    MYSQL_RES *Resultat;
    MYSQL_ROW ligne;

    // Connexion à la base de donnée
    MYSQL *connexion = connectToDatabase();

    // Construction + envoi de la requete de sélection
    char requete[256];
    sprintf(requete, "SELECT * FROM patients;"); // id, last_name, first_name, birthdate

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }
    printf("[ACCESSBD] Requête Pour savoir si * utilisateur existe %s \n", requete);

    if ((Resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    int nbChamps = mysql_num_fields(Resultat);
    while ((ligne = mysql_fetch_row(Resultat)) != NULL)
    {
        for (int i = 0; i < nbChamps; i++)
        {
            printf("[ACCESSBD] %s", ligne[i]);
        }

        printf("[ACCESSBD] \n");
    }

    // Deconnexion de la base de données
    mysql_close(connexion);
    exit(0);
}

int AddNewPatient(const char *lastname, const char *firstname, const char *birthdate)
{
   // Connexion à la base de donnée
    MYSQL *connexion = connectToDatabase();
    printf("[ACCESSBD] Connexion établie avec succès à la BD.\n");

    char requete[256];
    sprintf(requete, "INSERT INTO patients VALUES(NULL,'%s','%s',NULL);", lastname, firstname);

    printf("[ACCESSBD] requête AddNewPatient envoyée %s \n", requete);

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        return 0;
    }
    printf("[ACCESSBD] Insertion OK.\n");

    int IdOfUser = mysql_insert_id(connexion);
    mysql_close(connexion);

    // permet de recuperer le dernier id apres l'insert
    return IdOfUser;

}


Patient getPatientById(int id)
{
    MYSQL_ROW row;
    MYSQL_RES *resultat;
    Patient retour;

    // Connexion à la base de donnée
    MYSQL *connexion = connectToDatabase();

    printf("[ACCESSBD] Connexion établie avec succès à la BD.\n");

    char requete[256];
    sprintf(requete, "select * from patients where id=%d;", id);

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(0);
    }
    printf("[ACCESSBD] Requête OK.\n");

    resultat = mysql_store_result(connexion);

    if (resultat == NULL)
    {
        fprintf(stderr, "(AccessBD) Erreur de récupération des résultats: %s\n", mysql_error(connexion));
        exit(0);
    }
    printf("[ACCESSBD] Store ok");

    row = mysql_fetch_row(resultat);

    if (row == NULL)
    {
        exit(0);
    }

    else
    {
        retour.id = atoi(row[0]);
        strcpy(retour.lastname, row[1]);
        strcpy(retour.firstname, row[2]);
        // strcpy(retour.birthdate,row[3]);
    }

    mysql_free_result(resultat);
    // Déconnexion de la base de données
    mysql_close(connexion);
    return retour;
}

bool retourSpecialite(char* reponse)
{
    // Connexion à la base de donnée
    MYSQL *connexion = connectToDatabase();
    MYSQL_RES * Resultat;
    MYSQL_ROW ligne;

    //Construction + envoi de la requête de sélection
    char requete[256];
    sprintf(requete,"SELECT * FROM specialties;");

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        return false;
    }

    if ((Resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        return false;
    }

    int nbChamps = mysql_num_fields(Resultat);
    char retour[2048] = "";
    while ((ligne = mysql_fetch_row(Resultat)) != NULL)
    {
        for (int i = 0; i < nbChamps; i++)
        {
            strcat(retour,ligne[i]);
            strcat(retour,"#");
        }
    }

    printf("[ACCESSBD] Retour du retour: %s\n",retour);

    strcpy(reponse,retour);  
    mysql_close(connexion);
    return true;
}

bool retourDocteur(char * reponse)
{
    // Connexion à la base de donnée
    MYSQL *connexion = connectToDatabase();

    MYSQL_RES * Resultat;
    MYSQL_ROW ligne;

    //Construction + envoi de la requête de sélection
    char requete[256];
    sprintf(requete,"SELECT id, first_name, last_name FROM doctors;");

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        return false;
    }

    if ((Resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        return false;
    }

    int nbChamps = mysql_num_fields(Resultat);
    char retour[2048]= "";
    while ((ligne = mysql_fetch_row(Resultat)) != NULL)
    {
        for (int i = 0; i < nbChamps; i++)
        {
            strcat(retour,ligne[i]);
            strcat(retour,"#");
        }
    }

    strcpy(reponse,retour);
    mysql_close(connexion);

    return true;

}

bool retourConsultations(char * reponse,char* specialty,char* doctor, char * startDate, char* endDate)
{
    //connexion à la bad
    MYSQL * connexion = connectToDatabase();

    MYSQL_RES * Resultat;
    MYSQL_ROW ligne;

    //Construction + envoi de la requête de sélection
    char requete[512];
    sprintf(requete,
        "SELECT c.id, d.first_name, d.last_name, s.name, c.date, c.hour "
        "FROM consultations c "
        "INNER JOIN doctors d ON c.doctor_id = d.id "
        "INNER JOIN specialties s ON d.specialty_id = s.id "
        "WHERE s.name LIKE '%%%s%%' "
        "AND CONCAT(d.first_name, ' ', d.last_name) LIKE '%%%s%%' "
        "AND c.date BETWEEN '%s' AND '%s' "
        "AND c.patient_id IS NULL;",
        specialty, doctor, startDate, endDate);
    // id de la consultation, nom du médecin, nom de la specialite, date, heure

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        return false;
    }

    if ((Resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        return false;
    }

    char retour[2048]= "";
    if(mysql_num_rows(Resultat)==0)
    {
        strcpy(reponse,"NULL");
        return true;
    }

    int nbChamps = mysql_num_fields(Resultat);
    
    while ((ligne = mysql_fetch_row(Resultat)) != NULL)
    {
        for (int i = 0; i < nbChamps; i++)
        {
            strcat(retour,ligne[i]);
            strcat(retour,"#");
        }
    }

    strcat(retour,"END");

    strcpy(reponse,retour);
    mysql_close(connexion);

    return true;

}

bool modifyConsultation(char * reponse, int idConsultation, char * raison, int idPatient)
{
    //connexion à la bad
    MYSQL * connexion = connectToDatabase();

    MYSQL_RES * Resultat;
    MYSQL_ROW ligne;

    //Construction + envoi de la requete
    char requete[512];
    sprintf(requete,"UPDATE consultations SET reason = '%s', patient_id = %d WHERE id = %d AND patient_id IS NULL",raison,idPatient,idConsultation);

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        return false;
    }


    printf("[ACCESSBD] requête modifyConsultation envoyée %s \n", requete);

    printf("affected_rows=%llu, mysql_error='%s'\n", mysql_affected_rows(connexion), mysql_error(connexion));



    my_ulonglong nbLignes = mysql_affected_rows(connexion);

    if (nbLignes == 0)
    {
        printf("[ACCESSBD] Aucune ligne modifiée, la consultation est peut-être déjà réservée.\n");
        strcpy(reponse,"BOOK_CONSULTATION#NON");
    }
    else
    {
        printf("[ACCESSBD] Consultation réservée avec succès.\n");
        strcpy(reponse,"BOOK_CONSULTATION#OUI");

    }

    

    mysql_close(connexion);
    return true;
}