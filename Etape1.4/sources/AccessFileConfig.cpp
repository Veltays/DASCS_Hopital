#include <cstdio>
#include <cstring>
#include "AccessFileConfig.h"

bool getConfigValue(const char *key, char *value)
{
    FILE *f = fopen("/home/student/Bureau/DASCS_Hopital/Etape1.4/ressources/networkConfig.config", "r");
    if (!f)
    {
        perror("config");
        return false;
    }

    char line[128];
    while (fgets(line, sizeof(line), f))
    {
        // Couper au ';'
        char *part = strtok(line, ";");
        if (!part)
            continue;

        // Couper à '='
        char *keyR = strtok(part, "=");
        char *valueR = strtok(NULL, "=");

        if (!keyR || !valueR)
            continue;

        // Enlever espaces avant/après
        while (*keyR == ' ' || *keyR == '\t')
            keyR++;
        while (*valueR == ' ' || *valueR == '\t')
            valueR++;

        if (strcmp(keyR, key) == 0)
        {
            strcpy(value, valueR);
            fclose(f);
            return true;
        }
    }

    fclose(f);
    printf("clé %s non trouvée\n", key);


    return false;
}
