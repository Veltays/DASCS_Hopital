#include "mainwindowclientconsultationbooker.h"
#include "ui_mainwindowclientconsultationbooker.h"
#include <QInputDialog>
#include <QMessageBox>
#include <iostream>

#include "TCP.h"
#include "CBH.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include "AccessBD.h"

int TryLogin();
int fetchData();
void Echange(char *requete, char *reponse);

using namespace std;

int sClient;
char myIp[50] = "127.0.0.1";
int portClient = 6767;

MainWindowClientConsultationBooker::MainWindowClientConsultationBooker(QWidget *parent)
    : QMainWindow(parent), ui(new Ui::MainWindowClientConsultationBooker)
{
    ui->setupUi(this);
    logoutOk();

    // Configuration de la table des employes (Personnel Garage)
    ui->tableWidgetConsultations->setColumnCount(5);
    ui->tableWidgetConsultations->setRowCount(0);
    QStringList labelsTableConsultations;
    labelsTableConsultations << "Id"
                             << "Spécialité"
                             << "Médecin"
                             << "Date"
                             << "Heure";
    ui->tableWidgetConsultations->setHorizontalHeaderLabels(labelsTableConsultations);
    ui->tableWidgetConsultations->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableWidgetConsultations->setSelectionBehavior(QAbstractItemView::SelectRows);
    ui->tableWidgetConsultations->setEditTriggers(QAbstractItemView::NoEditTriggers);
    ui->tableWidgetConsultations->horizontalHeader()->setVisible(true);
    ui->tableWidgetConsultations->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetConsultations->verticalHeader()->setVisible(false);
    ui->tableWidgetConsultations->horizontalHeader()->setStyleSheet("background-color: lightyellow");
    int columnWidths[] = {40, 150, 200, 150, 100};
    for (int col = 0; col < 5; ++col)
        ui->tableWidgetConsultations->setColumnWidth(col, columnWidths[col]);
}

