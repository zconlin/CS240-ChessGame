package handler;

import requestclasses.RegisterRequest;
import resultclasses.RegisterResult;
import services.RegisterService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {

    private final RegisterService service;

    public RegisterHandler(RegisterService service) {
        this.service = service;
    }

    @Override
    public Object handle(Request request, Response response) {
        // Get the request object
        RegisterRequest javaRegisterRequestObj = this.getRequestClass(request);
        // Call the service object and get the result object
        RegisterResult javaRegisterResultObj = service.register(javaRegisterRequestObj);

        // Unpack the Result Object into a Spark Result
        response.status(javaRegisterResultObj.getStatus());

        Gson gson = new GsonBuilder().registerTypeAdapter(RegisterResult.class, new RegisterResult.RegisterResultTypeAdapter()).create();
        return gson.toJson(javaRegisterResultObj);
    }

    @Override
    public RegisterRequest getRequestClass(Request request) {
        RegisterRequest req;
        if (request.body() == null) {
            req = new RegisterRequest();
        } else {
            try {
                req = new Gson().fromJson(request.body(), RegisterRequest.class);
            } catch (Exception e) {
                req = new RegisterRequest();
            }
            if (req == null) {
                req = new RegisterRequest();
            }
        }
        return req;
    }
}