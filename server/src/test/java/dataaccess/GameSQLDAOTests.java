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

public class GameSQLDAOTests {
    // Database
    private DatabaseManager db;

    // SQL DAOs
    private GameSQL gameSQL;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.clear();
        gameSQL = new GameSQL();
    }

    @AfterAll
    public static void tearDown() throws DataAccessException {
        DatabaseManager db = new DatabaseManager();
        db.clear();
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