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
            String sql = "INSERT INTO report (id, idPatient, idMedecin, date, description) VALUES (?,?, ?, ?, ?)";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pStmt.setInt(1, r.getId());
            pStmt.setInt(2, r.getIdPatient());
            pStmt.setInt(3, r.getIdMedecin());
            pStmt.setDate(4, java.sql.Date.valueOf(r.getDate()));
            pStmt.setString(5, r.getDescription());

            int rowsAffected = pStmt.executeUpdate();
            System.out.println("[DAO DEBUG] Lignes insérées dans report : " + rowsAffected);

            if (rowsAffected > 0) {
                ResultSet rs = pStmt.getGeneratedKeys();
                if (rs.next()) {
                    r.setId(rs.getInt(1));
                    System.out.println("[DAO DEBUG] Report sauvegardé avec id=" + r.getId());
                }
                rs.close();
            }

            pStmt.close();
        }
        catch (SQLException sqlException)
        {
            System.err.println("[DAO ERREUR] Échec save report : " + sqlException.getMessage());
            sqlException.printStackTrace();
        }
    }

    public Report findById(int reportId) throws SQLException {
        String sql = "SELECT * FROM report WHERE id = ?";
        try (PreparedStatement ps = connectDB.getConn().prepareStatement(sql)) {
            ps.setInt(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Report(
                            rs.getInt("id"),
                            rs.getInt("idPatient"),
                            rs.getInt("idMedecin"),
                            rs.getDate("date").toLocalDate(),
                            rs.getString("description")
                    );
                }
                return null;
            }
        }
    }

    public void update(Report r) throws SQLException {
        String sql = "UPDATE report SET idPatient=?, date=?, description=? WHERE id=?";
        try (PreparedStatement ps = connectDB.getConn().prepareStatement(sql)) {
            ps.setInt(1, r.getIdPatient());
            ps.setDate(2, java.sql.Date.valueOf(r.getDate()));
            ps.setString(3, r.getDescription());
            ps.setInt(4, r.getId());

            int rows = ps.executeUpdate();
            System.out.println("[DAO DEBUG] Report modifié, lignes affectées : " + rows);
        }
    }




}
