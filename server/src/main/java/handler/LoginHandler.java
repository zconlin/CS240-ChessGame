package handler;

import requestclasses.LoginRequest;
import resultclasses.LoginResult;
import server.ServerException;
import services.LoginService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    private final LoginService service;

    public LoginHandler(LoginService service) {
        this.service = service;
    }

    @Override
    public Object handle(Request request, Response response) throws ServerException {
        String authToken = request.headers("authorization");
        LoginRequest javaLoginRequestObj = this.getRequestClass(request);
        LoginResult javaLoginResultObj = this.service.login(javaLoginRequestObj);
        response.status(javaLoginResultObj.getStatus());
        return new Gson().toJson(javaLoginResultObj.getAuthToken());
    }

    public LoginRequest getRequestClass(Request request) {
        LoginRequest req;
        if (request.body() == null) {
            req = new LoginRequest();
        } else {
            try {
                req = (LoginRequest)(new Gson()).fromJson(request.body(), LoginRequest.class);
            } catch (Exception e) {
                req = new LoginRequest();
            }

            if (req == null) {
                req = new LoginRequest();
            }
        }

        return req;
    }
}
