#include "AccessBD.h"
#include <stdio.h>
#include <stdlib.h>
#include <mysql.h>
#include "patient.h"

int estPresent(const char *nom)
{
    //Connexion à la base de donnée
    MYSQL *connexion;
    connexion = mysql_init(NULL);
    if (mysql_real_connect(connexion, "localhost",
                           "Student",
                           "PassStudent1_",
                           "PourStudent",
                           0, NULL, 0) == NULL)
    {
        fprintf(stderr, "Erreur de connexion à la BD: %s\n", mysql_error(connexion));
        exit(1);
    }

    //Construction + envoi de la requete de sélection
    char requete[256];
    sprintf(requete, "select * from patients"); // id, last_name, first_name

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    // Affichage du resultat
    MYSQL_RES *ResultSet;

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    MYSQL_ROW ligne;
    int nbChamps = mysql_num_fields(ResultSet);
    while((ligne = mysql_fetch_row(ResultSet))!=NULL)
    {
        for(int i=0;i<nbChamps;i++)
        {
            printf("%s",ligne[i]);
        }

        printf("\n");
    }

    //Deconnexion de la base de données
    mysql_close(connexion);
    exit(0);
}

int AddNewPatient(const char *lastname, const char *firstname, const char *birthdate)
{
    MYSQL *connexion;
    connexion = mysql_init(NULL);

    // Connexion à la base de données
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
    printf("Connexion établie avec succès à la BD.\n");

    char requete[256];
    sprintf(requete, "insert into patients values(NULL,'%s','%s','%s');");

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }
    printf("Insertion OK.\n");

    // Déconnexion de la base de données
    mysql_close(connexion);
    exit(0);
}

Patient* getPatientById(int id)
{
    MYSQL *connexion;
    connexion = mysql_init(NULL);
    Patient *retour;

    // Connexion à la base de données
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
    printf("Connexion établie avec succès à la BD.\n");

    char requete[256];
    sprintf(requete, "select * from patients where id=%d;", id);

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }
    printf("Requête OK.\n");

    // affichage du resultat
    MYSQL_RES *ResultSet;

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    MYSQL_ROW row;
    row = mysql_fetch_row(ResultSet);

    if (row == NULL)
    {
        return nullptr;
    }
    else
    {
        return retour;
    }

    // Déconnexion de la base de données
    mysql_close(connexion);
    return nullptr;
}
