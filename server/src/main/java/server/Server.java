package server;

import handler.LoginHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void defineRoutes(){
        Spark.post("/session", new LoginHandler());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
