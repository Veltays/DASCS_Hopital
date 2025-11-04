package ServeurGeneriqueTCP.model.dao;

import ServeurGeneriqueTCP.model.entity.Consultation;
import ServeurGeneriqueTCP.model.viewmodel.ConsultationSearchVM;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

public class ConsultationDAO {

    private final ConnectDB connectDB;
    private final ArrayList<Consultation> consultations;

    public ConsultationDAO()
    {
        connectDB = new ConnectDB();
        consultations = new ArrayList<>();
    }

    public ArrayList<Consultation> getList()
    {
        return consultations;
    }

    public Consultation getById(Integer id)
    {
        for (Consultation entity : consultations) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Consultation> load(ConsultationSearchVM csvm)
    {
        try
        {
            String sql = "SELECT" + "consultations.id" + "doctors.id" + "patients.id" ;
        }
        catch(SQLException e)
        {
            Logger.getLogger(ConsultationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void save(Consultation c) throws SQLException {
        String sql;
        if (c != null) {
            if (c.getId() != null) // UPDATE
            {

                sql = "UPDATE consultations SET "
                        + "id = ?, "
                        + "doctor_id = ?,"
                        + "patient_id = ?, "
                        + "date = ?, "
                        + "hour = ?,"
                        + "reason = ?"
                        + "WHERE id = ?";


                PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                pStmt.setInt(1,c.getEngine().getId());
                pStmt.setString(2,c.getModel());
                pStmt.setFloat(3,c.getPrice());
                pStmt.setDate(4,java.sql.Date.valueOf(c.getPurchase()));
                pStmt.setInt(5,c.getId());
                pStmt.executeUpdate();
                pStmt.close();
            }
            else // CREATE
            {
                if (c.getEngine() == null || c.getEngine().getId() == null) {
                    return; } // ...exception !
                sql = "INSERT INTO cars ("
                        + "engine_id, "
                        + "model, "
                        + "price, "
                        + "purchase "
                        + ") VALUES ("
                        + "?, "
                        + "?, "
                        + "?, "
                        + "? "
                        + ")";
                PreparedStatement pStmt =  connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pStmt.setInt(1,c.getEngine().getId());
                pStmt.setString(2,c.getModel());
                pStmt.setFloat(3,c.getPrice());
                pStmt.setDate(4,java.sql.Date.valueOf(c.getPurchase()));
                pStmt.executeUpdate();


                ResultSet rs = pStmt.getGeneratedKeys();
                rs.next();
                c.setId((int) rs.getLong(1));
                rs.close();
                pStmt.close();
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
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
                String sql = "DELETE FROM cars WHERE id = ?";
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


