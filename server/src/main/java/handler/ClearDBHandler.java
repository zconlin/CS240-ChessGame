package handler;

import requestclasses.ClearDBRequest;
import resultclasses.ClearDBResult;
import services.ClearDBService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class ClearDBHandler extends Handler {
    private final ClearDBService service;

    public ClearDBHandler(ClearDBService service) {
        this.service = service;
    }

    public ClearDBRequest getRequestClass(Request request) {
        ClearDBRequest req;
        if (request.body() == null) {
            req = new ClearDBRequest();
        } else {
            try {
                req = (ClearDBRequest)(new Gson()).fromJson(request.body(), ClearDBRequest.class);
            } catch (Exception var4) {
                req = new ClearDBRequest();
            }

            if (req == null) {
                req = new ClearDBRequest();
            }
        }

        return req;
    }

    public Object handle(Request request, Response response) {
        ClearDBRequest javaClearDBRequestObj = this.getRequestClass(request);
        ClearDBResult javaClearDBResultObj = this.service.clearDB(javaClearDBRequestObj);
        response.status(javaClearDBResultObj.getStatus());
        return (new Gson()).toJson(javaClearDBResultObj);
    }
}
