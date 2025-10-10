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




bool CBH(char *requete, char *reponse, int socket)
{
    // ***** Récupération nom de la requete *****************
    char *ptr = strtok(requete, "#");

    // ***** LOGIN ******************************************
    if (strcmp(ptr, "LOGIN") == 0)
    {
        char firstname[50], lastname[50], PatientId[50];
        strcpy(firstname, strtok(NULL, "#"));
        strcpy(lastname, strtok(NULL, "#"));
        strcpy(PatientId, strtok(NULL, "#"));
        printf("\t[THREAD %p] LOGIN de %s -- %s\n", pthread_self(), firstname, lastname);
        if (estPresent(socket) >= 0) // client déjà loggé
        {
            sprintf(reponse, "LOGIN#ko#Client déjà loggé !");
            return false;
        }
        else
        {
            int ResultOrID = CBH_Login(firstname, lastname, PatientId);
            if (ResultOrID > 0)
            {
                if (ResultOrID == 0)
                {
                    sprintf(reponse, "LOGIN#OK");
                    ajoute(socket);
                }
                else if(ResultOrID > 0)
                {
                    sprintf(reponse, "LOGIN#OK#%d", ResultOrID);
                    ajoute(socket);
                }
                else
                {
                    sprintf(reponse, "LOGIN#ko#Mauvais identifiants !");
                    return false;
                }
            }
        }
    }

    // ***** LOGOUT *****************************************
    if (strcmp(ptr, "LOGOUT") == 0)
    {
        if (CBH_Logout(socket) == false)
        {

            return false;
        }
        sprintf(reponse, "LOGOUT#ok");
    }

    // ***** GET_SPECIALTIES ********************************
    if (strcmp(ptr, "GET_SPECIALTIES") == 0)
    {
        if (CBH_Get_Specialites(reponse) == false)
        {

            return false;
        }
        sprintf(reponse, "GET_SPECIALTIES#ok");
    }

    // ***** GET_DOCTORS ************************************
    if (strcmp(ptr, "GET_DOCTORS") == 0)
    {
        if (CBH_Get_Doctors(reponse) == false)
        {
            return false;
        }
        sprintf(reponse, "GET_DOCTORS#ok");
    }

    // ***** SEARCH_CONSULTATIONS ****************************
    if (strcmp(ptr, "SEARCH_CONSULTATIONS") == 0)
    {

        char specialty[50], doctor[50], startDate[50], endDate[50];
        strcpy(specialty, strtok(NULL, "#"));
        strcpy(doctor, strtok(NULL, "#"));
        strcpy(startDate, strtok(NULL, "#"));
        strcpy(endDate, strtok(NULL, "#"));
        printf("\t[THREAD %p] SEARCH_CONSULTATIONS de %s -- %s -- %s -- %s\n", pthread_self(), specialty, doctor, startDate, endDate);
        if (CBH_Search_Consultations(specialty, doctor, startDate, endDate) == false)
        {
            return false;
        }
        sprintf(reponse, "SEARCH_CONSULTATIONS#ok");
    }

    // ***** BOOK_CONSULTATION *******************************
    if (strcmp(ptr, "BOOK_CONSULTATION") == 0)
    {
        int idConsultation = atoi(strtok(NULL, "#"));
        int idPatient = atoi(strtok(NULL, "#"));
        printf("\t[THREAD %p] BOOK_CONSULTATION de %d -- %d\n", pthread_self(), idConsultation, idPatient);
        if (CBH_BOOK_CONSULTATION(idConsultation, idPatient, reponse) == false)
        {
            return false;
        }
        sprintf(reponse, "BOOK_CONSULTATION#ok");
        return true;
    }
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
        const Patient* p = getPatientById(atoi(PatientId));
        if (!p) return -1;
        if (strcmp(p->firstname, firstname) == 0 && strcmp(p->lastname, lastname) == 0)
            return 0;
        else
            return -1;

    }

    return -1;
}

bool CBH_Logout(int socket)
{
    printf("\t[THREAD %p] LOGOUT\n", pthread_self());
    retire(socket);
    return false;
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