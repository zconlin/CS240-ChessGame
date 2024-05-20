package resultclasses;

import model.AuthToken;

public class LoginResult extends Result {

    private AuthToken authToken;

    public LoginResult() {
        super();
        this.authToken = null;
    }

    public LoginResult(AuthToken authToken) {
        super(200);
        this.authToken = authToken;
    }

    public LoginResult(int status, String message) {
        super(status, message);
        this.authToken = null;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
