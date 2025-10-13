#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <pthread.h>
#include <string>
#include "serveur.h"
#include "TCP.h"
#include "CBH.h"
#include "AccessBD.h"
#include "configReseau.h"

using namespace std;

// --- Définitions (une seule fois, ici) ---
int sEcouteS = -1;
int socketsAcceptees[TAILLE_FILE_ATTENTE];
int indiceEcriture = 0, indiceLecture = 0;
pthread_mutex_t mutexSocketsAcceptees;
pthread_cond_t condSocketsAcceptees;

int main()
{

    // Initialisation socketsAcceptees
    pthread_mutex_init(&mutexSocketsAcceptees, NULL);
    pthread_cond_init(&condSocketsAcceptees, NULL);
    for (int i = 0; i < TAILLE_FILE_ATTENTE; i++)
        socketsAcceptees[i] = -1;

    // Armement des signaux
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;
    if (sigaction(SIGINT, &A, NULL) == -1)
    {
        perror("[SERVEUR] Erreur de sigaction");
        exit(1);
    }

    // Creation de la socket d'écoute
    if ((sEcouteS = ServerSocket(PORT_RESERVATION)) == -1)
    {
        perror("[SERVEUR] Erreur de ServeurSocket");
        exit(1);
    }

    // Creation du pool de threads
    printf("[SERVEUR] Création du pool de threads.\n");
    pthread_t th;
    for (int i = 0; i < NB_THREADS_POOL; i++)
        pthread_create(&th, NULL, FctThreadClient, NULL);

    // Mise en boucle du serveur
    int sService;
    char ipClient[50];
    printf("[SERVEUR] Demarrage du serveur.\n");
    
    while (1)
    {
        printf("[THREAD %p] Attente d'une connexion...\n", pthread_self());
        if ((sService = Accept(sEcoute, ipClient)) == -1)
        {
            perror("[THREAD %p] Erreur de Accept");
            close(sEcoute);
            CBH_Close();
            exit(1);
        }
        printf("[THREAD %p] Connexion acceptée : \n\t -- IP=%s \n\t --socket=%d\n", pthread_self(), ipClient, sService);

        // Insertion en liste d'attente et réveil d'un thread du pool (Production d'une tâche)
        pthread_mutex_lock(&mutexSocketsAcceptees);
        socketsAcceptees[indiceEcriture] = sService; // !!!
        indiceEcriture++;
        if (indiceEcriture == TAILLE_FILE_ATTENTE)
            indiceEcriture = 0;
        pthread_mutex_unlock(&mutexSocketsAcceptees);
        pthread_cond_signal(&condSocketsAcceptees);
    }
}

void *FctThreadClient(void *p)
{
    int sService;
    while (1)
    {
        printf("[THREAD %p] Attente socket...\n", pthread_self());

        // Attente d'une tâche
        pthread_mutex_lock(&mutexSocketsAcceptees);
        while (indiceEcriture == indiceLecture)
            pthread_cond_wait(&condSocketsAcceptees, &mutexSocketsAcceptees);
        sService = socketsAcceptees[indiceLecture];
        socketsAcceptees[indiceLecture] = -1;
        indiceLecture++;
        if (indiceLecture == TAILLE_FILE_ATTENTE)
            indiceLecture = 0;
        pthread_mutex_unlock(&mutexSocketsAcceptees);

        // Traitement de la connexion (consommation de la tâche)
        printf("[THREAD %p] Je m'occupe de la socket %d\n", pthread_self(), sService);
        TraitementConnexion(sService);
    }
}

void TraitementConnexion(int sService)
{
    char requete[200], reponse[200];
    int nbLus, nbEcrits;
    bool onContinue = true;
    

    while (onContinue)
    {
        printf("[THREAD %p] Attente requete...\n", pthread_self());

        // ***** Reception Requete ******************
        if ((nbLus = Receive(sService, requete)) < 0)
        {
            perror("Erreur de Receive");
            close(sService);
            HandlerSIGINT(0);
        }

        // ***** Fin de connexion ? *****************
        if (nbLus == 0)
        {
            printf("[THREAD %p] Fin de connexion du client.\n", pthread_self());
            close(sService);
            return;
        }

        requete[nbLus] = 0;
        printf("[THREAD %p] Requete recue = %s\n", pthread_self(), requete);

        // ***** Traitement de la requete ***********
        onContinue = CBH(requete, reponse, sService);

        // ***** Envoi de la reponse ****************
        if ((nbEcrits = Send(sService, reponse, strlen(reponse))) < 0)
        {
            perror("Erreur de Send");
            close(sService);
            HandlerSIGINT(0);
        }

        printf("[THREAD %p] Reponse envoyee = %s\n", pthread_self(), reponse);

        if (!onContinue)
            printf("[THREAD %p] Fin de connexion de la socket %d\n %d\n", pthread_self(), sService);
    }
    close(sService);
}

void HandlerSIGINT(int s)
{
    printf("[SERVEUR] Arret du serveur.\n");
    close(sEcoute);
    pthread_mutex_lock(&mutexSocketsAcceptees);
    for (int i = 0; i < TAILLE_FILE_ATTENTE; i++)
        if (socketsAcceptees[i] != -1)
            close(socketsAcceptees[i]);
    pthread_mutex_unlock(&mutexSocketsAcceptees);
    CBH_Close();
    exit(0);
}