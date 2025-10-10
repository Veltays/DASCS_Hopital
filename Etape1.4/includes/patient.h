#ifndef PATIENT_H
#define PATIENT_H


typedef struct {
    int  id;
    char lastname[64];
    char firstname[64];
    char birthdate[11]; // "YYYY-MM-DD" + '\0'
} Patient;



#endif