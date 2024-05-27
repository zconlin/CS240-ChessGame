package handler;

import model.AuthToken;
import requestclasses.JoinGameRequest;
import resultclasses.JoinGameResult;
import server.ServerException;
import services.JoinGameService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
    private final JoinGameService service;

    public JoinGameHandler(JoinGameService service) {
        this.service = service;
    }

    public Object handle(Request request, Response response) throws ServerException {
        JoinGameRequest javaRequestObj = this.getRequestClass(request);
        JoinGameResult javaResultObj = this.service.joinGame(javaRequestObj);
        response.status(javaResultObj.getStatus());
        return (new Gson()).toJson(javaResultObj);
    }

    @Override
    public JoinGameRequest getRequestClass(Request request) {
        JoinGameRequest req;
        if (request.body() != null) {
            try {
                req = new Gson().fromJson(request.body(), JoinGameRequest.class);
            } catch (Exception e) {
                req = new JoinGameRequest();
            }
        } else {
            req = new JoinGameRequest();
        }

        if (request.headers("Authorization") != null) {
            var token = request.headers("Authorization");
            req.setAuthToken(token);
        }
        return req;
    }
}