package ServeurGeneriqueTCP.model.dao;
import ServeurGeneriqueTCP.model.entity.Specialty;
import ServeurGeneriqueTCP.model.viewmodel.SpecialtySearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;



public class SpecialtyDAO {

    private final ConnectDB connectDB;
    private final ArrayList<Specialty> SpecialtyList;

    public SpecialtyDAO()
    {
        connectDB = new ConnectDB();
        SpecialtyList = new ArrayList<>();
    }

    public ArrayList<Specialty> getList()
    {
        return SpecialtyList;
    }

    public Specialty getById(Integer id)
    {
        for (Specialty entity : SpecialtyList) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Specialty> load(SpecialtySearchVM specialtySearchVMParameter) throws SQLException {
        try {
            String sql = "SELECT id, name FROM specialties";

            if (specialtySearchVMParameter != null) {
                String where = "WHERE 1=1";

                if (specialtySearchVMParameter.getId() != null) {
                    where += " AND id = ?";
                }

                if (specialtySearchVMParameter.getName() != null) {
                    where += " AND name LIKE ?";
                }
                sql += where;
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
            if (specialtySearchVMParameter != null) {
                int paramNumber = 0;
                if (specialtySearchVMParameter.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, specialtySearchVMParameter.getId());
                }
                if (specialtySearchVMParameter.getName() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, "%" + specialtySearchVMParameter.getName() + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();
            SpecialtyList.clear();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");

                Specialty specialty = new Specialty(id, name);

                SpecialtyList.add(specialty);

            }
            stmt.close();
        }
        catch(SQLException sqlException)
        {
            Logger.getLogger(SpecialtyDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        return SpecialtyList;
    }


    public void save(Specialty s) throws SQLException {
        try
        {
            String sql;
            if (s != null)
            {
                if (s.getId() != null) // UPDATE
                {

                    sql = "UPDATE specialties SET name=? WHERE id=?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
                    pStmt.setString(1, s.getName());
                    pStmt.setInt(2, s.getId());

                    pStmt.executeUpdate();
                    pStmt.close();
                }
                else // CREATE
                {
                    sql = "INSERT INTO specialties (name) VALUES (?)";
                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pStmt.setString(1, s.getName());
                    pStmt.executeUpdate();

                    ResultSet rs = pStmt.getGeneratedKeys();
                    rs.next();
                    s.setId((int) rs.getLong(1));
                    rs.close();
                    pStmt.close();
                }
            }
        }
        catch (SQLException sqlException)
        {
            Logger.getLogger(SpecialtyDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }


    public void delete(Specialty entity) {
        if (entity != null && entity.getId() != null) {
            this.delete(entity.getId());
        }
    }

    public void delete(Integer id) {
        if (id != null) {
            try {
                String sql = "DELETE FROM specialties WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException exception) {
                Logger.getLogger(SpecialtyDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }




}

