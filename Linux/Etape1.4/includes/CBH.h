#ifndef CBH_H
#define CBH_H



#include "patient.h"



bool CBH(char* requete, char* reponse,int socket);
int CBH_Login(const char* firstname,const char* lastname,const char* PatientId);
bool CBH_Logout(int socket);

int estPresent(int socket);
void ajoute(int socket, const char* nom, const char* prenom, int idPatient);
void retire(int socket);
int getIdPatientFromSocket(int socket);


bool CBH_Get_Specialites(char* reponse);
bool CBH_Get_Doctors(char* reponse);
bool CBH_Search_Consultations(char* buffer, char* specialiter,char* medecin,char* dateDebut, char*dateFin);
bool CBH_BOOK_CONSULTATION(int idConsultation,char *raison,int idPatient,char* reponse);
void CBH_Close();


int getCommandReceive(char* ptr);


#endif // CBH_H