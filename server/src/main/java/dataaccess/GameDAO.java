package dataaccess;

import model.Game;

import java.util.HashMap;
import java.util.HashSet;

public class GameDAO extends DataAccess {

    private HashMap<String, Game> games = new HashMap<>();

    public GameDAO() {
        super();
    }

    public void addGame(Game game) throws DataAccessException {
    }

    public Game getGame(String gameID) throws DataAccessException {
        return null;
    }

    public HashSet<Game> getAllGames() throws DataAccessException {
        return null;
    }

    public Game updateGame(Game game) throws DataAccessException {
        return null;
    }

    public void claimSpot(String gameID, String playerID) throws DataAccessException {
    }

    public void claimSpot(int id, String gameID, String playerID) throws DataAccessException {
    }

    public void clear(){
    }

    public void deleteGame(Game game) throws DataAccessException {
    }

}
