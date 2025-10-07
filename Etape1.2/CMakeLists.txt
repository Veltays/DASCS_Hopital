cmake_minimum_required(VERSION 3.10)

project(Hopital CXX)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Qt
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC ON)   # activé globalement (mais on le désactive pour ce .cpp précis plus bas)
set(CMAKE_AUTORCC ON)

find_package(Qt5 COMPONENTS Core Gui Widgets REQUIRED)

# Include communs
include_directories(
    ${CMAKE_SOURCE_DIR}/includes
)

# ================== Sources communs (sans main()) ==================
set(COMMON_SOURCES
    sources/TCP.cpp
    sources/CBH.cpp
)

# ================== Hopital (GUI Qt) ==================
add_executable(Hopital
    sources/main.cpp
    sources/mainwindowclientconsultationbooker.cpp
    ${COMMON_SOURCES}
)

# IMPORTANT : lister les headers pour AUTOMOC (classe Qt + ui_* existant)
target_sources(Hopital PRIVATE
    includes/mainwindowclientconsultationbooker.h
    includes/ui_mainwindowclientconsultationbooker.h
)

# Tu n'as pas de .ui -> ne laisse pas AutoUic le chercher pour ce .cpp
set_source_files_properties(
    sources/mainwindowclientconsultationbooker.cpp
    PROPERTIES SKIP_AUTOUIC ON
)

target_link_libraries(Hopital
    Qt5::Core
    Qt5::Gui
    Qt5::Widgets
)

# ================== Serveur (console) ==================
add_executable(serveur
    sources/serveur.cpp
    ${COMMON_SOURCES}
)
target_link_libraries(serveur pthread)

# ================== Client (console) ==================
add_executable(client
    sources/Client.cpp
    ${COMMON_SOURCES}
)
target_link_libraries(client pthread)

# ================== CreationBD (outil MySQL) ==================
add_executable(CreationBD
    sources/CreationBD.cpp
)
target_include_directories(CreationBD PRIVATE /usr/include/mysql)
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

# ================== Sortie des binaires ==================
foreach(tgt IN ITEMS Hopital serveur client CreationBD)
    set_target_properties(${tgt} PROPERTIES
        RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
    )
endforeach()
