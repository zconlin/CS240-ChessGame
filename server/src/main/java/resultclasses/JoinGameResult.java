package resultclasses;


public class JoinGameResult extends Result {

    public JoinGameResult() {
        super();
    }

    public JoinGameResult(int status) {
        super(status);
    }

    public JoinGameResult(int status, String message) {
        super(status, message);
    }
}
