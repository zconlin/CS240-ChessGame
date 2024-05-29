package handler;

import dataaccess.DataAccessException;
import model.AuthToken;
import requestclasses.CreateGameRequest;
import resultclasses.CreateGameResult;
import server.ServerException;
import services.CreateGameService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class CreateGameHandler2 extends Handler {
    // IntelliJ is being weird about making a CreateGameHandler class, but putting a 2 works fine. Reminder to go fix that eventually
    private final CreateGameService service;

    public CreateGameHandler2(CreateGameService service) {
        this.service = service;
    }

    @Override
    public Object handle(Request request, Response response) throws DataAccessException, ServerException {
        CreateGameRequest javaCreateGameRequestObj = this.getRequestClass(request);
        CreateGameResult javaCreateGameResultObj = this.service.createGame(javaCreateGameRequestObj);
        response.status(javaCreateGameResultObj.getStatus());
        return (new Gson()).toJson(javaCreateGameResultObj);
    }

    public CreateGameRequest getRequestClass(Request request) {
        CreateGameRequest req = null;
        if (request.body() != null) {
            try {
                req = new com.google.gson.Gson().fromJson(request.body(), CreateGameRequest.class);
            } catch (Exception e) {
                req = new CreateGameRequest();
            }
        }
        if (req == null) {
            req = new CreateGameRequest();
        }
        if (request.headers("Authorization") != null) {
            var token = request.headers("Authorization");
//            token.setAuthToken(request.headers("Authorization"));
            req.setAuthToken(token);
        }

        return req;
    }
}