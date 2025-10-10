#include "AccessBD.h"
#include <stdio.h>
#include <stdlib.h>
#include <mysql.h>
#include <cstring> 
#include "patient.h"

int estPresent(const char *nom)
{
    // Connexion à la base de donnée
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

    // Construction + envoi de la requete de sélection
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
    while ((ligne = mysql_fetch_row(ResultSet)) != NULL)
    {
        for (int i = 0; i < nbChamps; i++)
        {
            printf("%s", ligne[i]);
        }

        printf("\n");
    }

    // Deconnexion de la base de données
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
    sprintf(requete, "insert into patients values(NULL,'%s','%s',NULL);", lastname, firstname);

    printf(" requqte envoyer %s \n", requete);

    if (mysql_query(connexion, requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        return 0;
    }
    printf("Insertion OK.\n");
    return 1;

    // Déconnexion de la base de données
    mysql_close(connexion);
}

Patient getPatientById(int id)
{
    MYSQL *connexion;
    MYSQL_ROW row;
    MYSQL_RES *resultat;


    connexion = mysql_init(NULL);
    Patient retour;

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
        exit(0);
    }
    printf("Requête OK.\n");



    resultat = mysql_store_result(connexion);


  if (resultat == NULL)
      {
        fprintf(stderr, "(AccessBD) Erreur de récupération des résultats: %s\n", mysql_error(connexion));
        exit(0);
      }
    printf("Store ok");


    row  = mysql_fetch_row(resultat);

    if (row == NULL)
    {
     exit(0);   
    }
    else
    {
        strcpy(retour.firstname, row[0]);
        strcpy(retour.lastname, row[1]);
        strcpy(retour.birthdate,row[2]);


    }


    mysql_free_result(resultat);
    // Déconnexion de la base de données
    mysql_close(connexion);
    return retour;
    
}
