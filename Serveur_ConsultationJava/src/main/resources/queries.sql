-- ================================================================
-- AJOUT D’UNE TABLE USERS POUR L’AUTHENTIFICATION
-- ================================================================

CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     login VARCHAR(50) UNIQUE NOT NULL,
                                     password VARCHAR(255) NULL,         -- ✅ autorise NULL pour première connexion
                                     role ENUM('doctor', 'admin', 'patient') DEFAULT 'doctor'
);

-- ================================================================
-- 🔗 AJOUT DE LA COLONNE user_id DANS doctors
-- ================================================================

-- 1️⃣ Vérifie si la colonne existe déjà
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'doctors' AND COLUMN_NAME = 'user_id'
);

-- 2️⃣ Si elle n’existe pas, on l’ajoute
SET @sql = IF(@col_exists = 0, 'ALTER TABLE doctors ADD COLUMN user_id INT;', 'SELECT "Colonne déjà existante";');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ================================================================
-- AJOUT DE LA CONTRAINTE DE CLÉ ÉTRANGÈRE
-- ================================================================

ALTER TABLE doctors
    ADD CONSTRAINT fk_doctor_user FOREIGN KEY (user_id) REFERENCES users(id);

-- ================================================================
-- INSERTION D'UN UTILISATEUR POUR CHAQUE MÉDECIN EXISTANT
-- ================================================================

INSERT INTO users (login, password, role)
SELECT
    UPPER(CONCAT(LEFT(last_name, 3), LEFT(first_name, 3))) AS login,  -- ex: DUPALI
    NULL AS password,                                                 -- ✅ mot de passe NULL
    'doctor' AS role
FROM doctors
WHERE UPPER(CONCAT(LEFT(last_name, 3), LEFT(first_name, 3))) NOT IN (SELECT login FROM users);

-- ================================================================
-- MISE À JOUR DE LA TABLE DOCTORS POUR LES LIER AUX USERS
-- ================================================================

UPDATE doctors
SET user_id = (
    SELECT id
    FROM users
    WHERE login = UPPER(CONCAT(LEFT(doctors.last_name, 3), LEFT(doctors.first_name, 3)))
)
WHERE EXISTS (
    SELECT 1
    FROM users
    WHERE login = UPPER(CONCAT(LEFT(doctors.last_name, 3), LEFT(doctors.first_name, 3)))
);

-- ================================================================
-- VERIFICATION DU RÉSULTAT
-- ================================================================

SELECT d.id AS doctor_id,
       d.last_name,
       d.first_name,
       u.login,
       u.password,
       u.role
FROM doctors d
         LEFT JOIN users u ON d.user_id = u.id
ORDER BY d.id;
