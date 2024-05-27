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
        String authToken = request.headers("authorization");
        ListGamesRequest javaRequestObj = this.getRequestClass(request);
        ListGamesResult javaResultObj = this.service.listGames(javaRequestObj);
        response.status(javaResultObj.getStatus());
        return (new Gson()).toJson(javaResultObj);
    }

    public ListGamesRequest getRequestClass(Request request) {
        if (request.headers("authorization") != null) {
            String token = request.headers("authorization");
            return new ListGamesRequest(token);
        } else {
            return new ListGamesRequest();
        }
    }
}
