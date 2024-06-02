package dataaccess;

import dataaccess.*;
import model.AuthToken;
import model.Game;
import model.User;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import server.Server;
import server.ServerException;

import java.util.ArrayList;
import java.util.HashSet;

public class UserSQLDAOTests {
    // Database
    private DatabaseManager db;

    // SQL DAOs
    private UserSQL userSQL;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.clear();
        userSQL = new UserSQL();
    }

    @AfterAll
    public static void tearDown() throws DataAccessException {
        DatabaseManager db = new DatabaseManager();
        db.clear();
    }

    @Test
    public void testUserSQLuserExistsPositive() throws ServerException, DataAccessException {
        // Make users
        var user1 = new User("username1", "password1", "email1");
        var user2 = new User("username2", "password2", "email2");

        // Test addUser
        try {
            userSQL.addUser(user1);
            userSQL.addUser(user2);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test userExists
        Assertions.assertTrue(userSQL.userExists(user1));
        Assertions.assertTrue(userSQL.userExists(user2));
    }

    @Test
    public void testUserSQLuserExistsNegative() throws ServerException, DataAccessException {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Test userExists
        Assertions.assertFalse(userSQL.userExists(user1));
    }

    @Test
    public void testUserSQLaddUserPositive() throws ServerException, DataAccessException {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Test addUser
        try {
            userSQL.addUser(user1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test userExists
        Assertions.assertTrue(userSQL.userExists(user1));
    }

    @Test
    public void testUserSQLaddUserNegative() {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Try adding twice
        try {
            userSQL.addUser(user1);
            Assertions.assertThrows(DataAccessException.class, () -> userSQL.addUser(user1));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testUserSQLgetUserPositive() {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Test addUser
        try {
            userSQL.addUser(user1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getUser
        try {
            Assertions.assertTrue(BCrypt.checkpw(user1.getPassword(), userSQL.getUser(user1.username()).getPassword()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testUserSQLgetUserNegative() throws ServerException, DataAccessException {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Test getUser
        Assertions.assertNull(userSQL.getUser(user1.getUsername()));
    }

    @Test
    public void testUserSQLclear() throws ServerException, DataAccessException {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Test addUser
        try {
            userSQL.addUser(user1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test clear
        try {
            userSQL.clear();
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test userExists
        Assertions.assertFalse(userSQL.userExists(user1));
    }

    @Test
    public void testUserSQLdeleteUserPositive() throws ServerException, DataAccessException {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Test addUser
        try {
            userSQL.addUser(user1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test deleteUser
        try {
            userSQL.deleteUser(user1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test userExists
        Assertions.assertFalse(userSQL.userExists(user1));
    }

    @Test
    public void testUserSQLdeleteUserNegative() {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Test deleteUser
        Assertions.assertThrows(ServerException.class, () -> userSQL.deleteUser(user1));
    }

    @Test
    public void testUserSQLPositive() {
        // Make users
        User user1 = new User("username1", "password1", "email1");
        User user2 = new User("username2", "password2", "email2");

        // Test addUser
        try {
            userSQL.addUser(user1);
            userSQL.addUser(user2);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getUser
        try {
            Assertions.assertTrue(BCrypt.checkpw(user1.getPassword(), userSQL.getUser(user1.username()).getPassword()));
            Assertions.assertTrue(BCrypt.checkpw(user2.getPassword(), userSQL.getUser(user2.username()).getPassword()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testUserSQLNegative() throws ServerException, DataAccessException {
        // Make users
        User user1 = new User("username1", "password1", "email1");

        // Test getUser
        Assertions.assertNull(userSQL.getUser(user1.getUsername()));

        // Add two
        try {
            userSQL.addUser(user1);
            Assertions.assertThrows(DataAccessException.class, () -> userSQL.addUser(user1));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }
}