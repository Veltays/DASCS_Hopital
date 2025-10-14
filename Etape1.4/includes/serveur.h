#ifndef SERVEUR_H
#define SERVEUR_H

#include <pthread.h>
#include <signal.h>
#include "configReseau.h"






// --- Prototypes (pas de main ici !) ---
void HandlerSIGINT(int s);
void TraitementConnexion(int sService);
void *FctThreadClient(void *p);





// --- Variables globales (en extern seulement !) ---
extern int sEcoute;
extern int socketsAcceptees[TAILLE_FILE_ATTENTE];
extern int indiceEcriture;
extern int indiceLecture;
extern pthread_mutex_t mutexSocketsAcceptees;
extern pthread_cond_t condSocketsAcceptees;



#endif