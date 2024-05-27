package service;

import dataaccess.*;
import handler.*;
import requestclasses.RegisterRequest;
import server.ServerException;
import services.*;
import org.junit.jupiter.api.*;
import server.Server;

public class ServiceTests{
    // Server
    private Server server;

    // dataaccess
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    // Services
    private LoginService loginService;
    private ClearDBService clearDBService;
    private RegisterService registerService;
    private CreateGameService createGameService;
    private JoinGameService joinGameService;
    private ListGamesService listGamesService;
    private LogoutService logoutService;

    // Handlers
    private LoginHandler loginHandler;
    private LogoutHandler logoutHandler;
    private ClearDBHandler clearDBHandler;
    private RegisterHandler registerHandler;
    private CreateGameHandler2 createGameHandler;
    private JoinGameHandler joinGameHandler;
    private ListGamesHandler listGamesHandler;


    @BeforeAll
    public static void setUpAll() {
        //Init databases
    }

    @BeforeEach
    public void setUp() {
        //Init dataaccess
        this.authDAO = new AuthDAO();
        this.gameDAO = new GameDAO();
        this.userDAO = new UserDAO();

        //Init services
        this.loginService = new LoginService(this.authDAO, this.userDAO);
        this.clearDBService = new ClearDBService(this.authDAO, this.gameDAO, this.userDAO);
        this.createGameService = new CreateGameService(this.authDAO, this.gameDAO, this.userDAO);
        this.joinGameService = new JoinGameService(this.authDAO, this.gameDAO, this.userDAO);
        this.listGamesService = new ListGamesService(this.authDAO, this.gameDAO, this.userDAO);
        this.registerService = new RegisterService(this.authDAO, this.userDAO);
        this.logoutService = new LogoutService(this.authDAO, this.userDAO);

        //Init Handlers
        this.loginHandler = new LoginHandler(this.loginService);
        this.clearDBHandler = new ClearDBHandler(this.clearDBService);
        this.registerHandler = new RegisterHandler(this.registerService);
        this.createGameHandler = new CreateGameHandler2(this.createGameService);
        this.joinGameHandler = new JoinGameHandler(this.joinGameService);
        this.listGamesHandler = new ListGamesHandler(this.listGamesService);
        this.logoutHandler = new LogoutHandler(this.logoutService);

        // Server
        this.server = new Server();
    }

//    @Test
//    public void testClearDBService_positive(){
//        // Add a token to the database
//        var temp = new AuthToken("test", "test");
//        try {
//            this.authDAO.addAuthToken(temp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Add a user to the database
//        var tempUser = new User("test", "test", "test");
//        try {
//            this.userDAO.addUser(tempUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        // Add a game to the database
////        var tempGame = new model.Game(123, "test", "test", new ArrayList<>(), "test", new ChessGame());
////        try {
////            this.gameDAO.addGame(tempGame);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        // Clear the database
//        var tempRequest = new requestclasses.ClearDBRequest();
//        var tempResult = this.clearDBService.clearDB(tempRequest);
//
//        // Check that tempResult is correct
//        Assertions.assertEquals(200, tempResult.getStatus());
//
//        // Check that the database is empty
//        Assertions.assertThrows(DataAccessException.class, () -> this.authDAO.checkAuthToken(temp));
//        Assertions.assertThrows(DataAccessException.class, () -> this.userDAO.getUser(tempuser.username()));
////        Assertions.assertThrows(DataAccessException.class, () -> this.gameDAO.getGame(tempGame.getGameID()));
//    }

    @Test
    public void testRegisterServicePositive() {
        // Register a user
        var tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        var tempResult = this.registerService.register(tempRequest);

        // Check that tempResult is correct
        Assertions.assertEquals(200, tempResult.getStatus());
        Assertions.assertEquals("test", tempResult.getUsername());
        Assertions.assertNotNull(tempResult.getAuthToken());

        // Check that the user was added to the database
        try {
            var user = this.userDAO.getUser("test");
            Assertions.assertNotNull(user);
            Assertions.assertEquals("test", user.username());
            Assertions.assertEquals("test", user.password());
            Assertions.assertEquals("test", user.email());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception thrown");
        }

        // Check that the auth token was added to the database
        try {
            var exists = this.authDAO.checkAuthToken(tempResult.getAuthToken().getAuthToken());
            Assertions.assertTrue(exists);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception thrown");
        }
    }

