package resultclasses;

import model.AuthToken;
import com.google.gson.*;

import java.lang.reflect.Type;

public class RegisterResult extends Result {

    private AuthToken authToken;

    private String username;

    public RegisterResult() {
        super();
        this.authToken = null;
        this.username = null;
    }

    public RegisterResult(AuthToken authToken, String username) {
        super(200);
        this.authToken = authToken;
        this.username = username;
    }

    public RegisterResult(int status, String message) {
        super(status, message);
        this.authToken = null;
        this.username = null;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static class RegisterResultTypeAdapter implements JsonSerializer<RegisterResult> {

        @Override
        public JsonElement serialize(RegisterResult registerResult, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            if (registerResult.getAuthToken() != null) {
                jsonObject.addProperty("authToken", registerResult.getAuthToken().getAuthToken());
            }
            if (registerResult.getUsername() != null) {
                jsonObject.addProperty("username", registerResult.getUsername());
            }
            if (registerResult.getMessage() != null) {
                jsonObject.addProperty("message", registerResult.getMessage());
            }
            if (registerResult.getStatus() != 0) {
                jsonObject.addProperty("status", registerResult.getStatus());
            }
            return jsonObject;
        }
    }
}