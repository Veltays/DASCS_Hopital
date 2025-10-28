cmake_minimum_required(VERSION 3.10)
project(Hopital LANGUAGES CXX)

# --- C++ ---
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# --- Qt ---
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC OFF)   # tu utilises un ui_*.h manuel
set(CMAKE_AUTORCC ON)
find_package(Qt5 REQUIRED COMPONENTS Core Gui Widgets)

# --- Includes communs ---
set(PROJECT_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/includes)

# --- MySQL (adapter si besoin) ---
set(MYSQL_INCLUDE_DIR /usr/include/mysql)
set(MYSQL_LIB_DIR     /usr/lib64/mysql)
set(MYSQL_LIBS        mysqlclient pthread z m rt ssl crypto dl)

# --- Sortie des binaires ---
set(BIN_DIR ${CMAKE_BINARY_DIR}/bin)

# --- Sources partagées ---
set(SHARED_SOURCES
    sources/TCP.cpp
    sources/AccessFileConfig.cpp
)

# =========================
#   client (GUI Qt)
# =========================
add_executable(client
    sources/main.cpp
    sources/mainwindowclientconsultationbooker.cpp
    ${SHARED_SOURCES}

    # >>> IMPORTANT : lister les headers avec Q_OBJECT pour AUTOMOC <<<
    includes/mainwindowclientconsultationbooker.h
    includes/ui_mainwindowclientconsultationbooker.h
)

# (Pas d'UI .ui : on garde AUTOUIC OFF)
# Si jamais tu avais un .ui : enlève ceci et laisse AUTOUIC ON par défaut.

target_include_directories(client PRIVATE ${PROJECT_INCLUDE_DIR})
target_link_libraries(client PRIVATE Qt5::Core Qt5::Gui Qt5::Widgets pthread)
set_target_properties(client PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${BIN_DIR}")

# =========================
#   serveur (console)
# =========================
add_executable(serveur
    sources/serveur_Reservation.cpp
    ${SHARED_SOURCES}
    sources/CBH.cpp
    sources/AccessBD.cpp
)

target_include_directories(serveur PRIVATE ${PROJECT_INCLUDE_DIR} ${MYSQL_INCLUDE_DIR})
target_link_directories(serveur PRIVATE ${MYSQL_LIB_DIR})
target_link_libraries(serveur PRIVATE ${MYSQL_LIBS})
set_target_properties(serveur PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${BIN_DIR}")

# =========================
#   CreationBD (outil)
# =========================
add_executable(CreationBD
    sources/CreationBD.cpp
)

target_include_directories(CreationBD PRIVATE ${PROJECT_INCLUDE_DIR} ${MYSQL_INCLUDE_DIR})
target_link_directories(CreationBD PRIVATE ${MYSQL_LIB_DIR})
target_link_libraries(CreationBD PRIVATE ${MYSQL_LIBS})
set_target_properties(CreationBD PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${BIN_DIR}")

# =========================
#   Copie des ressources (Solution 1)
# =========================
add_custom_target(copy_resources ALL
    COMMAND ${CMAKE_COMMAND} -E copy_directory
            ${CMAKE_SOURCE_DIR}/ressources
            ${BIN_DIR}/ressources
    COMMENT "Copie du dossier ressources/ vers bin/ressources/"
)

add_dependencies(client copy_resources)
add_dependencies(serveur copy_resources)
add_dependencies(CreationBD copy_resources)
