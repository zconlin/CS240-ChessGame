package resultclasses;

import model.Game;

import java.util.Collection;

public class ListGamesResult extends Result {

    private Collection<Game> games;

    public ListGamesResult() {
        super();
    }

    public ListGamesResult(Collection<Game> games) {
        super(200);
        this.games = games;
    }

    public ListGamesResult(int status, String message) {
        super(status, message);
    }

    public Collection<Game> getGames() {
        return games;
    }

    public void setGames(Collection<Game> games) {
        this.games = games;
    }
}
