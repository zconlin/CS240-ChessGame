package handler;

import model.AuthToken;
import requestclasses.ListGamesRequest;
import resultclasses.ListGamesResult;
import services.ListGamesService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler {
    private final ListGamesService service;

    public ListGamesHandler(ListGamesService service) {
        this.service = service;
    }

    public Object handle(Request request, Response response) {
        ListGamesRequest javaRequestObj = this.getRequestClass(request);
        ListGamesResult javaResultObj = this.service.listGames(javaRequestObj);
        response.status(javaResultObj.getStatus());
        return (new Gson()).toJson(javaResultObj);
    }

    public ListGamesRequest getRequestClass(Request request) {
        if (request.headers("Authorization") != null) {
            AuthToken token = new AuthToken();
            token.setAuthToken(request.headers("Authorization"));
            return new ListGamesRequest(token);
        } else {
            return new ListGamesRequest();
        }
    }
}
