package requestclasses;

import model.AuthToken;

public class Request {

    private AuthToken authToken;

    public Request() {
        this.authToken = null;
    }

    public Request(AuthToken authToken) {
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}