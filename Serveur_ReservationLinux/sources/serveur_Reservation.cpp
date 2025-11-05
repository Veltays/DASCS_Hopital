#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <pthread.h>
#include <string>
#include "serveur.h"
#include "TCP.h"
#include "CBP.h"
#include "ACBP.h"
#include "AccessBD.h"
#include "AccessFileConfig.h"

using namespace std;

int sEcouteReservation, sEcouteAdmin;



// --- Définitions (une seule fois, ici) ---
int socketsAcceptees[TAILLE_FILE_ATTENTE];
int indiceEcriture = 0, indiceLecture = 0;

char portStr[10];
char NbThreadsPoolStr[10];
char ipServeurStr[50];
char portAdminStr[10];

int portReservation;
int NbThreadsPool;
int portAdmin;

pthread_mutex_t mutexSocketsAcceptees;
pthread_cond_t condSocketsAcceptees;



pthread_t thClient;
pthread_t thAdmin;


int main()
{

    if (!getConfigValue("PORT_RESERVATION", portStr))
        return false;
    if (!getConfigValue("NB_THREADS_POOL", NbThreadsPoolStr))
        return false;
    if (!getConfigValue("IP_SERVER", ipServeurStr))
        return false;
    if (!getConfigValue("PORT_ADMIN", portAdminStr))
        return false;

    portReservation = atoi(portStr);
    NbThreadsPool = atoi(NbThreadsPoolStr);
    portAdmin = atoi(portAdminStr);

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

    // Creation de la socket d'écoute du serveur pour le client
    if ((sEcouteReservation = ServerSocket(ipServeurStr, portReservation)) == -1)
    {
        perror("[SERVEUR] Erreur de ServeurSocket pour le service de réservation");
        exit(1);
    }

    // Creation de la socket d'écoute du serveur pour le client
    if ((sEcouteAdmin = ServerSocket(ipServeurStr, portAdmin)) == -1)
    {
        perror("[SERVEUR] Erreur de ServeurSocket pour le service admin");
        exit(1);
    }



    printf("[SERVEUR] En écoute sur ports %d (CBP) et %d (ACBP)\n", portReservation, portAdmin);

 
    // Creation du pool de threads pour le service de réservation
    printf("[SERVEUR] Création du pool de threads.\n");
    for (int i = 0; i < NbThreadsPool; i++)
        pthread_create(&thClient, NULL, FctThreadClientReservation, NULL);


    // Creation du thread pour le service admin
    pthread_create(&thAdmin, NULL, FctThreadAdmin, &sEcouteAdmin);



    // Mise en boucle du serveur
    int sService;
    char ipClient[50];
    printf("[SERVEUR] Demarrage du serveur.\n");

    while (1)
    {
        printf("[THREAD %p] Attente d'une connexion...\n", pthread_self());
        if ((sService = Accept(sEcouteReservation, ipClient)) == -1)
        {
            perror("[THREAD %p] Erreur de Accept");
            close(sEcouteReservation);
            CBP_Close();
            exit(1);
        }
        printf("[THREAD %p] Connexion acceptée : \n\t -- IP=%s \n\t --socket=%d \n", pthread_self(), ipClient, sService);

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

void *FctThreadClientReservation(void *p)
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
        printf("[THREAD %p] Je m'occupe de la socket %d, à l'indice %d\n", pthread_self(), sService, indiceLecture);
        TraitementConnexion(sService);
    }
}



void *FctThreadAdmin(void *p)
{
    int sEcouteAdmin = *((int *)p);
    char ipClient[50];

    printf("[ADMIN] Serveur ACBP prêt sur le port admin.\n");

    while (1)
    {
        int sService = Accept(sEcouteAdmin, ipClient);
        if (sService == -1) continue;

        pthread_t th;
        int *arg = (int *)malloc(sizeof(int)); 
        *arg = sService;
        pthread_create(&th, NULL, FctThreadAdminClient, arg);
        pthread_detach(th);
    }
}




void *FctThreadAdminClient(void *p)
{
    int sService = *((int *)p);
    free(p);

    char requete[200], reponse[200];
    int nbLus = Receive(sService, requete);

    if (nbLus > 0)
    {
        if (!ACBP(requete, reponse, sService))
            printf("[ADMIN] Erreur traitement requête admin\n");
        else
            Send(sService, reponse, strlen(reponse));
    }

    close(sService);
    return NULL;
}





void TraitementConnexion(int sService)
{
    char requete[512];
    char reponse[2048];
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
        onContinue = CBP(requete, reponse, sService);

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
    pthread_detach(thAdmin);
    pthread_detach(thClient);
    
    close(sEcouteReservation);
    close(sEcouteAdmin);
    pthread_mutex_lock(&mutexSocketsAcceptees);
    for (int i = 0; i < TAILLE_FILE_ATTENTE; i++)
        if (socketsAcceptees[i] != -1)
            close(socketsAcceptees[i]);
    pthread_mutex_unlock(&mutexSocketsAcceptees);
    CBP_Close();
    exit(0);
} 