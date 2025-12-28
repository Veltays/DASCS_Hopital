package model.dao;
import model.entity.Report;
import model.viewmodel.ReportSearchVM;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportDAO {

    private final ConnectDB connectDB;
    private final ArrayList<Report> reportList;

    public ReportDAO()
    {
        connectDB = new ConnectDB();
        reportList = new ArrayList<>();
    }

    public ReportDAO(ConnectDB mockConnectDB) {
        connectDB = mockConnectDB;
        reportList = new ArrayList<>();
    }

    public ArrayList<Report> getList()
    {
        return reportList;
    }

    public Report getById(Integer id)
    {
        for (Report entity : reportList) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Report> load (ReportSearchVM reportSearchVMParameter) throws SQLException
    {
        try
        {
            String sql = "SELECT id, idPatient, idMedecin, date, description FROM report";

            if (reportSearchVMParameter != null) {
                String where = " WHERE 1=1";

                if (reportSearchVMParameter.getId() != null) {
                    where += " AND id = ?";
                }
                if (reportSearchVMParameter.getIdPatient() != null) {
                    where += " AND idPatient = ?";
                }
                if (reportSearchVMParameter.getIdMedecin() != null) {
                    where += " AND idMedecin = ?";
                }
                if (reportSearchVMParameter.getDateReport() != null) {
                    where += " AND date = ?";
                }
                if (reportSearchVMParameter.getDescription() != null) {
                    where += " AND description LIKE ?";
                }

                sql += where;
            }
            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if (reportSearchVMParameter != null) {
                int paramNumber = 0;

                if (reportSearchVMParameter.getId() != null) {
                    stmt.setInt(++paramNumber, reportSearchVMParameter.getId());
                }
                if (reportSearchVMParameter.getIdPatient() != null) {
                    stmt.setInt(++paramNumber, reportSearchVMParameter.getIdPatient());
                }
                if (reportSearchVMParameter.getIdMedecin() != null) {
                    stmt.setInt(++paramNumber, reportSearchVMParameter.getIdMedecin());
                }
                if (reportSearchVMParameter.getDateReport() != null) {
                    stmt.setDate(++paramNumber, Date.valueOf(reportSearchVMParameter.getDateReport()));
                }
                if (reportSearchVMParameter.getDescription() != null) {
                    stmt.setString(++paramNumber, "%" + reportSearchVMParameter.getDescription() + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();
            reportList.clear();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Integer idPatient = rs.getInt("idPatient");
                Integer idMedecin = rs.getInt("idMedecin");
                LocalDate dateReport = null;
                if (rs.getDate("date") != null) {
                    dateReport = rs.getDate("date").toLocalDate();
                }
                String description = rs.getString("description");

                Report report = new Report(id, idPatient, idMedecin, dateReport, description);
                reportList.add(report);
            }

            stmt.close();
        } catch (SQLException exception) {
            Logger.getLogger(ReportDAO.class.getName()).log(Level.SEVERE, null, exception);
        }

        return reportList;
    }


    public void save(Report r)
    {
        try
        {
            String sql;
            if(r != null)
            {
                if(r.getId() != null) // UPDATE
                {
                    sql = "UPDATE specialties SET name=? WHERE id=?";

//                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
//                    pStmt.setString(1, r.getName());
//                    pStmt.setInt(2, r.getId());
//
//                    pStmt.executeUpdate();
//                    pStmt.close();
                }
            }
            else
            {
                sql = "INSERT INTO report (idPatient, idMedecin, date, description) VALUES (?, ?, ?, ?)";
                PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);

                pStmt.setInt(1, r.getIdPatient());
                pStmt.setInt(2, r.getIdMedecin());
                pStmt.setDate(3, java.sql.Date.valueOf(r.getDate())); // si LocalDate
                pStmt.setString(4, r.getDescription());

                pStmt.executeUpdate();

                ResultSet rs = pStmt.getGeneratedKeys();
                rs.next();
                r.setId(rs.getInt(1));

                rs.close();
                pStmt.close();

            }
        }
        catch (SQLException sqlException)
        {
            Logger.getLogger(SpecialtyDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }



    }




}
