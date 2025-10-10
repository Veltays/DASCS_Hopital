cmake_minimum_required(VERSION 3.10)

project(Hopital LANGUAGES CXX)

# === C++ & Qt automations ===
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(CMAKE_AUTOMOC ON)    # Qt MOC auto
set(CMAKE_AUTOUIC OFF)   # Pas de .ui dans le projet
set(CMAKE_AUTORCC ON)

# === Qt ===
find_package(Qt5 REQUIRED COMPONENTS Core Gui Widgets)

# === Dossiers d'include communs ===
set(PROJECT_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/includes)

# === Sources partagés (réseau uniquement) ===
set(SHARED_SOURCES
    sources/TCP.cpp
)

# === Sources serveur (accès BD) ===
set(SERVER_SOURCES
    sources/CBH.cpp
)

# ------------------------------------------------------------
# Hopital (GUI Qt)
# ------------------------------------------------------------
add_executable(Hopital
    sources/main.cpp
    sources/mainwindowclientconsultationbooker.cpp
    ${SHARED_SOURCES}
)

# Headers nécessaires pour AUTOMOC
target_sources(Hopital PRIVATE
    includes/mainwindowclientconsultationbooker.h
    includes/ui_mainwindowclientconsultationbooker.h
)

target_include_directories(Hopital PRIVATE ${PROJECT_INCLUDE_DIR})
target_link_libraries(Hopital
    Qt5::Core
    Qt5::Gui
    Qt5::Widgets
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
    /usr/include/mysql
)

target_link_directories(serveur PRIVATE /usr/lib64/mysql)

target_link_libraries(serveur
    pthread
    mysqlclient
    z
    m
    rt
    ssl
    crypto
    dl
)

# ------------------------------------------------------------
# client (GUI Qt)
# ------------------------------------------------------------
add_executable(client
    sources/main.cpp
    sources/mainwindowclientconsultationbooker.cpp
    ${SHARED_SOURCES}
)

# POUR AUTOMOC
target_sources(client PRIVATE
    includes/mainwindowclientconsultationbooker.h
    includes/ui_mainwindowclientconsultationbooker.h
)

# Empêche AUTOUIC de se déclencher sur le .cpp
set_source_files_properties(
    sources/mainwindowclientconsultationbooker.cpp
    PROPERTIES SKIP_AUTOUIC ON
)

target_include_directories(client PRIVATE ${PROJECT_INCLUDE_DIR})
target_link_libraries(client
    Qt5::Core
    Qt5::Gui
    Qt5::Widgets
    pthread
)

# ------------------------------------------------------------
# CreationBD (outil MySQL)
# ------------------------------------------------------------
add_executable(CreationBD
    sources/CreationBD.cpp
)

target_include_directories(CreationBD PRIVATE
    ${PROJECT_INCLUDE_DIR}
    /usr/include/mysql
)

target_link_directories(CreationBD PRIVATE /usr/lib64/mysql)

target_link_libraries(CreationBD
    mysqlclient
    pthread
    z
    m
    rt
    ssl
    crypto
    dl
)

# ------------------------------------------------------------
# Répertoire de sortie pour tous les binaires
# ------------------------------------------------------------
foreach(tgt IN ITEMS Hopital serveur client CreationBD)
    set_target_properties(${tgt} PROPERTIES
        RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
    )
endforeach()
