#include "audit.h"
#include <pthread.h>

// === Définitions uniques ===
ClientStocker clientsConnectes[100];
int nbClients = 0;
pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;
