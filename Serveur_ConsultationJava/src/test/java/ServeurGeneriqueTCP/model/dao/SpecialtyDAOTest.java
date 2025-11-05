package ServeurGeneriqueTCP.model.dao;

import ServeurGeneriqueTCP.model.entity.Specialty;
import ServeurGeneriqueTCP.model.viewmodel.SpecialtySearchVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpecialtyDAOTest {

    private SpecialtyDAO dao;
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

        // Constructeur à ajouter dans SpecialtyDAO pour l'injection du mock
        dao = new SpecialtyDAO(mockConnectDB);
    }

    // --- getList et getById ---
    @Test
    void getList_ShouldReturnEmptyInitially() {
        assertTrue(dao.getList().isEmpty());
    }

    @Test
    void getById_ShouldReturnSpecialty_WhenFound() {
        Specialty s1 = new Specialty(1, "Cardiologie");
        dao.getList().add(s1);
        assertEquals(s1, dao.getById(1));
    }

    @Test
    void getById_ShouldReturnNull_WhenNotFound() {
        assertNull(dao.getById(999));
    }

    // --- load ---
    @Test
    void load_ShouldReturnListOfSpecialties() throws Exception {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Neurologie");

        ArrayList<Specialty> list = dao.load(null);

        assertEquals(1, list.size());
        Specialty s = list.get(0);
        assertEquals(1, s.getId());
        assertEquals("Neurologie", s.getName());
    }

    // --- save (INSERT) ---
    @Test
    void save_ShouldInsert_WhenIdIsNull() throws Exception {
        Specialty s = new Specialty(null, "Pédiatrie");

        when(mockConnection.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStmt);
        ResultSet mockKeys = mock(ResultSet.class);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockKeys);
        when(mockKeys.next()).thenReturn(true);
        when(mockKeys.getLong(1)).thenReturn(10L);

        dao.save(s);

        verify(mockStmt, times(1)).executeUpdate();
        assertEquals(10, s.getId());
    }

    // --- save (UPDATE) ---
    @Test
    void save_ShouldUpdate_WhenIdNotNull() throws Exception {
        Specialty s = new Specialty(1, "Dermatologie");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);

        dao.save(s);

        verify(mockStmt, times(1)).executeUpdate();
        verify(mockStmt, times(1)).close();
    }

    // --- delete by entity ---
    @Test
    void delete_ShouldCallDeleteById_WhenEntityNotNull() throws Exception {
        Specialty s = new Specialty(3, "Oncologie");
        PreparedStatement stmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(stmt);

        dao.delete(s);

        verify(mockConnection, times(1)).prepareStatement(anyString());
    }

    // --- delete by id ---
    @Test
    void delete_ShouldExecuteDelete_WhenIdNotNull() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        dao.delete(4);
        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void delete_ShouldNotExecute_WhenIdIsNull() throws Exception {
        dao.delete((Integer) null);
        verify(mockConnection, never()).prepareStatement(anyString());
    }
}
