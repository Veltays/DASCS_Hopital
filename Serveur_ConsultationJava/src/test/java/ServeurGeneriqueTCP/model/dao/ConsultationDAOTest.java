package ServeurGeneriqueTCP.model.dao;

import model.dao.ConnectDB;
import model.dao.ConsultationDAO;
import model.entity.Consultation;
import model.viewmodel.ConsultationSearchVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

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
    void getList_ShouldReturnEmptyInitially() {
        assertTrue(dao.getList().isEmpty());
    }

    @Test
    void getById_ShouldReturnEntity_WhenFound() {
        Consultation c1 = new Consultation(
                1, 1, 2,
                "10:00",
                LocalDate.now(),
                "Check",
                "30"
        );
        dao.getList().add(c1);
        assertEquals(c1, dao.getById(1));
    }

    @Test
    void getById_ShouldReturnNull_WhenNotFound() {
        assertNull(dao.getById(999));
    }

    @Test
    void load_ShouldReturnListOfConsultations() throws Exception {

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("doctor_id")).thenReturn(10);
        when(mockResultSet.getInt("patient_id")).thenReturn(20);
        when(mockResultSet.getString("hour")).thenReturn("09:00");
        when(mockResultSet.getDate("date")).thenReturn(java.sql.Date.valueOf(LocalDate.now()));
        when(mockResultSet.getString("reason")).thenReturn("Routine");
        when(mockResultSet.getString("duration")).thenReturn("30");

        ArrayList<Consultation> list = dao.load(null);

        assertEquals(1, list.size());
        Consultation c = list.get(0);
        assertEquals(1, c.getId());
        assertEquals(10, c.getDoctor_id());
        assertEquals(20, c.getPatient_id());
        assertEquals("Routine", c.getReason());
        assertEquals("30", c.getDuration());
    }

    // --- SAVE INSERT ---
    @Test
    void save_ShouldInsert_WhenIdIsNull() throws Exception {

        Consultation c = new Consultation(
                null, 10, 20,
                "11:00",
                LocalDate.now(),
                "New",
                "30"
        );

        when(mockConnection.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStmt);

        ResultSet mockKeys = mock(ResultSet.class);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockKeys);
        when(mockKeys.next()).thenReturn(true);
        when(mockKeys.getLong(1)).thenReturn(1L);

        dao.save(c);

        verify(mockStmt, times(1)).executeUpdate();
        assertEquals(1, c.getId());
    }

    // --- SAVE UPDATE ---
    @Test
    void save_ShouldUpdate_WhenIdNotNull() throws Exception {
        Consultation c = new Consultation(
                1, 10, 20,
                "12:00",
                LocalDate.now(),
                "Follow-up",
                "30"
        );

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);

        dao.save(c);

        verify(mockStmt, times(1)).executeUpdate();
        verify(mockStmt).close();
    }

    // --- DELETE entity ---
    @Test
    void delete_ShouldCallDeleteById_WhenEntityNotNull() throws Exception {

        Consultation c = new Consultation(
                5, 1, 2,
                "08:00",
                LocalDate.now(),
                "Test",
                "30"
        );

        PreparedStatement stmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(stmt);

        dao.delete(c);

        verify(mockConnection).prepareStatement(anyString());
    }

    // --- DELETE by id ---
    @Test
    void delete_ShouldExecuteDelete_WhenIdNotNull() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        dao.delete(10);
        verify(mockStmt).executeUpdate();
    }

    @Test
    void delete_ShouldNotExecute_WhenIdIsNull() throws Exception {
        dao.delete((Integer) null);
        verify(mockConnection, never()).prepareStatement(anyString());
    }
}
