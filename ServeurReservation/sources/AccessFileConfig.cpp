#include <cstdio>
#include <cstring>
#include "AccessFileConfig.h"

bool getConfigValue(const char *key, char *value)
{
    FILE *f = fopen("ressources/networkConfig.config", "r");
    if (!f)
    {
        perror("config");
        return false;
    }

    char line[128];
     printf("-------------------------\n");
    while (fgets(line, sizeof(line), f))
    {
        printf("[GETCONFIGVALUE] Appel de GetConfigValue pour récupérer la clé %s\n\n", key);
        printf("[GETCONFIGVALUE] ligne lue: %s", line);

        // Couper au ';'
        char *part = strtok(line, ";");
        if (!part)
            return false;

    
        // Couper à '='
        char *keyR = strtok(part, "=");
        char *valueR = strtok(NULL, "=");

        if (!keyR || !valueR)
            return false;

        printf("[GETCONFIGVALUE] partie avant '=' : %s\n", keyR);
        printf("[GETCONFIGVALUE] partie après '=' : %s\n", valueR);

        cleanLine(keyR,valueR);


        printf("[GETCONFIGVALUE] Comparaison entre '%s' et '%s'\n", keyR, key);

        if (strcmp(keyR, key) == 0)
        {
            strcpy(value, valueR);
            fclose(f);
            printf("[GETCONFIGVALUE] clé %s trouvée, valeur = %s\n", key, value);
            return true;
        }
        printf("[GETCONFIGVALUE] Clé %s non correspondante\n", keyR);
    }

    fclose(f);
    printf("[GETCONFIGVALUE] clé %s non trouvée\n", key);

    return false;
}





void cleanLine(char *key, char *value)
{
    // retirer les espaces autour de la clé
    char *end;

    // retirer les espaces au début
    while (*key == ' ' || *key == '\t')
        key++;

    // retirer les espaces à la fin
    end = key + strlen(key) - 1;
    while (end > key && (*end == ' ' || *end == '\t'))
        end--;
    *(end + 1) = '\0';

    // retirer les espaces autour de la valeur
    while (*value == ' ' || *value == '\t')
        value++;

    end = value + strlen(value) - 1;
    while (end > value && (*end == ' ' || *end == '\t'))
        end--;
    *(end + 1) = '\0';



        printf("[GETCONFIGVALUE] clé traitée: '%s'\n", key);
        printf("[GETCONFIGVALUE] valeur traitée: '%s'\n", value);
}