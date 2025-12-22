USE PourStudent;
CREATE TABLE report
(
    id        int PRIMARY KEY,
    idPatient int,
    idMedecin int,
    date      DATE,
    description VARCHAR(255)
);

ALTER TABLE report
    ADD CONSTRAINT FK_REPORT_PATIENT FOREIGN KEY (idPatient) REFERENCES patients(id);
ALTER TABLE  report
    ADD CONSTRAINT FK_REPORT_MEDECIN FOREIGN KEY (idMedecin) REFERENCES doctors(id);

