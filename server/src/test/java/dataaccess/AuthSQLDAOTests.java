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

public class AuthSQLDAOTests {
    // Database
    private DatabaseManager db;

    // SQL DAOs
    private AuthSQL authSQL;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.clear();
        authSQL = new AuthSQL();
    }

    @AfterAll
    public static void tearDown() throws DataAccessException {
        DatabaseManager db = new DatabaseManager();
        db.clear();
    }

    @Test
    public void testAuthSQLauthTokenExistsPositive() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");
        var auth2 = new AuthToken("auth2", "username2");

        // Test addAuthToken
        try {
            authSQL.addAuthToken(auth1);
            authSQL.addAuthToken(auth2);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test authTokenExists
        try {
            Assertions.assertTrue(authSQL.authTokenExists(auth1.getAuthToken()));
            Assertions.assertTrue(authSQL.authTokenExists(auth2.getAuthToken()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testAuthSQLauthTokenExistsNegative() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Test authTokenExists
        try {
            Assertions.assertFalse(authSQL.authTokenExists(String.valueOf(auth1)));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testAuthSQLaddAuthTokenPositive() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Test addAuthToken
        try {
            authSQL.addAuthToken(auth1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test authTokenExists
        try {
            Assertions.assertTrue(authSQL.authTokenExists(auth1.getAuthToken()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testAuthSQLaddAuthTokenNegative() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Try adding twice
        try {
            authSQL.addAuthToken(auth1);
            Assertions.assertThrows(ServerException.class, () -> authSQL.addAuthToken(auth1));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testAuthSQLcheckAuthTokenPositive() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Test addAuthToken
        try {
            authSQL.addAuthToken(auth1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test checkAuthToken
        try {
            Assertions.assertTrue(authSQL.checkAuthToken(auth1.getAuthToken()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testAuthSQLcheckAuthTokenNegative() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Test checkAuthToken
        Assertions.assertThrows(DataAccessException.class, () -> authSQL.checkAuthToken(String.valueOf(auth1)));
    }

    @Test
    public void testAuthSQLgetUsernamePositive() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Test addAuthToken
        try {
            authSQL.addAuthToken(auth1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getUsername
        try {
            Assertions.assertEquals("username1", authSQL.getUsername(auth1.getAuthToken()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testAuthSQLgetUsernameNegative() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Test getUsername
        Assertions.assertThrows(ServerException.class, () -> authSQL.getUsername(String.valueOf(auth1)));
    }

    @Test
    @Order(1)
    public void testAuthSQLclear() throws ServerException, DataAccessException {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Test addAuthToken
        try {
            authSQL.addAuthToken(auth1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test clear
        authSQL.clear();

        // Test authTokenExists
        try {
            Assertions.assertFalse(authSQL.authTokenExists(String.valueOf(auth1)));
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testAuthSQLdeleteAuthTokenPositive() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Test addAuthToken
        try {
            authSQL.addAuthToken(auth1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test deleteAuthToken
        try {
            authSQL.deleteAuthToken(auth1.getAuthToken());
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test authTokenExists
        try {
            Assertions.assertFalse(authSQL.authTokenExists(String.valueOf(auth1)));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testAuthSQLdeleteAuthTokenNegative() {
        // Make auth tokens
        var auth1 = new AuthToken("auth1", "username1");

        // Test deleteAuthToken
        Assertions.assertThrows(ServerException.class, () -> authSQL.deleteAuthToken(String.valueOf(auth1)));
    }

    @Test
    public void testAuthSQLPositive() {
        // Make auth tokens
        AuthToken auth1 = new AuthToken("auth1", "username1");
        AuthToken auth2 = new AuthToken("auth2", "username2");

        // Test addAuth
        try {
            authSQL.addAuthToken(auth1);
            authSQL.addAuthToken(auth2);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getAuth
        try {
            Assertions.assertTrue(authSQL.checkAuthToken(auth1.getAuthToken()));
            Assertions.assertTrue(authSQL.checkAuthToken(auth2.getAuthToken()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test removeAuth
        try {
            authSQL.deleteAuthToken(auth1.getAuthToken());
            authSQL.deleteAuthToken(auth2.getAuthToken());
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getAuth
        try {
            Assertions.assertFalse(authSQL.authTokenExists(auth1.getAuthToken()));
            Assertions.assertFalse(authSQL.authTokenExists(auth2.getAuthToken()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test clear
        try {
            authSQL.addAuthToken(auth1);
            authSQL.addAuthToken(auth2);
            authSQL.clear();
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getAuth
        try {
            Assertions.assertFalse(authSQL.authTokenExists(auth1.getAuthToken()));
            Assertions.assertFalse(authSQL.authTokenExists(auth2.getAuthToken()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testAuthSQLNegative() {
        // Make auth tokens
        AuthToken auth1 = new AuthToken("auth1", "username1");

        // Test getAuth
        try {
            Assertions.assertFalse(authSQL.authTokenExists(String.valueOf(auth1)));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test removeAuth
        Assertions.assertThrows(ServerException.class, () -> authSQL.deleteAuthToken(String.valueOf(auth1)));

        // Add two
        try {
            authSQL.addAuthToken(auth1);
            Assertions.assertThrows(ServerException.class, () -> authSQL.addAuthToken(auth1));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }
}