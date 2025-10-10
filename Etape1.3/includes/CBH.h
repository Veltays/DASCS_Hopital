#ifndef CBH_H
#define CBH_H
#define NB_MAX_CLIENTS 20


typedef struct Patient {
    const char* firstname;
    const char* lastname;
    const char* birthdate; // ou nullptr si inconnu
} Patient;

bool CBH(char* requete, char* reponse,int socket);
int CBH_Login(const char* firstname,const char* lastname,const char* PatientId);
bool CBH_Logout(int socket);

int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);

bool CBH_Get_Specialites(char* reponse);
bool CBH_Get_Doctors(char* reponse);
bool CBH_Search_Consultations(char* specialiter,char* medecin,char* dateDebut, char*dateFin);
bool CBH_BOOK_CONSULTATION(int idConsultation,int idPatient,char* reponse);
void CBH_Close();


#endif // CBH_H