#include "CBH.h"
#include "TCP.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include "AccessBD.h"

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;
int clients[NB_MAX_CLIENTS];
int nbClients = 0;

const int CMD_LOGIN = 1;
const int CMD_LOGOUT = 2;
const int CMD_GET_SPECIALTIES = 3;
const int CMD_GET_DOCTORS = 4;
const int CMD_SEARCH_CONSULTATIONS = 5;
const int CMD_BOOK_CONSULTATION = 6;

bool CBH(char *requete, char *reponse, int socket)
{

    // ***** Récupération nom de la requete *****************
    char *ptr = strtok(requete, "#");

    int cmd;
    char firstname[50], lastname[50], PatientId[50];
    char specialty[50], doctor[50], startDate[50], endDate[50];
    int ResultOrID;

    int idConsultation;
    int idPatient;

    switch (cmd = getCommandReceive(ptr))
    {
    case CMD_LOGIN:
        strcpy(lastname, strtok(NULL, "#"));
        strcpy(firstname, strtok(NULL, "#"));
        strcpy(PatientId, strtok(NULL, "#"));
        printf("[THREAD %p] LOGIN de %s -- %s\n", pthread_self(), firstname, lastname);

        if (estPresent(socket) >= 0) // client déjà loggé
        {
            sprintf(reponse, "LOGIN#ko#Client déjà loggé !");
            return false;
        }
        else
        {
            ResultOrID = CBH_Login(firstname, lastname, PatientId);
            printf("[THREAD %p] ResultOrID = %d\n", pthread_self(), ResultOrID);
            if (ResultOrID == 0)
            {
                printf("[THREAD %p] Connexion patient\n", pthread_self());
                sprintf(reponse, "LOGIN#OK");
                ajoute(socket);
                return true;
            }
            else if (ResultOrID > 0)
            {
                printf("[THREAD %p] Ajout d'un nouveau patient\n", pthread_self());
                sprintf(reponse, "LOGIN#OK#%d", ResultOrID);
                ajoute(socket);
                return true;
            }
            else
            {
                sprintf(reponse, "LOGIN#ko#Mauvais identifiants !");
                return false;
            }
        }
        break;

    case CMD_LOGOUT:
        if (CBH_Logout(socket) == false)
        {

            sprintf(reponse, "LOGOUT#ko#Erreur lors du logout !");
            return false;
        }
        sprintf(reponse, "LOGOUT#ok");
        break;

    case CMD_GET_SPECIALTIES:
        if (CBH_Get_Specialites(reponse) == false)
        {

            return false;
        }
        sprintf(reponse, "GET_SPECIALTIES#ok");
        break;

    case CMD_GET_DOCTORS:
        if (CBH_Get_Doctors(reponse) == false)
        {
            return false;
        }
        sprintf(reponse, "GET_DOCTORS#ok");
        break;

    case CMD_SEARCH_CONSULTATIONS:
        
        strcpy(specialty, strtok(NULL, "#"));
        strcpy(doctor, strtok(NULL, "#"));
        strcpy(startDate, strtok(NULL, "#"));
        strcpy(endDate, strtok(NULL, "#"));
        printf("[THREAD %p] SEARCH_CONSULTATIONS de %s -- %s -- %s -- %s\n", pthread_self(), specialty, doctor, startDate, endDate);
        if (CBH_Search_Consultations(specialty, doctor, startDate, endDate) == false)
        {
            return false;
        }
        sprintf(reponse, "SEARCH_CONSULTATIONS#ok");
        break;

    case CMD_BOOK_CONSULTATION:
        idConsultation = atoi(strtok(NULL, "#"));
        idPatient = atoi(strtok(NULL, "#"));
        printf("[THREAD %p] BOOK_CONSULTATION de %d -- %d\n", pthread_self(), idConsultation, idPatient);
        if (CBH_BOOK_CONSULTATION(idConsultation, idPatient, reponse) == false)
        {
            return false;
        }
        sprintf(reponse, "BOOK_CONSULTATION#ok");
        return true;
        break;
    default:
        sprintf(reponse, "UNKNOWN#ko#Commande inconnue !");
        return false;
        break;
    }

    return false;
}


int CBH_Login(const char *firstname, const char *lastname, const char *PatientId)
{
    if (strcmp(PatientId, "-1") == 0)
    {
        // nouveau patient a crée
        char *birthdate = nullptr;
        int NewID = AddNewPatient(firstname, lastname, birthdate);
        return NewID;
    }
    else
    {
        Patient p = getPatientById(atoi(PatientId));
        printf("[THREAD %p] Patient trouvé : \n\t  -- Id : %d \n\t  --LastName :  %s \n\t  -- FirstName : %s\n\t", pthread_self(), p.id, p.lastname, p.firstname);
        if (strcmp(p.firstname, firstname) == 0 && strcmp(p.lastname, lastname) == 0)
            return 0;
    }

    return -1;
}

bool CBH_Logout(int socket)
{
    printf("[THREAD %p] LOGOUT\n", pthread_self());
    retire(socket);
    return true;
}

void CBH_Close()
{
    pthread_mutex_lock(&mutexClients);
    for (int i = 0; i < nbClients; i++)
        close(clients[i]);
    pthread_mutex_unlock(&mutexClients);
}

bool CBH_Get_Specialites(char *reponse)
{
    return true;
}

bool CBH_Get_Doctors(char *reponse)
{
    return true;
}

bool CBH_Search_Consultations(char *specialiter, char *medecin, char *dateDebut, char *dateFin)
{
    return true;
}

bool CBH_BOOK_CONSULTATION(int idConsultation, int idPatient, char *reponse)
{
    return true;
}

//***** Gestion de l'état du protocole ******************************
int estPresent(int socket)
{
    int indice = -1;
    pthread_mutex_lock(&mutexClients);
    for (int i = 0; i < nbClients; i++)
        if (clients[i] == socket)
        {
            indice = i;
            break;
        }
    pthread_mutex_unlock(&mutexClients);
    return indice;
}

void ajoute(int socket)
{
    pthread_mutex_lock(&mutexClients);
    clients[nbClients] = socket;
    nbClients++;
    pthread_mutex_unlock(&mutexClients);
}

void retire(int socket)
{
    int pos = estPresent(socket);
    if (pos == -1)
        return;
    pthread_mutex_lock(&mutexClients);
    for (int i = pos; i <= nbClients - 2; i++)
        clients[i] = clients[i + 1];
    nbClients--;
    pthread_mutex_unlock(&mutexClients);
}


//***** Fonction de gestions des commandes ******************************
int getCommandReceive(char *ptr)
{

    if (strcmp(ptr, "LOGIN") == 0)
        return CMD_LOGIN;
    else if (strcmp(ptr, "LOGOUT") == 0)
        return CMD_LOGOUT;
    else if (strcmp(ptr, "GET_SPECIALTIES") == 0)
        return CMD_GET_SPECIALTIES;
    else if (strcmp(ptr, "GET_DOCTORS") == 0)
        return CMD_GET_DOCTORS;
    else if (strcmp(ptr, "SEARCH_CONSULTATIONS") == 0)
        return CMD_SEARCH_CONSULTATIONS;
    else if (strcmp(ptr, "BOOK_CONSULTATION") == 0)
        return CMD_BOOK_CONSULTATION;
    else
        return -1;
}
