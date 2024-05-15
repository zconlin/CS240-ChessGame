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
        Spark.delete("/db", this::clearDatabase);
    }

    private Object clearDatabase(Request req, Response res) {
        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
