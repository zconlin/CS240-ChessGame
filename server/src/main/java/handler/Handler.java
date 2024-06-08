package handler;

import spark.Request;
import spark.Route;

public abstract class Handler implements Route {
    public Handler() {
    }

    public abstract requestclasses.Request getRequestClass(Request var1);
}
