#ifndef ACCESSBD_H
#define ACCESSBD_H
#include "patient.h"
#include <mysql/mysql.h>

int estPresent(const char * nom);

MYSQL *connectToDatabase(void);



int getLastIdOfPatient();


Patient getPatientById(int id);

// Crée un patient, retourne le nouvel ID (>0) ou -1 en cas d’erreur.
int AddNewPatient(const char* firstname, const char* lastname, const char* birthdate);

#endif 