package requestclasses;

import model.AuthToken;

public class ClearDBRequest extends Request {
    public ClearDBRequest() {
    }

    public ClearDBRequest(String authToken) {
        super(authToken);
    }
}
