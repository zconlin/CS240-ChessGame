package requestclasses;

import model.AuthToken;

public class Request {

    private String authToken;

    public Request() {
        this.authToken = null;
    }

    public Request(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}