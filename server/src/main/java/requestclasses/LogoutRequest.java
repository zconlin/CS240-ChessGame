package requestclasses;

import model.AuthToken;

public class LogoutRequest extends Request {

        public LogoutRequest() {
            super();
        }

        public LogoutRequest(AuthToken authToken) {
            super(authToken);
        }
}
