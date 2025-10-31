#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include "ACBP.h"
#include "audit.h"

const int CMD_AUDIT = 1;

bool ACBP(char *requete, char *reponse, int socket)
{


    // ***** Récupération nom de la requete *****************
    char *ptr = strtok(requete, "#");
    printf("[ACBP] requete traiter %s \n",ptr);
    int cmd;

    
 
    switch (cmd = getCommandReceiveACPB(ptr))
    {
    case CMD_AUDIT:
          printf("[THREAD %p] AUDIT\n", pthread_self());
            reponse[0] = '\0'; // vide le buffer avant concaténation

            pthread_mutex_lock(&mutexClients);
            for (int i = 0; i < nbClients; i++) {
                char clientInfo[256];
                sprintf(clientInfo, "ID:%d,Nom:%s,Prenom:%s,IP:%s",
                        clientsConnectes[i].idPatient,
                        clientsConnectes[i].nom,
                        clientsConnectes[i].prenom,
                        clientsConnectes[i].adresseIp);

                strcat(reponse, clientInfo);

                // Ajouter un '#' sauf pour le dernier
                if (i < nbClients - 1)
                    strcat(reponse, "#");
                
            }
            printf("[THREAD %p] Nombre de clients connectés : %d\n", pthread_self(), nbClients);
            printf("[THREAD %p] Contenu de la réponse avant envoi : %s\n", pthread_self(), reponse);
            pthread_mutex_unlock(&mutexClients);

            printf("[THREAD %p] AUDIT reponse = %s\n", pthread_self(), reponse);
            return true;

    }
    return false;
}




int getCommandReceiveACPB(char *ptr)
{

    if (strcmp(ptr, "GETAUDIT") == 0)

        return CMD_AUDIT;

    return -1;
}
