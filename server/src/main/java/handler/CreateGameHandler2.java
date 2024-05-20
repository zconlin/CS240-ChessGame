package handler;

import model.AuthToken;
import requestclasses.CreateGameRequest;
import resultclasses.CreateGameResult;
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

    public Object handle(Request request, Response response) {
        CreateGameRequest javaCreateGameRequestObj = this.getRequestClass(request);
        CreateGameResult javaCreateGameResultObj = this.service.createGame(javaCreateGameRequestObj);
        response.status(javaCreateGameResultObj.getStatus());
        return (new Gson()).toJson(javaCreateGameResultObj);
    }

    public CreateGameRequest getRequestClass(Request request) {
        CreateGameRequest req = null;
        if (request.body() != null) {
            try {
                req = (CreateGameRequest)(new Gson()).fromJson(request.body(), CreateGameRequest.class);
            } catch (Exception var4) {
                req = new CreateGameRequest();
            }
        }

        if (req == null) {
            req = new CreateGameRequest();
        }

        if (request.headers("Authorization") != null) {
            AuthToken token = new AuthToken();
            token.setAuthToken(request.headers("Authorization"));
            req.setAuthToken(token);
        }

        return req;
    }
}
