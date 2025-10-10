#include <stdio.h>
#include <stdlib.h>
#include <mysql.h>



int estPresent(const char * nom);


// Retourne un Patient* alloué dynamiquement (à libérer) ou nullptr.
Patient* getPatientById(int id);

// Crée un patient, retourne le nouvel ID (>0) ou -1 en cas d’erreur.
int AddNewPatient(const char* firstname, const char* lastname, const char* birthdate);