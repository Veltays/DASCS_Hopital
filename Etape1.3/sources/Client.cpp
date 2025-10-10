#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include "TCP.h"
#include "CBH.h"
#include "client.h"

int sClient;

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        printf("Erreur...\n");
        printf("USAGE : Client ipServeur portServeur\n");
        exit(1);
    }
    // Armement des signaux
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;
    if (sigaction(SIGINT, &A, NULL) == -1)
    {
        perror("Erreur de sigaction");
        exit(1);
    }
    // Connexion sur le serveur
    if ((sClient = ClientSocket(argv[1], atoi(argv[2]))) == -1)
    {
        perror("Erreur de ClientSocket");
        exit(1);
    }
    printf("Connecte sur le serveur.\n");
    // Phase de login
    char user[50], password[50];
    printf("user: ");
    fgets(user, 50, stdin);
    user[strlen(user) - 1] = 0;
    printf("password: ");
    fgets(password, 50, stdin);
    password[strlen(password) - 1] = 0;
    

    // envoie des données au client

    char requetes[200], reponse[200];
    snprintf(requetes, sizeof(requetes), "LOGIN#%s#%s", user, password);

    Echange(requetes, reponse);

    printf("Reponse du serveur : %s\n", reponse);

    if (strcmp(reponse, "LOGIN#OK") != 0)
    {
        printf("Erreur de login.\n");
        exit(1);
    }

    printf("Login reussi.\n");
}

//***** Fin de connexion ********************************************
void HandlerSIGINT(int s)
{
    printf("\nArret du client.\n");
    CBH_Logout(sClient);
    close(sClient);
    exit(0);
}

//***** Echange de données entre client et serveur ******************
void Echange(char *requete, char *reponse)
{
    int nbEcrits, nbLus;
    // ***** Envoi de la requete ****************************
    if ((nbEcrits = Send(sClient, requete, strlen(requete))) == -1)
    {
        perror("Erreur de Send");
        close(sClient);
        exit(1);
    }
    // ***** Attente de la reponse **************************
    if ((nbLus = Receive(sClient, reponse)) < 0)
    {
        perror("Erreur de Receive");
        close(sClient);
        exit(1);
    }
    if (nbLus == 0)
    {
        printf("Serveur arrete, pas de reponse reçue...\n");
        close(sClient);
        exit(1);
    }
    reponse[nbLus] = 0;
}
