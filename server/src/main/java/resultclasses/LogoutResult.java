package resultclasses;

public class LogoutResult extends Result {

    public LogoutResult() {
        super();
    }

    public LogoutResult(int status) {
        super(status);
    }

    public LogoutResult(int status, String message) {
        super(status, message);
    }
}
