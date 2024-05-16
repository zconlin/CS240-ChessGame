package server;

import spark.*;

public class Server {

    public Server() {    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        defineRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void defineRoutes(){
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::makeGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearDatabase);
    }

    private Object registerUser(Request req, Response res) {
        return "";
    }

    private Object loginUser(Request req, Response res) {
        return "";
    }

    private Object logoutUser(Request req, Response res) {
        return "";
    }

    private Object listGames(Request req, Response res) {
        return "";
    }

    private Object makeGame(Request req, Response res) {
        return "";
    }

    private Object joinGame(Request req, Response res) {
        return "";
    }

    private Object clearDatabase(Request req, Response res) {
        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
