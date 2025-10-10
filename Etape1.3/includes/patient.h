#ifndef PATIENT_H
#define PATIENT_H

typedef struct Patient {
    char* firstname;
    char* lastname;
    char* birthdate; // peut être nullptr si inconnu
} Patient;


#endif