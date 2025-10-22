cmake_minimum_required(VERSION 3.10)

project(Hopital LANGUAGES CXX)

# === C++ & Qt automations ===
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC OFF)
set(CMAKE_AUTORCC ON)

# === Qt ===
find_package(Qt5 REQUIRED COMPONENTS Core Gui Widgets)

# === Dossiers d'include communs ===
set(PROJECT_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/includes)

# === MySQL (adapté à ta VM) ===
set(MYSQL_INCLUDE_DIR /usr/include/mysql)
set(MYSQL_LIB_DIR     /usr/lib64/mysql)
set(MYSQL_LIBS        mysqlclient pthread z m rt ssl crypto dl)

# === Sources partagées (réseau uniquement) ===
set(SHARED_SOURCES
    sources/TCP.cpp
    sources/AccessFileConfig.cpp  
)

# === Sources serveur (accès BD) ===
set(SERVER_SOURCES
    sources/CBH.cpp
    sources/AccessBD.cpp
)

# ------------------------------------------------------------
# client (GUI Qt) 
# ------------------------------------------------------------
add_executable(client
    sources/main.cpp
    sources/mainwindowclientconsultationbooker.cpp
    ${SHARED_SOURCES}
)

target_sources(client PRIVATE
    includes/mainwindowclientconsultationbooker.h
    includes/ui_mainwindowclientconsultationbooker.h
)

set_source_files_properties(
    sources/mainwindowclientconsultationbooker.cpp
    PROPERTIES SKIP_AUTOUIC ON
)

target_include_directories(client PRIVATE ${PROJECT_INCLUDE_DIR})
target_link_libraries(client
    Qt5::Core Qt5::Gui Qt5::Widgets
    pthread
)

# ------------------------------------------------------------
# serveur (console)
# ------------------------------------------------------------
add_executable(serveur
    sources/serveur_Reservation.cpp
    ${SHARED_SOURCES}
    ${SERVER_SOURCES}
)

target_include_directories(serveur PRIVATE
    ${PROJECT_INCLUDE_DIR}
    ${MYSQL_INCLUDE_DIR}
)
target_link_directories(serveur PRIVATE ${MYSQL_LIB_DIR})
target_link_libraries(serveur ${MYSQL_LIBS})

# ------------------------------------------------------------
# CreationBD (outil MySQL)
# ------------------------------------------------------------
add_executable(CreationBD
    sources/CreationBD.cpp
)

target_include_directories(CreationBD PRIVATE
    ${PROJECT_INCLUDE_DIR}
    ${MYSQL_INCLUDE_DIR}
)
target_link_directories(CreationBD PRIVATE ${MYSQL_LIB_DIR})
target_link_libraries(CreationBD ${MYSQL_LIBS})

# ------------------------------------------------------------
# Répertoire de sortie pour tous les binaires
# ------------------------------------------------------------
foreach(tgt IN ITEMS client serveur CreationBD)
    set_target_properties(${tgt} PROPERTIES
        RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
    )
endforeach()