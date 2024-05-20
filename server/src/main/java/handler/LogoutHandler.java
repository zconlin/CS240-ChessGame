package handler;

import model.AuthToken;
import requestclasses.LogoutRequest;
import resultclasses.LogoutResult;
import services.LogoutService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    private final LogoutService service;

    public LogoutHandler(LogoutService service) {
        this.service = service;
    }

    public Object handle(Request request, Response response) {
        LogoutRequest javaLogoutRequestObj = this.getRequestClass(request);
        LogoutResult javaLogoutResultObj = this.service.logout(javaLogoutRequestObj);
        response.status(javaLogoutResultObj.getStatus());
        return (new Gson()).toJson(javaLogoutResultObj);
    }

    public LogoutRequest getRequestClass(Request request) {
        LogoutRequest req = new LogoutRequest();
        if (request.headers("Authorization") != null) {
            AuthToken token = new AuthToken();
            token.setAuthToken(request.headers("Authorization"));
            req.setAuthToken(token);
        }

        return req;
    }
}
