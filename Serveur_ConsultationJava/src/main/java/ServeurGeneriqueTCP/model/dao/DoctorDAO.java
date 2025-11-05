package ServeurGeneriqueTCP.model.dao;


import ServeurGeneriqueTCP.model.entity.Doctor;
import ServeurGeneriqueTCP.model.viewmodel.DoctorSearchVM;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;



public class DoctorDAO {

    private final ConnectDB connectDB;
    private final ArrayList<Doctor> doctorsList;

    public DoctorDAO()
    {
        connectDB = new ConnectDB();
        doctorsList = new ArrayList<>();
    }

    public DoctorDAO(ConnectDB mockConnectDB) {
        this.connectDB = mockConnectDB;
        this.doctorsList = new ArrayList<>();
    }

    public ArrayList<Doctor> getList()
    {
        return doctorsList;
    }

    public Doctor getById(Integer id)
    {
        for (Doctor entity : doctorsList) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Doctor> load(DoctorSearchVM doctorSearchVMParameter) throws SQLException {
        try {
            String sql = "SELECT id, specialty_id, last_name, first_name FROM doctors";

            if (doctorSearchVMParameter != null) {

                String where = " WHERE 1=1";

                if (doctorSearchVMParameter.getId() != null) {
                    where += " AND id = ?";
                }

                if (doctorSearchVMParameter.getSpecialty_id() != null) {
                    where += " AND specialty_id = ?";
                }

                if (doctorSearchVMParameter.getLastname() != null) {
                    where += " AND last_name LIKE ?";
                }

                sql += where;
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
            if (doctorSearchVMParameter != null) {
                int paramNumber = 0;
                if (doctorSearchVMParameter.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, doctorSearchVMParameter.getId());
                }
                if (doctorSearchVMParameter.getSpecialty_id() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber,doctorSearchVMParameter.getSpecialty_id());
                }
                if (doctorSearchVMParameter.getLastname() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + doctorSearchVMParameter.getLastname() + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();
            doctorsList.clear();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Integer specialityId = rs.getInt("specialty_id");
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");

                Doctor doctor = new Doctor(id, specialityId, lastName,firstName);

                doctorsList.add(doctor);

            }
            stmt.close();
        }
        catch(SQLException sqlException)
        {
            Logger.getLogger(DoctorDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        return doctorsList;
    }


    public void save(Doctor d) throws SQLException {
        try
        {
            String sql;
            if (d != null)
            {
                if (d.getId() != null) // UPDATE
                {

                    sql = "UPDATE doctors SET specialty_id=?, last_name=?, first_name=? WHERE id=?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                    pStmt.setInt(1, d.getSpecialty_id());
                    pStmt.setString(2, d.getLastname());
                    pStmt.setString(3, d.getFirstname());
                    pStmt.setInt(4, d.getId());

                    pStmt.executeUpdate();
                    pStmt.close();
                }
                else // CREATE
                {
                    sql = "INSERT INTO doctors (specialty_id, last_name, first_name) VALUES (?, ?, ?)";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pStmt.setInt(1, d.getSpecialty_id());
                    pStmt.setString(2, d.getLastname());
                    pStmt.setString(3, d.getFirstname());
                    pStmt.executeUpdate();

                    ResultSet rs = pStmt.getGeneratedKeys();
                    rs.next();
                    d.setId((int) rs.getLong(1));
                    rs.close();
                    pStmt.close();
                }
            }
        }
        catch (SQLException sqlException)
        {
            Logger.getLogger(DoctorDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }


    public void delete(Doctor entity) {
        if (entity != null && entity.getId() != null) {
            this.delete(entity.getId());
        }
    }

    public void delete(Integer id) {
        if (id != null) {
            try {
                String sql = "DELETE FROM doctors WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException exception) {
                Logger.getLogger(DoctorDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }


    Doctor Anissa = new Doctor(1, 2, "Ben Amor", "Anissa");








}
