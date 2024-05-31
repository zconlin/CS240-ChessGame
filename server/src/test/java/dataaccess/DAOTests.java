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

public class DAOTests {
    // Database
    private DatabaseManager db;

    // SQL DAOs
    private AuthSQL authSQL;
    private GameSQL gameSQL;
    private UserSQL userSQL;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.clear();
        authSQL = new AuthSQL();
        gameSQL = new GameSQL();
        userSQL = new UserSQL();
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
    public void testGameSQLgameExistsPositive() {
        // Make users
        var user1 = new User("username1", "password1", "email1");
        var user2 = new User("username2", "password2", "email2");

        // Make games
        var cgame1 = new ChessGame();
        var cgame2 = new ChessGame();

        var game1 = new Game("1", user1.getUsername(), user2.getUsername(), new HashSet<>(), "test", cgame1);
        var game2 = new Game("2", null, null, new HashSet<>(), "test", cgame2);

        // Test addGame
        try {
            gameSQL.addGame(game1);
            gameSQL.addGame(game2);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test gameExists
        try {
            Assertions.assertTrue(gameSQL.gameExists(game1));
            Assertions.assertTrue(gameSQL.gameExists(game2));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testGameSQLgameExistsNegative() {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Make games
        var cgame1 = new ChessGame();

        var game1 = new Game("1", user1.getUsername(), null, new HashSet<>(), "test", cgame1);

        // Test gameExists
        try {
            Assertions.assertFalse(gameSQL.gameExists(game1));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testGameSQLaddGamePositive() {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Make games
        var cgame1 = new ChessGame();

        var game1 = new Game("1", user1.getUsername(), null, new HashSet<>(), "test", cgame1);

        // Test addGame
        try {
            gameSQL.addGame(game1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test gameExists
        try {
            Assertions.assertTrue(gameSQL.gameExists(game1));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testGameSQLaddGameNegative() {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Make games
        var cgame1 = new ChessGame();

        var game1 = new Game("1", user1.getUsername(), null, new HashSet<>(), "test", cgame1);

        // Try adding twice
        try {
            gameSQL.addGame(game1);
            Assertions.assertThrows(ServerException.class, () -> gameSQL.addGame(game1));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testGameSQLgetGamePositive() {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Make games
        var cgame1 = new ChessGame();

        var game1 = new Game("1", user1.getUsername(), null, new HashSet<>(), "test", cgame1);

        // Test addGame
        try {
            gameSQL.addGame(game1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getGame
        try {
            Assertions.assertEquals(game1, gameSQL.getGame(game1.getGameID()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testGameSQLgetGameNegative() {
        // Make games
        var cgame1 = new ChessGame();

        var game1 = new Game("1", null, null, new HashSet<>(), "test", cgame1);

        // Test getGame
        Assertions.assertThrows(ServerException.class, () -> gameSQL.getGame(game1.getGameID()));
    }

    @Test
    public void testGameSQLgetAllGamesPositive() {
        // Make users
        var user1 = new User("username1", "password1", "email1");
        var user2 = new User("username2", "password2", "email2");

        // Make games
        var cgame1 = new ChessGame();
        var cgame2 = new ChessGame();

        var game1 = new Game("1", user1.getUsername(), user2.getUsername(), new HashSet<>(), "test", cgame1);
        var game2 = new Game("2", null, null, new HashSet<>(), "test", cgame2);

        // Test addGame
        try {
            gameSQL.addGame(game1);
            gameSQL.addGame(game2);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getAllGames
        try {
            Assertions.assertArrayEquals(new Game[]{game1, game2}, gameSQL.getAllGames());
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testGameSQLgetAllGamesNegative() {
        // Test getAllGames
        try {
            Assertions.assertArrayEquals(new Game[]{}, gameSQL.getAllGames());
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testGameSQLupdateGamePositive() {
        // Make users
        var user1 = new User("username1", "password1", "email1");
        var user2 = new User("username2", "password2", "email2");

        // Make games
        var cgame1 = new ChessGame();
        var cgame2 = new ChessGame();

        var game1 = new Game("1", user1.getUsername(), user2.getUsername(), new HashSet<>(), "test", cgame1);
        var game2 = new Game("1", null, null, new HashSet<>(), "test", cgame2);

        // Test addGame
        try {
            gameSQL.addGame(game1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test updateGame
        try {
            gameSQL.updateGame(game2);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

//        // Test getGame
//        try {
//            Assertions.assertEquals(game2, gameSQL.getGame(game2.getGameID()));
//        } catch (DataAccessException | ServerException e) {
//            Assertions.fail(e.getMessage());
//        }
    }

    @Test
    public void testGameSQLupdateGameNegative() {
        // Make games
        var cgame1 = new ChessGame();

        var game1 = new Game("1", null, null, new HashSet<>(), "test", cgame1);

        // Test updateGame
        Assertions.assertThrows(ServerException.class, () -> gameSQL.updateGame(game1));
    }

    @Test
    public void testGameSQLclear() throws ServerException {
        // Make users
        var user1 = new User("username1", "password1", "email1");

        // Make games
        var cgame1 = new ChessGame();

        var game1 = new Game("1", user1.getUsername(), null, new HashSet<>(), "test", cgame1);

        // Test addGame
        try {
            gameSQL.addGame(game1);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test clear
        gameSQL.clear();

        // Test gameExists
        try {
            Assertions.assertFalse(gameSQL.gameExists(game1));
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
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

    @Test
    public void testGameSQLPositive() throws ServerException {
        // Make users
        User user1 = new User("username1", "password1", "email1");
        User user2 = new User("username2", "password2", "email2");

        // Make games
        ChessGame cgame1 = new ChessGame();
        ChessGame cgame2 = new ChessGame();

        Game game1 = new Game("1", user1.getUsername(), user2.getUsername(), new HashSet<>(), "test", cgame1);
        Game game2 = new Game("2", null, null, new HashSet<>(), "test", cgame2);

        // Test addGame
        try {
            gameSQL.addGame(game1);
            gameSQL.addGame(game2);
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getGame
        try {
            Assertions.assertEquals(game1, gameSQL.getGame(game1.getGameID()));
            Assertions.assertEquals(game2, gameSQL.getGame(game2.getGameID()));
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test getGameList
        try {
            Assertions.assertArrayEquals(new Game[]{game1, game2}, gameSQL.getAllGames());
        } catch (DataAccessException | ServerException e) {
            Assertions.fail(e.getMessage());
        }

        // Test clear
        gameSQL.clear();

        // Test getGame
        Assertions.assertThrows(ServerException.class, () -> gameSQL.getGame(game2.getGameID()));
    }

    @Test
    public void testGameSQLNegative() throws ServerException {

    }
}