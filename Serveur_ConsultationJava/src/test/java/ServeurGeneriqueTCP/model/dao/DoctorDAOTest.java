package ServeurGeneriqueTCP.model.dao;

import model.dao.ConnectDB;
import model.dao.DoctorDAO;
import model.entity.Doctor;
import model.viewmodel.DoctorSearchVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorDAOTest {

    private DoctorDAO dao;
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

        dao = new DoctorDAO(mockConnectDB);
    }

    // -------------------------------
    // getList & getById
    // -------------------------------
    @Test
    void getList_ShouldReturnEmptyInitially() {
        assertTrue(dao.getList().isEmpty());
    }

    @Test
    void getById_ShouldReturnDoctor_WhenFound() {
        Doctor d1 = new Doctor(1, 2, "Dupont", "Jean", 10);
        dao.getList().add(d1);

        assertEquals(d1, dao.getById(1));
    }

    @Test
    void getById_ShouldReturnNull_WhenNotFound() {
        assertNull(dao.getById(99));
    }

    // -------------------------------
    // load()
    // -------------------------------
    @Test
    void load_ShouldReturnListOfDoctors() throws Exception {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("specialty_id")).thenReturn(2);
        when(mockResultSet.getString("last_name")).thenReturn("Smith");
        when(mockResultSet.getString("first_name")).thenReturn("John");
        when(mockResultSet.getInt("user_id")).thenReturn(50);

        ArrayList<Doctor> list = dao.load(new DoctorSearchVM());

        assertEquals(1, list.size());
        Doctor d = list.get(0);

        assertEquals(1, d.getId());
        assertEquals(2, d.getSpecialty_id());
        assertEquals("Smith", d.getLastname());
        assertEquals("John", d.getFirstname());
        assertEquals(50, d.getUser_id());
    }

    // -------------------------------
    // save() INSERT
    // -------------------------------
    @Test
    void save_ShouldInsert_WhenIdIsNull() throws Exception {
        Doctor d = new Doctor(null, 2, "Martin", "Paul", 10);

        when(mockConnection.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStmt);

        ResultSet mockKeys = mock(ResultSet.class);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockKeys);
        when(mockKeys.next()).thenReturn(true);
        when(mockKeys.getLong(1)).thenReturn(5L);

        dao.save(d);

        verify(mockStmt, times(1)).executeUpdate();
        assertEquals(5, d.getId());
    }

    // -------------------------------
    // save() UPDATE
    // -------------------------------
    @Test
    void save_ShouldUpdate_WhenIdNotNull() throws Exception {
        Doctor d = new Doctor(1, 3, "Durand", "Alice", 10);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);

        dao.save(d);

        verify(mockStmt, times(1)).executeUpdate();
        verify(mockStmt, times(1)).close();
    }

    // -------------------------------
    // delete(entity)
    // -------------------------------
    @Test
    void delete_ShouldCallDeleteById_WhenEntityNotNull() throws Exception {
        Doctor d = new Doctor(4, 1, "Rossi", "Luca", 10);

        PreparedStatement stmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(stmt);

        dao.delete(d);

        verify(mockConnection, times(1)).prepareStatement(anyString());
    }

    // -------------------------------
    // delete(id)
    // -------------------------------
    @Test
    void delete_ShouldExecuteDelete_WhenIdNotNull() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);

        dao.delete(3);

        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void delete_ShouldNotExecute_WhenIdIsNull() throws Exception {
        dao.delete((Integer) null);

        verify(mockConnection, never()).prepareStatement(anyString());
    }
}
