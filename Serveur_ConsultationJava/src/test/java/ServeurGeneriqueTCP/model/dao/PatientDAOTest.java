package ServeurGeneriqueTCP.model.dao;

import ServeurGeneriqueTCP.model.entity.Patient;
import ServeurGeneriqueTCP.model.viewmodel.PatientSearchVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientDAOTest {

    private PatientDAO dao;
    private ConnectDB mockConnectDB;
    private Connection mockConnection;
    private PreparedStatement mockStmt;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws Exception {
        mockConnectDB = mock(ConnectDB.class);
        mockConnection = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnectDB.getConn()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);

        // Injection du mock via un constructeur à ajouter
        dao = new PatientDAO(mockConnectDB);
    }

    // --- getList & getById ---
    @Test
    void getList_ShouldReturnEmptyInitially() {
        assertTrue(dao.getList().isEmpty());
    }

    @Test
    void getById_ShouldReturnPatient_WhenFound() {
        Patient p1 = new Patient(1, "Dupont", "Claire", new Date());
        dao.getList().add(p1);
        assertEquals(p1, dao.getById(1));
    }

    @Test
    void getById_ShouldReturnNull_WhenNotFound() {
        assertNull(dao.getById(999));
    }

    // --- load ---
    @Test
    void load_ShouldReturnListOfPatients() throws Exception {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("last_name")).thenReturn("Martin");
        when(mockResultSet.getString("first_name")).thenReturn("Paul");
        when(mockResultSet.getDate("birth_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

        ArrayList<Patient> list = dao.load(null);

        assertEquals(1, list.size());
        Patient p = list.get(0);
        assertEquals(1, p.getId());
        assertEquals("Martin", p.getLastname());
        assertEquals("Paul", p.getFirstname());
    }

    // --- save (INSERT) ---
    @Test
    void save_ShouldInsert_WhenIdIsNull() throws Exception {
        Date birth = new Date();
        Patient p = new Patient(null, "Durand", "Alice", birth);

        when(mockConnection.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStmt);
        ResultSet mockKeys = mock(ResultSet.class);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockKeys);
        when(mockKeys.next()).thenReturn(true);
        when(mockKeys.getLong(1)).thenReturn(7L);

        dao.save(p);

        verify(mockStmt, times(1)).executeUpdate();
        assertEquals(7, p.getId());
    }

    // --- save (UPDATE) ---
    @Test
    void save_ShouldUpdate_WhenIdNotNull() throws Exception {
        Patient p = new Patient(3, "Bernard", "Lucie", new Date());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);

        dao.save(p);

        verify(mockStmt, times(1)).executeUpdate();
        verify(mockStmt, times(1)).close();
    }

    // --- delete by entity ---
    @Test
    void delete_ShouldCallDeleteById_WhenEntityNotNull() throws Exception {
        Patient p = new Patient(5, "Test", "Jean", new Date());
        PreparedStatement stmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(stmt);

        dao.delete(p);

        verify(mockConnection, times(1)).prepareStatement(anyString());
    }

    // --- delete by id ---
    @Test
    void delete_ShouldExecuteDelete_WhenIdNotNull() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        dao.delete(10);
        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void delete_ShouldNotExecute_WhenIdIsNull() throws Exception {
        dao.delete((Integer) null);
        verify(mockConnection, never()).prepareStatement(anyString());
    }
}
