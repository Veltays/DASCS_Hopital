#ifndef CBP_H
#define CBP_H



#include "patient.h"



bool CBP(char* requete, char* reponse,int socket);
int CBP_Login(const char* firstname,const char* lastname,const char* PatientId);
bool CBP_Logout(int socket);

int estPresent(int socket);
void ajoute(int socket, const char* nom, const char* prenom, int idPatient);
void retire(int socket);
int getIdPatientFromSocket(int socket);


bool CBP_Get_Specialites(char* reponse);
bool CBP_Get_Doctors(char* reponse);
bool CBP_Search_Consultations(char* buffer, char* specialiter,char* medecin,char* dateDebut, char*dateFin);
bool CBP_BOOK_CONSULTATION(int idConsultation,char *raison,int idPatient,char* reponse);
void CBP_Close();


int getCommandReceiveCBP(char* ptr);


#endif // CBP_H