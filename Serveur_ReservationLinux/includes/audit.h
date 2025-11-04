#ifndef AUDIT_H
#define AUDIT_H

#include <pthread.h>   

typedef struct {
    int  socket;
    int  idPatient;
    char* nom;
    char* prenom;
    char* adresseIp;
} ClientStocker;

extern ClientStocker clientsConnectes[100];
extern int nbClients;
extern pthread_mutex_t mutexClients;   

#endif
