package handler;

import model.AuthToken;
import requestclasses.LogoutRequest;
import resultclasses.LogoutResult;
import server.ServerException;
import services.LogoutService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    private final LogoutService service;

    public LogoutHandler(LogoutService service) {
        this.service = service;
    }

    public Object handle(Request request, Response response) throws ServerException {
        String authToken = request.headers("authorization");
        LogoutRequest javaLogoutRequestObj = this.getRequestClass(request);
        LogoutResult javaLogoutResultObj = this.service.logout(javaLogoutRequestObj);
        response.status(javaLogoutResultObj.getStatus());
        return new Gson().toJson(javaLogoutResultObj.getAuthToken());
    }

    public LogoutRequest getRequestClass(Request request) {
        LogoutRequest req = new LogoutRequest();
        if (request.headers("authorization") != null) {
            String token = request.headers("authorization");
            req.setAuthToken(token);
        }

        return req;
    }
}
