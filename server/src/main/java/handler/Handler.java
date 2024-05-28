package handler;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class Handler implements Route {
    public Handler() {
    }

//    public abstract Object handle(Request var1, Response var2) throws Exception;

    public abstract requestclasses.Request getRequestClass(Request var1);
}
