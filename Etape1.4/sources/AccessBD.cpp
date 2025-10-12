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

void retourSpecialite(char* reponse)
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
        exit(1);
    }

    if ((Resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
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

    strcpy(reponse,retour);  
    mysql_close(connexion);
}

void retourDocteur(char * reponse)
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
        exit(1);
    }

    if ((Resultat = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
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

}