MainWindowClientConsultationBooker::~MainWindowClientConsultationBooker()
{
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table des livres encodés (ne pas modifier) ////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::addTupleTableConsultations(int id,
                                                                    string specialty,
                                                                    string doctor,
                                                                    string date,
                                                                    string hour)
{
    int nb = ui->tableWidgetConsultations->rowCount();
    nb++;
    ui->tableWidgetConsultations->setRowCount(nb);
    ui->tableWidgetConsultations->setRowHeight(nb - 1, 10);

    // id
    QTableWidgetItem *item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(id));
    ui->tableWidgetConsultations->setItem(nb - 1, 0, item);

    // specialty
    item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(specialty));
    ui->tableWidgetConsultations->setItem(nb - 1, 1, item);

    // doctor
    item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(doctor));
    ui->tableWidgetConsultations->setItem(nb - 1, 2, item);

    // date
    item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(date));
    ui->tableWidgetConsultations->setItem(nb - 1, 3, item);

    // hour
    item = new QTableWidgetItem;
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(hour));
    ui->tableWidgetConsultations->setItem(nb - 1, 4, item);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::clearTableConsultations()
{
    ui->tableWidgetConsultations->setRowCount(0);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int MainWindowClientConsultationBooker::getSelectionIndexTableConsultations() const
{
    QModelIndexList list = ui->tableWidgetConsultations->selectionModel()->selectedRows();
    if (list.size() == 0)
        return -1;
    QModelIndex index = list.at(0);
    int ind = index.row();
    return ind;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles des comboboxes (ne pas modifier) //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::addComboBoxSpecialties(string specialty)
{
    ui->comboBoxSpecialties->addItem(QString::fromStdString(specialty));
}

string MainWindowClientConsultationBooker::getSelectionSpecialty() const
{
    return ui->comboBoxSpecialties->currentText().toStdString();
}

void MainWindowClientConsultationBooker::clearComboBoxSpecialties()
{
    ui->comboBoxSpecialties->clear();
    this->addComboBoxSpecialties("--- TOUTES ---");
}

void MainWindowClientConsultationBooker::addComboBoxDoctors(string doctor)
{
    ui->comboBoxDoctors->addItem(QString::fromStdString(doctor));
}

string MainWindowClientConsultationBooker::getSelectionDoctor() const
{
    return ui->comboBoxDoctors->currentText().toStdString();
}

void MainWindowClientConsultationBooker::clearComboBoxDoctors()
{
    ui->comboBoxDoctors->clear();
    this->addComboBoxDoctors("--- TOUS ---");
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonction utiles de la fenêtre (ne pas modifier) ////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientConsultationBooker::getLastName() const
{
    return ui->lineEditLastName->text().toStdString();
}

string MainWindowClientConsultationBooker::getFirstName() const
{
    return ui->lineEditFirstName->text().toStdString();
}

int MainWindowClientConsultationBooker::getPatientId() const
{
    return ui->spinBoxId->value();
}

void MainWindowClientConsultationBooker::setLastName(string value)
{
    ui->lineEditLastName->setText(QString::fromStdString(value));
}

string MainWindowClientConsultationBooker::getStartDate() const
{
    return ui->dateEditStartDate->date().toString("yyyy-MM-dd").toStdString();
}

string MainWindowClientConsultationBooker::getEndDate() const
{
    return ui->dateEditEndDate->date().toString("yyyy-MM-dd").toStdString();
}

void MainWindowClientConsultationBooker::setFirstName(string value)
{
    ui->lineEditFirstName->setText(QString::fromStdString(value));
}

void MainWindowClientConsultationBooker::setPatientId(int value)
{
    if (value > 0)
        ui->spinBoxId->setValue(value);
}

bool MainWindowClientConsultationBooker::isNewPatientSelected() const
{
    return ui->checkBoxNewPatient->isChecked();
}

void MainWindowClientConsultationBooker::setNewPatientChecked(bool state)
{
    ui->checkBoxNewPatient->setChecked(state);
}

void MainWindowClientConsultationBooker::setStartDate(string date)
{
    QDate qdate = QDate::fromString(QString::fromStdString(date), "yyyy-MM-dd");
    if (qdate.isValid())
        ui->dateEditStartDate->setDate(qdate);
}

void MainWindowClientConsultationBooker::setEndDate(string date)
{
    QDate qdate = QDate::fromString(QString::fromStdString(date), "yyyy-MM-dd");
    if (qdate.isValid())
        ui->dateEditEndDate->setDate(qdate);
}

void MainWindowClientConsultationBooker::loginOk()
{
    ui->lineEditLastName->setReadOnly(true);
    ui->lineEditFirstName->setReadOnly(true);
    ui->spinBoxId->setReadOnly(true);
    ui->checkBoxNewPatient->setEnabled(false);
    ui->pushButtonLogout->setEnabled(true);
    ui->pushButtonLogin->setEnabled(false);
    ui->pushButtonRechercher->setEnabled(true);
    ui->pushButtonReserver->setEnabled(true);
}

void MainWindowClientConsultationBooker::logoutOk()
{
    ui->lineEditLastName->setReadOnly(false);
    setLastName("");
    ui->lineEditFirstName->setReadOnly(false);
    setFirstName("");
    ui->spinBoxId->setReadOnly(false);
    setPatientId(1);
    ui->checkBoxNewPatient->setEnabled(true);
    setNewPatientChecked(false);
    ui->pushButtonLogout->setEnabled(false);
    ui->pushButtonLogin->setEnabled(true);
    ui->pushButtonRechercher->setEnabled(false);
    ui->pushButtonReserver->setEnabled(false);
    setStartDate("2025-09-15");
    setEndDate("2025-12-31");
    clearComboBoxDoctors();
    clearComboBoxSpecialties();
    clearTableConsultations();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier) ///////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::dialogMessage(const string &title, const string &message)
{
    QMessageBox::information(this, QString::fromStdString(title), QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::dialogError(const string &title, const string &message)
{
    QMessageBox::critical(this, QString::fromStdString(title), QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientConsultationBooker::dialogInputText(const string &title, const string &question)
{
    return QInputDialog::getText(this, QString::fromStdString(title), QString::fromStdString(question)).toStdString();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int MainWindowClientConsultationBooker::dialogInputInt(const string &title, const string &question)
{
    return QInputDialog::getInt(this, QString::fromStdString(title), QString::fromStdString(question));
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions gestion des boutons (TO DO) //////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientConsultationBooker::on_pushButtonLogin_clicked()
{
    char requetes[200], reponse[200];

    // tente une connexion au serveur
    if (TryLogin())
        cout << "[CLIENT] Connexion au serveur reussie" << endl;
    else
        cout << "[CLIENT] Connexion au serveur echouee" << endl;

    string lastName = this->getLastName();
    string firstName = this->getFirstName();
    int patientId = this->getPatientId();
    bool newPatient = this->isNewPatientSelected();

    cout << "[CLIENT] lastName = " << lastName << endl;
    cout << "[CLIENT] FirstName = " << firstName << endl;
    cout << "[CLIENT] patientId = " << patientId << endl;
    cout << "[CLIENT] newPatient = " << newPatient << endl;

    string req;
    if (newPatient == 0)
    {
        req = "LOGIN#" + lastName + "#" + firstName + "#" + to_string(patientId);
    }
    else
    {
        req = "LOGIN#" + lastName + "#" + firstName + "#" + to_string(-1);
    }

    strncpy(requetes, req.c_str(), sizeof(requetes) - 1);

    requetes[sizeof(requetes) - 1] = '\0';
    Echange(requetes, reponse);

    // traite de la réponse
    // cas 1 LOGIN#OK
    char *ptr = strtok(reponse, "#");
    char ResLogin[50];
    char NewId[50];

    strcpy(ResLogin, strtok(NULL, "#"));
    printf("[CLIENT] ResLogin = %s\n", ResLogin);

    if (strcmp(ResLogin, "ko") == 0)
    {
        strcpy(NewId, strtok(NULL, "#"));
        printf("[CLIENT] Nouvelle ID = %s\n", NewId);
        // ptr vaut le NOUVEAU_ID ou le MESSAGE d'erreur
        string message = NewId;
        dialogError("Erreur de login", message);
        return;
    }
    else if (strcmp(ResLogin, "OK") == 0)
    {

        printf("[CLIENT] Login OK\n");
        if (newPatient == 1)
        {
            strcpy(NewId, strtok(NULL, "#"));
            printf("[CLIENT] Nouvelle ID = %s\n", NewId);
            // ptr vaut le NOUVEAU_ID ou le MESSAGE d'erreur
            // nouveau patient, on affiche la nouvelle ID
            printf("[CLIENT] Nouveau patient, nouvelle ID = %s\n", NewId);
            int NewIDInt = atoi(NewId);
            this->setPatientId(NewIDInt);
            string message = "Bienvenue ! Votre identifiant patient est : " + to_string(NewIDInt);
            dialogMessage("Login OK", message);
        }
        dialogMessage("Login OK", "Bienvenue " + firstName + " " + lastName + " !");
        loginOk();

        fetchSpecialties();
        fetchDoctors();
    }
    else
    {
        string message = "Reponse du serveur inconnue !";
        dialogError("Erreur de login", message);
        return;
    }
}

int MainWindowClientConsultationBooker::fetchSpecialties()
{
    char reponse[200];
    char requete[200];
    string req = "GET_SPECIALTIES";
    strncpy(requete, req.c_str(), sizeof(requete) - 1);

    requete[sizeof(requete) - 1] = '\0';
    Echange(requete, reponse);

    char *token;
    int compteur = 0;

    token = strtok(reponse, "#");
    while (token != NULL)
    {
        compteur++;
        // Les spécialités sont aux positions paires : 2, 4, 6, ...
        if (compteur % 2 == 0)
        {
            this->addComboBoxSpecialties(token);
            printf("Spécialité : %s\n", token); // pour test
        }
        token = strtok(NULL, "#");
    }

    return 0;
}

int MainWindowClientConsultationBooker::fetchDoctors()
{
    char reponse[200];
    char requete[200];
    string req = "GET_DOCTORS";
    strncpy(requete, req.c_str(), sizeof(requete) - 1);

    requete[sizeof(requete) - 1] = '\0';
    Echange(requete, reponse);

    char *token;
    int compteur = 0;
    char lastname[50];
    char firstname[50];

    token = strtok(reponse, "#");
    while (token != NULL)
    {
        compteur++;
        
        if (compteur % 3 == 2)
        {
            strcpy(firstname, token);
        }
        else if (compteur % 3 == 0)
        {
            strcpy(lastname, token);

            char fullname[100];
            sprintf(fullname, "%s %s", firstname, lastname);

            this->addComboBoxDoctors(fullname);
        }

        token = strtok(NULL,"#");
    }
        return 0;
}

int TryLogin()
{

    if ((sClient = ClientSocket(myIp, portClient)) == -1)
    {
        perror("[CLIENT] Erreur de ClientSocket");
        exit(1);
    }

    return true;
}

void MainWindowClientConsultationBooker::on_pushButtonLogout_clicked()
{
    string req;
    char requetes[200], reponse[200];
    req = "LOGOUT#";
    strncpy(requetes, req.c_str(), sizeof(requetes) - 1);

    Echange(requetes, reponse);

    logoutOk();
}

void MainWindowClientConsultationBooker::on_pushButtonRechercher_clicked()
{
    string specialty = this->getSelectionSpecialty();
    string doctor = this->getSelectionDoctor();
    string startDate = this->getStartDate();
    string endDate = this->getEndDate();

    cout << "[CLIENT] specialty = " << specialty << endl;
    cout << "[CLIENT] doctor = " << doctor << endl;
    cout << "[CLIENT] startDate = " << startDate << endl;
    cout << "[CLIENT] endDate = " << endDate << endl;

    string requete;

    requete = "SEARCH_CONSULTATIONS" + "#" + specialty + "#" + doctor + "#" + startDate + "#" + endDate;

    strncpy(requetes, req.c_str(), sizeof(requetes) - 1);

    requetes[sizeof(requetes) - 1] = '\0';
    

    Echange(requete,reponse);



}

void MainWindowClientConsultationBooker::on_pushButtonReserver_clicked()
{
    int selectedRow = this->getSelectionIndexTableConsultations();

    cout << "[CLIENT] selectedRow = " << selectedRow << endl;
}

void Echange(char *requete, char *reponse)
{

    int nbEcrits, nbLus;

    // ***** Envoi de la requete ****************************
    if ((nbEcrits = Send(sClient, requete, strlen(requete))) == -1)
    {
        perror("[CLIENT] Erreur de Send");
        close(sClient);
        exit(1);
    }
    else
    {
        cout << "[CLIENT] Requete envoyee = " << requete << endl;
    }

    // ***** Attente de la reponse **************************
    if ((nbLus = Receive(sClient, reponse)) < 0)
    {
        perror("[CLIENT] Erreur de Receive");
        close(sClient);
        exit(1);
    }
    else
    {
        cout << "[CLIENT] Reponse recue = " << reponse << endl;
    }
}