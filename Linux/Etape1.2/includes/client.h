#ifndef CLIENT_H
#define CLIENT_H

extern int sClient;

void HandlerSIGINT(int s);
void Echange(char *requete, char *reponse);

#endif