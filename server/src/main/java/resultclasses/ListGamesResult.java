package resultclasses;

import model.Game;
import model.GamesList;

import java.util.Collection;

public class ListGamesResult extends Result {

    private Game[] games;

    public ListGamesResult() {
        super();
    }

    public ListGamesResult(Game[] games) {
        super(200);
        this.games = games;
    }

    public ListGamesResult(int status, String message) {
        super(status, message);
    }

    public Game[] getGames() {
        return games;
    }

    public void setGames(Game[] games) {
        this.games = games;
    }
}
