package ServeurGeneriqueTCP.model.dao;


import ServeurGeneriqueTCP.model.entity.Patient;
import ServeurGeneriqueTCP.model.viewmodel.PatientSearchVM;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;



public class PatientDAO {

    private final ConnectDB connectDB;
    private final ArrayList<Patient> PatientsList;

    public PatientDAO()
    {
        connectDB = new ConnectDB();
        PatientsList = new ArrayList<>();
    }

    public ArrayList<Patient> getList()
    {
        return PatientsList;
    }

    public Patient getById(Integer id)
    {
        for (Patient entity : PatientsList) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Patient> load(PatientSearchVM patientSearchVMParameter) throws SQLException {
        try {
            String sql = "SELECT id, last_name, first_name, birth_date FROM patients";

            if (patientSearchVMParameter != null) {
                String where = "WHERE 1=1";

                if (patientSearchVMParameter.getId() != null) {
                    where += "AND id = ?";
                }

                if (patientSearchVMParameter.getLastname() != null) {
                    where += " AND last_name LIKE ?";
                }
                if (patientSearchVMParameter.getFirstname() != null) {
                    where += " AND first_name LIKE ?";
                }
                sql += where;
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
            if (patientSearchVMParameter != null) {
                int paramNumber = 0;
                if (patientSearchVMParameter.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, patientSearchVMParameter.getId());
                }
                if (patientSearchVMParameter.getLastname() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + patientSearchVMParameter.getLastname() + "%");
                }
                if (patientSearchVMParameter.getFirstname() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + patientSearchVMParameter.getFirstname() + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();
            PatientsList.clear();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");
                Date birthDate = rs.getDate("birth_date");

                Patient patient = new Patient(id, lastName, firstName, birthDate);

                PatientsList.add(patient);

            }
            stmt.close();
        }
        catch(SQLException sqlException)
        {
            Logger.getLogger(PatientDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        return PatientsList;
    }


    public void save(Patient p) throws SQLException {
        try
        {
            String sql;
            if (p != null)
            {
                if (p.getId() != null) // UPDATE
                {

                    sql = "UPDATE patients SET last_name=?, first_name=?, birth_date=? WHERE id=?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                    pStmt.setString(1, p.getLastname());
                    pStmt.setString(2, p.getFirstname());
                    pStmt.setDate(3, new java.sql.Date(p.getBirthDate().getTime()));
                    pStmt.setInt(4, p.getId());

                    pStmt.executeUpdate();
                    pStmt.close();
                }
                else // CREATE
                {
                    sql = "INSERT INTO patients (last_name, first_name, birth_date) VALUES (?, ?, ?)";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pStmt.setString(1, p.getLastname());
                    pStmt.setString(2, p.getFirstname());
                    pStmt.setDate(3, new java.sql.Date(p.getBirthDate().getTime()));
                    pStmt.executeUpdate();

                    ResultSet rs = pStmt.getGeneratedKeys();
                    rs.next();
                    p.setId((int) rs.getLong(1));
                    rs.close();
                    pStmt.close();
                }
            }
        }
        catch (SQLException sqlException)
        {
            Logger.getLogger(PatientDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }


    public void delete(Patient entity) {
        if (entity != null && entity.getId() != null) {
            this.delete(entity.getId());
        }
    }

    public void delete(Integer id) {
        if (id != null) {
            try {
                String sql = "DELETE FROM patients WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException exception) {
                Logger.getLogger(PatientDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }




}
