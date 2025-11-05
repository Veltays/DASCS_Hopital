package ServeurGeneriqueTCP.model.dao;

import ServeurGeneriqueTCP.model.entity.Consultation;
import ServeurGeneriqueTCP.model.viewmodel.ConsultationSearchVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultationDAOTest {

    private ConsultationDAO dao;
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

        dao = new ConsultationDAO(mockConnectDB);
    }

    @Test
    void load_ShouldWorkWithRealDB() throws Exception {
        ArrayList<Consultation> result = dao.load(null);
        assertNotNull(result);
        System.out.println("Nombre de consultations : " + result.size());
    }

    // --- getList et getById ---
    @Test
    void getList_ShouldReturnEmptyInitially() {
        assertTrue(dao.getList().isEmpty());
    }

    @Test
    void getById_ShouldReturnEntity_WhenFound() {
        Consultation c1 = new Consultation(1, 1, 2, "10:00", new Date(), "Check");
        dao.getList().add(c1);
        assertEquals(c1, dao.getById(1));
    }

    @Test
    void getById_ShouldReturnNull_WhenNotFound() {
        assertNull(dao.getById(999));
    }

    // --- load ---
    @Test
    void load_ShouldReturnListOfConsultations() throws Exception {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("doctor_id")).thenReturn(10);
        when(mockResultSet.getInt("patient_id")).thenReturn(20);
        when(mockResultSet.getString("hour")).thenReturn("09:00");
        when(mockResultSet.getDate("date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(mockResultSet.getString("reason")).thenReturn("Routine");

        // Act
        ArrayList<Consultation> list = dao.load(null);

        // Assert
        assertEquals(1, list.size());
        Consultation c = list.get(0);
        assertEquals(1, c.getId());
        assertEquals(10, c.getDoctor_id());
        assertEquals("Routine", c.getReason());
    }

    // --- save (INSERT) ---
    @Test
    void save_ShouldInsert_WhenIdIsNull() throws Exception {
        // 📅 Crée une date dans le futur (+1 jour)
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // + 1 jour en millisecondes

        Consultation c = new Consultation(null, 10, 20, "11:00", futureDate, "New");

        when(mockConnection.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
        ResultSet mockKeys = mock(ResultSet.class);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockKeys);
        when(mockKeys.next()).thenReturn(true);
        when(mockKeys.getLong(1)).thenReturn(1L);

        dao.save(c);

        verify(mockStmt, times(1)).executeUpdate();
        assertNotNull(c.getId());
    }

    // --- save (UPDATE) ---
    @Test
    void save_ShouldUpdate_WhenIdNotNull() throws Exception {
        Consultation c = new Consultation(1, 10, 20, "12:00", new Date(), "Follow-up");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);

        dao.save(c);

        verify(mockStmt, times(1)).executeUpdate();
        verify(mockStmt, times(1)).close();
    }

    // --- delete by entity ---
    @Test
    void delete_ShouldCallDeleteById_WhenEntityNotNull() throws Exception {
        Consultation c = new Consultation(5, 1, 2, "08:00", new Date(), "Test");

        PreparedStatement stmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(stmt);

        dao.delete(c);

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