    @Test
    public void testRegisterServiceNegative(){
        // Register a user
        var tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        var tempResult = this.registerService.register(tempRequest);

        // Check that tempResult is correct
        Assertions.assertEquals(200, tempResult.getStatus());
        Assertions.assertEquals("test", tempResult.getUsername());
        Assertions.assertNotNull(tempResult.getAuthToken());

        // Check that the user was added to the database
        try {
            var user = this.userDAO.getUser("test");
            Assertions.assertNotNull(user);
            Assertions.assertEquals("test", user.username());
            Assertions.assertEquals("test", user.password());
            Assertions.assertEquals("test", user.email());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception thrown");
        }

        // Try adding the same user again
        tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        tempResult = this.registerService.register(tempRequest);

        // Check that tempResult is correct
        Assertions.assertEquals(403, tempResult.getStatus());
        Assertions.assertEquals("Error: already taken", tempResult.getMessage());
        Assertions.assertNull(tempResult.getUsername());
        Assertions.assertNull(tempResult.getAuthToken());
    }

    @Test
    public void testLoginServicePositive() throws ServerException {
        // Register a user
        var tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        var tempResult = this.registerService.register(tempRequest);

        // Check that tempResult is correct
        Assertions.assertEquals(200, tempResult.getStatus());
        Assertions.assertEquals("test", tempResult.getUsername());
        Assertions.assertNotNull(tempResult.getAuthToken());

        // Check that the user was added to the database
        try {
            var user = this.userDAO.getUser("test");
            Assertions.assertNotNull(user);
            Assertions.assertEquals("test", user.username());
            Assertions.assertEquals("test", user.password());
            Assertions.assertEquals("test", user.email());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception thrown");
        }

        // Login the user
        var loginRequest = new requestclasses.LoginRequest("test", "test");
        var loginResult = this.loginService.login(loginRequest);

        // Check that tempResult is correct
        Assertions.assertEquals(200, loginResult.getStatus());
        Assertions.assertNotNull(loginResult.getAuthToken());

        // Check that the auth token was added to the database
        try {
            var exists = this.authDAO.checkAuthToken(loginResult.getAuthToken().getAuthToken());
            Assertions.assertTrue(exists);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception thrown");
        }
    }

    @Test
    public void testLoginServiceNegative() throws ServerException {
        // Register a user
        var tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        var tempResult = this.registerService.register(tempRequest);

        // Check that the user was added to the database
        try {
            var user = this.userDAO.getUser("test");
            Assertions.assertNotNull(user);
            Assertions.assertEquals("test", user.username());
            Assertions.assertEquals("test", user.password());
            Assertions.assertEquals("test", user.email());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception thrown");
        }

        // Login the user
        var loginRequest = new requestclasses.LoginRequest("test", "wrong");

        Assertions.assertThrows(ServerException.class, ()->loginService.login(loginRequest));
    }

    @Test
    public void testLogoutServicePositive() throws ServerException {
        // Register a user
        var tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        var tempResult = this.registerService.register(tempRequest);

        // Check that tempResult is correct
        Assertions.assertEquals(200, tempResult.getStatus());
        Assertions.assertEquals("test", tempResult.getUsername());
        Assertions.assertNotNull(tempResult.getAuthToken());

        // Try to logout
        var logoutRequest = new requestclasses.LogoutRequest(tempResult.getAuthToken().getAuthToken());
        var logoutResult = this.logoutService.logout(logoutRequest);

        // Check that logoutResult is correct
        Assertions.assertEquals(200, logoutResult.getStatus());

        // Check that the auth token was removed from the database
        Assertions.assertThrows(DataAccessException.class, () -> this.authDAO.checkAuthToken(tempResult.getAuthToken().getAuthToken()));
    }

    @Test
    public void testLogoutServiceNegative() throws ServerException {
        // Try to logout non-existent user
        var logoutRequest = new requestclasses.LogoutRequest("test");

        // Check that logoutResult is correct
        Assertions.assertThrows(ServerException.class, () -> logoutService.logout(logoutRequest));
    }

    @Test
    public void testCreateGameServicePositive() throws DataAccessException {
        // Register a user
        var tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        var tempResult = this.registerService.register(tempRequest);
        var authToken = tempResult.getAuthToken();

        // Create a game
        var createGameRequest = new requestclasses.CreateGameRequest(authToken.getAuthToken(), "test");
        var createGameResult = this.createGameService.createGame(createGameRequest);

        // Check that createGameResult is correct
        Assertions.assertEquals(200, createGameResult.getStatus());
        Assertions.assertNotNull(createGameResult.getGameID());

        // Check that the game was added to the database
        try {
            var game = this.gameDAO.getGame(createGameResult.getGameID());
            Assertions.assertNotNull(game);
            Assertions.assertEquals("test", game.getGameName());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception thrown");
        }
    }

