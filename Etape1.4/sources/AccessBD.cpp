#include "AccessBD.h"
#include <stdio.h>
#include <stdlib.h>
#include <cstring>
#include "patient.h"
#include <string.h>

using namespace std;

// ------------------------------------------------------------
// Connexion MySQL globale
// ------------------------------------------------------------
static MYSQL *connexionGlobale = NULL;

// ------------------------------------------------------------
// Ouverture de la connexion (appelée une seule fois au début)
// ------------------------------------------------------------
void ouvrirConnexion()
{
    if (connexionGlobale != NULL)
    {
        printf("[ACCESSBD] Connexion déjà ouverte.\n");
        return;
    }

    connexionGlobale = mysql_init(NULL);
    if (mysql_real_connect(connexionGlobale,
                           "localhost",
                           "Student",
                           "PassStudent1_",
                           "PourStudent",
                           0, NULL, 0) == NULL)
    {
        fprintf(stderr, "Erreur de connexion à la BD: %s\n", mysql_error(connexionGlobale));
        exit(1);
    }

    printf("[ACCESSBD] Connexion établie avec succès à la BD.\n");
}

// ------------------------------------------------------------
// Fermeture de la connexion (appelée au logout ou fin du serveur)
// ------------------------------------------------------------
void fermerConnexion()
{
    if (connexionGlobale)
    {
        mysql_close(connexionGlobale);
        connexionGlobale = NULL;
        printf("[ACCESSBD] Connexion à la BD fermée.\n");
    }
}

// ------------------------------------------------------------
// Fonctions existantes adaptées pour utiliser connexionGlobale
// ------------------------------------------------------------
int estPresent(const char *nom)
{
    MYSQL_RES *Resultat;
    MYSQL_ROW ligne;

    char requete[256];
    sprintf(requete, "SELECT * FROM patients;");

    if (mysql_query(connexionGlobale, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexionGlobale));
        exit(1);
    }
    printf("[ACCESSBD] Requête Pour savoir si * utilisateur existe %s \n", requete);

    if ((Resultat = mysql_store_result(connexionGlobale)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexionGlobale));
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

    mysql_free_result(Resultat);
    return 0;
}

int AddNewPatient(const char *lastname, const char *firstname, const char *birthdate)
{
    char requete[256];
    sprintf(requete, "INSERT INTO patients VALUES(NULL,'%s','%s',NULL);", lastname, firstname);

    printf("[ACCESSBD] requête AddNewPatient envoyée %s \n", requete);

    if (mysql_query(connexionGlobale, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexionGlobale));
        return 0;
    }

    printf("[ACCESSBD] Insertion OK.\n");
    int IdOfUser = mysql_insert_id(connexionGlobale);
    return IdOfUser;
}

Patient getPatientById(int id)
{
    MYSQL_ROW row;
    MYSQL_RES *resultat;
    Patient retour;

    char requete[256];
    sprintf(requete, "select * from patients where id=%d;", id);

    if (mysql_query(connexionGlobale, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexionGlobale));
        exit(0);
    }

    resultat = mysql_store_result(connexionGlobale);
    if (resultat == NULL)
    {
        fprintf(stderr, "(AccessBD) Erreur de récupération des résultats: %s\n", mysql_error(connexionGlobale));
        exit(0);
    }

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
    }

    mysql_free_result(resultat);
    return retour;
}

bool retourSpecialite(char *reponse)
{
    MYSQL_RES *Resultat;
    MYSQL_ROW ligne;

    char requete[256];
    sprintf(requete, "SELECT * FROM specialties;");

    if (mysql_query(connexionGlobale, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexionGlobale));
        return false;
    }

    if ((Resultat = mysql_store_result(connexionGlobale)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexionGlobale));
        return false;
    }

    int nbChamps = mysql_num_fields(Resultat);
    char retour[2048] = "";
    while ((ligne = mysql_fetch_row(Resultat)) != NULL)
    {
        for (int i = 0; i < nbChamps; i++)
        {
            strcat(retour, ligne[i]);
            strcat(retour, "#");
        }
    }

    strcpy(reponse, retour);
    mysql_free_result(Resultat);
    return true;
}

bool retourDocteur(char *reponse)
{
    MYSQL_RES *Resultat;
    MYSQL_ROW ligne;

    char requete[256];
    sprintf(requete, "SELECT id, first_name, last_name FROM doctors;");

    if (mysql_query(connexionGlobale, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexionGlobale));
        return false;
    }

    if ((Resultat = mysql_store_result(connexionGlobale)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexionGlobale));
        return false;
    }

    int nbChamps = mysql_num_fields(Resultat);
    char retour[2048] = "";
    while ((ligne = mysql_fetch_row(Resultat)) != NULL)
    {
        for (int i = 0; i < nbChamps; i++)
        {
            strcat(retour, ligne[i]);
            strcat(retour, "#");
        }
    }

    strcpy(reponse, retour);
    mysql_free_result(Resultat);
    return true;
}

bool retourConsultations(char *reponse, char *specialty, char *doctor, char *startDate, char *endDate)
{
    MYSQL_RES *Resultat;
    MYSQL_ROW ligne;

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

    if (mysql_query(connexionGlobale, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexionGlobale));
        return false;
    }

    if ((Resultat = mysql_store_result(connexionGlobale)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexionGlobale));
        return false;
    }

    char retour[2048] = "";
    if (mysql_num_rows(Resultat) == 0)
    {
        strcpy(reponse, "NULL");
        return true;
    }

    int nbChamps = mysql_num_fields(Resultat);
    while ((ligne = mysql_fetch_row(Resultat)) != NULL)
    {
        for (int i = 0; i < nbChamps; i++)
        {
            strcat(retour, ligne[i]);
            strcat(retour, "#");
        }
    }

    strcat(retour, "END");
    strcpy(reponse, retour);
    mysql_free_result(Resultat);
    return true;
}

bool modifyConsultation(char *reponse, int idConsultation, char *raison, int idPatient)
{
    char requete[512];
    sprintf(requete, "UPDATE consultations SET reason = '%s', patient_id = %d WHERE id = %d AND patient_id IS NULL", raison, idPatient, idConsultation);

    if (mysql_query(connexionGlobale, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexionGlobale));
        return false;
    }

    my_ulonglong nbLignes = mysql_affected_rows(connexionGlobale);
    if (nbLignes == 0)
    {
        strcpy(reponse, "BOOK_CONSULTATION#NON");
    }
    else
    {
        strcpy(reponse, "BOOK_CONSULTATION#OUI");
    }

    return true;
}
