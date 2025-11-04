package org.example.model.dao;

import org.example.model.entity.Consultation;
import org.example.model.viewmodel.ConsultationSearchVM;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.time.LocalDate;
public class ConsultationDAO {

    private ConnectDB connectDB;
    private ArrayList<Consultation> consultations;

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
        catch()
        {}
    }

    public void save(Consultation c)
    {
        try
        {
            String sql;

        }
        catch ( ) {
            Logger.getLogger(ConsultationDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            } catch (SQLException ex) {
                Logger.getLogger(ConsultationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}


