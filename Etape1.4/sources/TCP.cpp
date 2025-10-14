#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h> // pour memset
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "TCP.h"

char host[NI_MAXHOST];
char port[NI_MAXSERV];
int sEcoute;

#pragma region CreationSocket

int CreationSocket()
{

    printf("[TCP] pid = %d\n", getpid());
    // Creation de la socket
    if ((sEcoute = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("[TCP] Erreur de socket()");
        exit(1);
    }

    int opt = 1;
    // Active SO_REUSEADDR pour pouvoir réutiliser le port rapidement   --> permet d'éviter l'erreur "Address already in use" lors du redémarrage rapide du serveur
    if (setsockopt(sEcoute, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt)) < 0)
    {
        perror("[TCP] setsockopt");
        exit(EXIT_FAILURE);
    }

    printf("[TCP] Le socket a ete cree = %d\n", sEcoute);
    return sEcoute;
}

struct addrinfo *creationAdresse(int portSer)
{
    struct addrinfo hints;
    struct addrinfo *results = NULL;

    // convertir le port en chaîne numérique (AI_NUMERICSERV l'exige)
    char portStr[16];
    snprintf(portStr, sizeof(portStr), "%d", portSer);

    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_INET;                    // IPv4
    hints.ai_socktype = SOCK_STREAM;              // TCP
    hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV; // écoute sur toutes interfaces, port numérique

    int ret = getaddrinfo(NULL, portStr, &hints, &results);
    if (ret != 0)
    {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(ret));
        exit(1);
    }

    // Affichage IP/port sous forme numérique

    if (getnameinfo(results->ai_addr, results->ai_addrlen,
                    host, sizeof(host),
                    port, sizeof(port),
                    NI_NUMERICHOST | NI_NUMERICSERV) == 0)
    {
        printf("[TCP] \n\t  -- Mon Adresse IP: %s \n\t  -- Mon Port: %s\n", host, port);
    }

    return results; // à libérer plus tard avec freeaddrinfo(results)
}

void bindServeurToSocket(int sEcoute, struct addrinfo *results)
{

    if (bind(sEcoute, results->ai_addr, results->ai_addrlen) < 0)
    {
        perror("[TCP] Erreur de bind()");
        exit(1);
    }
    freeaddrinfo(results);
    printf("[TCP] bind() reussi !\n");
}

void ListenOnSocket(int sEcoute)
{

    // Mise à l'écoute de la socket
    if (listen(sEcoute, SOMAXCONN) == -1)
    {
        perror("Erreur de listen()");
        exit(1);
    }
    printf("[TCP] listen() reussi !\n");
}

int ServerSocket(int portSer)
{
    sEcoute = CreationSocket();

    struct addrinfo *results = creationAdresse(portSer);

    bindServeurToSocket(sEcoute, results);

    ListenOnSocket(sEcoute);

    return sEcoute;
}

#pragma endregion CreationSocket

#pragma region Accept

int Accept(int sEcoute, char *ipClient)
{

    int sService = AcceptClient(sEcoute);

    GetClientIP(sService, ipClient);

    return sService;
}

int AcceptClient(int sEcoute)
{
    int sService;
    if ((sService = accept(sEcoute, NULL, NULL)) == -1)
    {
        perror("[TCP] Erreur de accept()");
        exit(1);
    }
    printf("[TCP] accept() reussi !\n");
    printf("[TCP] socket de service = %d\n", sService);

    return sService;
}

void GetClientIP(int sService, char *ipClient)
{

    struct sockaddr_in adrClient;
    socklen_t adrClientLen = sizeof(struct sockaddr_in); // nécessaire
    getpeername(sService, (struct sockaddr *)&adrClient, &adrClientLen);
    getnameinfo((struct sockaddr *)&adrClient, adrClientLen, host, NI_MAXHOST, port, NI_MAXSERV, NI_NUMERICSERV | NI_NUMERICHOST);
    printf("[TCP] Client connecte \n\t  -- Adresse IP: %s \n\t  -- Port: %s\n", host, port);
}

#pragma endregion Accept

#pragma region ClientSocket

int CreationSocketClient(char *port)
{

    // Création de la socket
    int sClient;
    if ((sClient = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("[TCP] Erreur de socket()");
        return -1;
    }
    return sClient;
}

struct addrinfo *ConstructionAdresseClient(char *ipServeur, char *port)
{
    struct addrinfo hints;
    struct addrinfo *results;

    memset(&hints, 0, sizeof(struct addrinfo));
    hints.ai_family = AF_INET;       // IPv4
    hints.ai_socktype = SOCK_STREAM; // TCP
    hints.ai_flags = AI_NUMERICSERV; // port numérique

    if (getaddrinfo(ipServeur, port, &hints, &results) != 0)
    {
        perror("[TCP] Erreur de getaddrinfo()");
    }

    return results;
}

void ConnectToServer(int sClient, struct addrinfo *results)
{
    // Demande de connexion
    if (connect(sClient, results->ai_addr, results->ai_addrlen) == -1)
    {
        perror("[TCP] Erreur de connect()");
        close(sClient);
    
    }
}

int ClientSocket(char *ipServeur, int portServeur)
{

    char port[10];

    // convertir port en chaîne
    sprintf(port, "%d", portServeur);

    int sClient = CreationSocketClient(port);

    struct addrinfo *results = ConstructionAdresseClient(ipServeur, port);

    ConnectToServer(sClient, results);

    freeaddrinfo(results);

    return sClient; // socket connectée prête à l’emploi
}

#pragma endregion ClientSocket

#pragma region Send

int Send(int sSocket, char *data, int taille)
{

    printf("[TCP] Send() called with taille=%d\n", taille);
    printf("[TCP] -- Data to send: \n\t -- %d \n\t -- %s\n", taille, data);

    // Concatenation taille + données
    // taille ||
    // 0000   || data
    char buffer[9999]; // 4 chiffres

    sprintf(buffer, "%04d", taille);
    printf("[TCP] Taille de la chaine : %s\n", buffer); // affiche 0013

    strcpy(buffer + 4, data);

    int nbEcrits = write(sSocket, buffer, 4 + taille); // envoi de la taille + données

    printf("[TCP] Write returned %d\n", nbEcrits);
    
    if (nbEcrits != 4 + taille)
    {
        perror("[TCP] Erreur d’envoi des données");
        return -1;
    }

    printf("[TCP] Send() \n\t -- taille = %d, \n\t -- envoyés = %d, \n\t -- data = %s-- \n", taille, nbEcrits,data);
    return nbEcrits;
}

#pragma endregion Send

#pragma region Receive

int Receive(int sSocket, char *data)
{
    printf("[TCP] Receive() called on socket %d\n", sSocket);

    char tailleStr[5]; // 4 chiffres

    int n = read(sSocket, tailleStr, 4);

    printf("[TCP] Read returned %d\n", n);

    tailleStr[4] = '\0';

    printf("[TCP] Received tailleStr: %s\n", tailleStr);

    int taille = atoi(tailleStr);

    read(sSocket, data, taille);

    data[taille] = '\0'; // ajout du \0
    //affiche les data lu

    printf("[TCP] Data received: %s\n", data);

    return taille;

}

#pragma endregion Receive