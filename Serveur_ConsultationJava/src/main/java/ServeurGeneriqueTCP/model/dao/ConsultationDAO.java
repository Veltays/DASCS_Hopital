package ServeurGeneriqueTCP.model.dao;

import ServeurGeneriqueTCP.model.entity.Consultation;
import ServeurGeneriqueTCP.model.viewmodel.ConsultationSearchVM;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.logging.*;

public class ConsultationDAO {

    private final ConnectDB connectDB;
    private final ArrayList<Consultation> consultationsList;

    public ConsultationDAO()
    {
        connectDB = new ConnectDB();
        consultationsList = new ArrayList<>();
    }

    public ConsultationDAO(ConnectDB mockConnectDB) {
        this.connectDB = mockConnectDB;
        this.consultationsList = new ArrayList<>();
    }


    public ArrayList<Consultation> getList()
    {
        return consultationsList;
    }

    public Consultation getById(Integer id)
    {
        for (Consultation entity : consultationsList) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Consultation> load(ConsultationSearchVM ConsultationSearchVMParameter) throws SQLException {
        try {
            String sql = "SELECT id, doctor_id, patient_id, date, hour, reason FROM consultations ";

            if (ConsultationSearchVMParameter != null) {
                String where = " WHERE 1=1";

                if (ConsultationSearchVMParameter.getId() != null) {
                    where += " AND id = ?";
                }

                if (ConsultationSearchVMParameter.getDoctor_id() != null) {
                    where += " AND doctor_id = ?";
                }

                if (ConsultationSearchVMParameter.getPatient_id() != null) {
                    where += " AND patient_id = ?";
                }

                if(ConsultationSearchVMParameter.getDate() != null) {
                    where += " AND date = ?";
                }

                if(ConsultationSearchVMParameter.getUserId())
                sql += where;
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
            if (ConsultationSearchVMParameter != null) {
                int paramNumber = 0;
                if (ConsultationSearchVMParameter.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, ConsultationSearchVMParameter.getId());
                }
                if (ConsultationSearchVMParameter.getDoctor_id() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, ConsultationSearchVMParameter.getDoctor_id());
                }
                if (ConsultationSearchVMParameter.getPatient_id() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, ConsultationSearchVMParameter.getPatient_id());
                }
                if (ConsultationSearchVMParameter.getDate() != null) {
                    paramNumber++;
                    stmt.setDate(paramNumber, new Date(ConsultationSearchVMParameter.getDate().getTime()));
                }
            }

            ResultSet rs = stmt.executeQuery();
            consultationsList.clear();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Integer doctor_id = rs.getInt("doctor_id");
                Integer patient_id = rs.getInt("patient_id");
                String hour = rs.getString("hour");
                Date date = rs.getDate("date");
                String reason = rs.getString("reason");

                Consultation consultation = new Consultation(id, doctor_id, patient_id, hour, date, reason);

                consultationsList.add(consultation);

            }
            stmt.close();
        }
        catch(SQLException sqlException)
        {
            Logger.getLogger(ConsultationDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        return consultationsList;
    }


    public void save(Consultation c) throws SQLException {
        try
        {
            String sql;
            if (c != null)
            {
                if (c.getId() != null) // UPDATE
                {

                    sql = "UPDATE consultations SET doctor_id=?, patient_id=?, date=?, hour=?, reason=? WHERE id=?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                    pStmt.setInt(1, c.getDoctor_id());
                    pStmt.setInt(2, c.getPatient_id());
                    pStmt.setDate(3, new java.sql.Date(c.getDate().getTime()));
                    pStmt.setString(4, c.getHour());
                    pStmt.setString(5, c.getReason());
                    pStmt.setInt(6, c.getId());
                    pStmt.executeUpdate();
                    pStmt.close();
                }
                else // CREATE
                {
                    sql = "INSERT INTO consultations (doctor_id, patient_id, date, hour, reason) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pStmt.setInt(1, c.getDoctor_id());
                    pStmt.setInt(2, c.getPatient_id());
                    pStmt.setDate(3, new java.sql.Date(c.getDate().getTime()));
                    pStmt.setString(4, c.getHour());
                    pStmt.setString(5, c.getReason());
                    pStmt.executeUpdate();

                    ResultSet rs = pStmt.getGeneratedKeys();
                    rs.next();
                    c.setId((int) rs.getLong(1));
                    rs.close();
                    pStmt.close();
                }

            }
        }
        catch (SQLException sqlException)
        {
            Logger.getLogger(ConsultationDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }


    public void delete(Consultation entity) {
        if (entity != null && entity.getId() != null) {
            this.delete(entity.getId());
        }
    }

    public void delete(Integer id) {
        if (id != null) {
            try {
                String sql = "DELETE FROM consultations WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException exception) {
                Logger.getLogger(ConsultationDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }


}


