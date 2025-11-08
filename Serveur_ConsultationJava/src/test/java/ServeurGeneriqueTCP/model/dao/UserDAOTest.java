package ServeurGeneriqueTCP.model.dao;

import model.dao.ConnectDB;
import model.dao.UserDAO;
import model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDAOTest {

    private UserDAO dao;
    private ConnectDB mockConnectDB;
    private Connection mockConnection;
    private Statement mockStatement;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws Exception {
        mockConnectDB = mock(ConnectDB.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnectDB.getConn()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // constructeur à ajouter
        dao = new UserDAO(mockConnectDB);
    }

    // --- loadAll ---
    @Test
    void loadAll_ShouldReturnListOfUsers() throws Exception {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("login")).thenReturn("admin");
        when(mockResultSet.getString("password")).thenReturn("1234567");
        when(mockResultSet.getString("role")).thenReturn("ADMIN");

        ArrayList<User> users = dao.loadAll();

        assertEquals(1, users.size());
        User u = users.get(0);
        assertEquals(1, u.getId());
        assertEquals("admin", u.getLogin());
        assertEquals("admin", u.getRole());
    }

    @Test
    void loadAll_ShouldReturnEmptyList_WhenNoResults() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        ArrayList<User> users = dao.loadAll();
        assertTrue(users.isEmpty());
    }

    // --- getByLogin ---
    @Test
    void getByLogin_ShouldReturnUser_WhenFound() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(2);
        when(mockResultSet.getString("login")).thenReturn("doctor1");
        when(mockResultSet.getString("password")).thenReturn("abcdefg");
        when(mockResultSet.getString("role")).thenReturn("DOCTOR");

        User u = dao.getByLogin("doctor1");

        assertNotNull(u);
        assertEquals("doctor1", u.getLogin());
        assertEquals("doctor", u.getRole());
        verify(mockPreparedStatement, times(1)).setString(1, "doctor1");
    }

    @Test
    void getByLogin_ShouldReturnNull_WhenNotFound() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        User u = dao.getByLogin("unknown");
        assertNull(u);
    }

    // --- checkLogin ---
    @Test
    void checkLogin_ShouldReturnTrue_WhenCredentialsMatch() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        boolean result = dao.checkLogin("admin", "1234567");

        verify(mockPreparedStatement, times(1)).setString(1, "admin");
        verify(mockPreparedStatement, times(1)).setString(2, "1234567");
        assertTrue(result);
    }

    @Test
    void checkLogin_ShouldReturnFalse_WhenCredentialsInvalid() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        boolean result = dao.checkLogin("wrong", "password");
        assertFalse(result);
    }

    @Test
    void checkLogin_ShouldReturnFalse_WhenSQLExceptionOccurs() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);
        boolean result = dao.checkLogin("admin", "1234567");
        assertFalse(result);
    }
}
