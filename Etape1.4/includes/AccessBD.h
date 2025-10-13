#ifndef ACCESSBD_H
#define ACCESSBD_H
#include "patient.h"
#include <cstring>
#include <mysql/mysql.h>

int estPresent(const char * nom);

MYSQL *connectToDatabase(void);

int getLastIdOfPatient();

Patient getPatientById(int id);

// Crée un patient, retourne le nouvel ID (>0) ou -1 en cas d’erreur.
int AddNewPatient(const char* firstname, const char* lastname, const char* birthdate);

bool retourSpecialite(char * r);

bool retourDocteur(char *r );

bool retourConsultations(char * r,char* specialty,char* doctor, char * startDate, char* endDate);

bool modifyConsultation(char * reponse, int idConsultation, char * raison, int idPatient);

#endif