    @Test
    public void testCreateGameServiceNegative() throws DataAccessException {
        // Attempt to create a game without logging in
        var createGameRequest = new requestclasses.CreateGameRequest(null, "test");
        var createGameResult = this.createGameService.createGame(createGameRequest);

        // Check that createGameResult is correct
        Assertions.assertEquals(400, createGameResult.getStatus());
    }

    @Test
    public void testListGamesServicePositive() throws DataAccessException {
        // Register a User
        var tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        var tempResult = this.registerService.register(tempRequest);
        var authToken = tempResult.getAuthToken();

        // Create some Games
        var createGameRequest = new requestclasses.CreateGameRequest(authToken.getAuthToken(), "test");
        var createGameResult = this.createGameService.createGame(createGameRequest);
        var gameID1 = createGameResult.getGameID();

        createGameRequest = new requestclasses.CreateGameRequest(authToken.getAuthToken(), "test");
        createGameResult = this.createGameService.createGame(createGameRequest);
        var gameID2 = createGameResult.getGameID();

        // List the Games
        var listGamesRequest = new requestclasses.ListGamesRequest(authToken.getAuthToken());
        var listGamesResult = this.listGamesService.listGames(listGamesRequest);

        // Check that listGamesResult is correct
        Assertions.assertEquals(200, listGamesResult.getStatus());
        Assertions.assertNotNull(listGamesResult.getGames());
        Assertions.assertEquals(2, listGamesResult.getGames().length);
    }

    @Test
    public void testListGamesServiceNegative() {
        // Attempt to list games without logging in
        var listGamesRequest = new requestclasses.ListGamesRequest(null);
        var listGamesResult = this.listGamesService.listGames(listGamesRequest);

        // Check that listGamesResult is correct
        Assertions.assertEquals(400, listGamesResult.getStatus());
    }

    @Test
    public void testJoinGameServicePositive() throws DataAccessException, ServerException {
        // Register a User
        var tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        var tempResult = this.registerService.register(tempRequest);
        var authToken = tempResult.getAuthToken();

        // Create a Game
        var createGameRequest = new requestclasses.CreateGameRequest(authToken.getAuthToken(), "test");
        var createGameResult = this.createGameService.createGame(createGameRequest);
        Integer gameID = Integer.valueOf(createGameResult.getGameID());

        // Join the Game
        var joinGameRequest = new requestclasses.JoinGameRequest(authToken.getAuthToken(), gameID, "WHITE");
        var joinGameResult = this.joinGameService.joinGame(joinGameRequest);

        // Check that joinGameResult is correct
        Assertions.assertEquals(200, joinGameResult.getStatus());

        // Check that the game was updated in the database
        try {
            var game = this.gameDAO.getGame(String.valueOf(gameID));
            Assertions.assertNotNull(game);
            Assertions.assertEquals("test", game.getGameName());
            Assertions.assertEquals("test", game.getWhiteUsername());
            Assertions.assertNull(game.getBlackUsername());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception thrown");
        }
    }

    @Test
    public void testJoinGameServiceNegative() throws DataAccessException, ServerException {
        // Register a User
        var tempRequest = new requestclasses.RegisterRequest("test", "test", "test");
        var tempResult = this.registerService.register(tempRequest);
        var authToken = tempResult.getAuthToken();

        // Create a Game
        var createGameRequest = new requestclasses.CreateGameRequest(authToken.getAuthToken(), "test");
        var createGameResult = this.createGameService.createGame(createGameRequest);
        Integer gameID = Integer.valueOf(createGameResult.getGameID());

        // Join the Game
        var joinGameRequest = new requestclasses.JoinGameRequest(authToken.getAuthToken(), gameID, "WHITE");
        var joinGameResult = this.joinGameService.joinGame(joinGameRequest);

        // Check that joinGameResult is correct
        Assertions.assertEquals(200, joinGameResult.getStatus());

        // Join the same spot with new player
        var player2req = new RegisterRequest("test2", "test2", "test2");
        var player2res = this.registerService.register(player2req);
        var player2token = player2res.getAuthToken();
        var joinGameRequest2 = new requestclasses.JoinGameRequest(player2token.getAuthToken(), gameID, "WHITE");
        Assertions.assertThrows(ServerException.class, ()->joinGameService.joinGame(joinGameRequest2));

    }
}