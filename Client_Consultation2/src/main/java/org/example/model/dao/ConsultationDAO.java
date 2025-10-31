package org.example.model.dao;

import org.example.model.entity.Consultation;
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
            String sql = "SELECT" + "consultations.id" + "doctors.id" + "patients.id" +from machin machin

            if(csvm!=null)
            {
                String
            }

        }
    }
}
