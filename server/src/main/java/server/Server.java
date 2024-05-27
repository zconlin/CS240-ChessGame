package server;

import dataaccess.*;
import handler.*;
import services.*;
import spark.Spark;
import server.ServerException;

public class Server {
    private final AuthDAO authDAO = new AuthDAO();
    private final GameDAO gameDAO = new GameDAO();
    private final UserDAO userDAO = new UserDAO();
    private final LoginService loginService;
    private final ClearDBService clearDBService;
    private final RegisterService registerService;
    private final CreateGameService createGameService;
    private final JoinGameService joinGameService;
    private final ListGamesService listGamesService;
    private final LogoutService logoutService;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;
    private final ClearDBHandler clearDBHandler;
    private final RegisterHandler registerHandler;
    private final CreateGameHandler2 createGameHandler;
    private final JoinGameHandler joinGameHandler;
    private final ListGamesHandler listGamesHandler;

    public static void main(String[] args) {
        (new Server()).run(8080);
    }

    public Server() {
        this.loginService = new LoginService(this.authDAO, this.userDAO);
        this.clearDBService = new ClearDBService(this.authDAO, this.gameDAO, this.userDAO);
        this.createGameService = new CreateGameService(this.authDAO, this.gameDAO, this.userDAO);
        this.joinGameService = new JoinGameService(this.authDAO, this.gameDAO, this.userDAO);
        this.listGamesService = new ListGamesService(this.authDAO, this.gameDAO, this.userDAO);
        this.registerService = new RegisterService(this.authDAO, this.userDAO);
        this.logoutService = new LogoutService(this.authDAO, this.userDAO);
        this.loginHandler = new LoginHandler(this.loginService);
        this.clearDBHandler = new ClearDBHandler(this.clearDBService);
        this.registerHandler = new RegisterHandler(this.registerService);
        this.createGameHandler = new CreateGameHandler2(this.createGameService);
        this.joinGameHandler = new JoinGameHandler(this.joinGameService);
        this.listGamesHandler = new ListGamesHandler(this.listGamesService);
        this.logoutHandler = new LogoutHandler(this.logoutService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        defineRoutes();

        Spark.exception(ServerException.class, this::handleException);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void defineRoutes() {
        Spark.delete("/db", this.clearDBHandler);
        Spark.post("/user", this.registerHandler);
        Spark.post("/session", this.loginHandler);
        Spark.delete("/session", this.logoutHandler);
        Spark.post("/game", this.createGameHandler);
        Spark.get("/game", this.listGamesHandler);
        Spark.put("/game", this.joinGameHandler);
    }

    private void handleException(ServerException exception, spark.Request request, spark.Response response) {
        response.status(exception.getStatusCode());
        response.body(exception.getMessage());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
