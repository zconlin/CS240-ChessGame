package handler;

import requestclasses.LoginRequest;
import resultclasses.LoginResult;
import services.LoginService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    private final LoginService service;

    public LoginHandler(LoginService service) {
        this.service = service;
    }

    public Object handle(Request request, Response response) {
        LoginRequest javaRequestObj = this.getRequestClass(request);
        LoginResult javaResultObj = this.service.login(javaRequestObj);
        response.status(javaResultObj.getStatus());
        return (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create().toJson(javaResultObj);
    }

    public LoginRequest getRequestClass(Request request) {
        LoginRequest req;
        if (request.body() == null) {
            req = new LoginRequest();
        } else {
            try {
                req = (LoginRequest)(new Gson()).fromJson(request.body(), LoginRequest.class);
            } catch (Exception var4) {
                req = new LoginRequest();
            }

            if (req == null) {
                req = new LoginRequest();
            }
        }

        return req;
    }
}
