package dataaccess;

import model.Game;
import chess.ChessGame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.ServerException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class GameSQL extends DataAccess {

    public GameSQL() {
        super();
    }

    public boolean gameExists(Game game) throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

            var sql = "SELECT * FROM chess.game WHERE gameID = ?;";
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, game.getGameID());
                var rs = stmt.executeQuery();
                return rs.next();
            } catch (Exception e) {
                throw new DataAccessException("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new ServerException("Unable to read data", 500);
        }
    }

    public void addGame(Game game) throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

            if (gameExists(game)) {
                throw new DataAccessException("Error: GameID already exists");
            }

            var sql = "INSERT INTO chess.game (gameID, whiteUsername, blackUsername, spectators, gameName, game) VALUES (?, ?, ?, ?, ?, ?);";
            try (var stmt = conn.prepareStatement(sql)) {
                var gson = new Gson();

                stmt.setString(1, game.getGameID());
                stmt.setString(2, game.getWhiteUsername());
                stmt.setString(3, game.getBlackUsername());
                stmt.setString(4, gson.toJson(game.getSpectators()));
                stmt.setString(5, game.getGameName());
                stmt.setString(6, ChessGame.toJSON(game.getGame()));
                stmt.executeUpdate();
            } catch (Exception e) {
                throw new DataAccessException("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new ServerException("Unable to read data", 500);
        }
    }

    public Game getGame(String gameID) throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

            if (!gameExists(new Game(gameID, null, null, new ArrayList<>(), null, null))) {
                throw new DataAccessException("Error: GameID does not exist");
            }

            var sql = "SELECT * FROM chess.game WHERE gameID = ?;";
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, gameID);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    var whiteUsername = rs.getString("whiteUsername");
                    var blackUsername = rs.getString("blackUsername");
                    var gameName = rs.getString("gameName");
                    var observersString = rs.getString("observers");
                    HashSet<String> observers = new Gson().fromJson(observersString, new TypeToken<HashSet<String>>() {
                    }.getType());
                    var gameJSON = rs.getString("game");
                    var gson = new GsonBuilder()
                            .registerTypeAdapter(ChessGame.class, new ChessGame.ChessGameTA())
                            .create();
                    var game = gson.fromJson(gameJSON, ChessGame.class);
                    return new Game(gameID, whiteUsername, blackUsername, observers, gameName, game);
                }
            } catch (Exception e) {
                throw new DataAccessException("Error: " + e.getMessage());
            }
            return null;
        } catch (Exception e) {
            throw new ServerException("Unable to read data", 500);
        }
    }

    public Game[] getAllGames() throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

            var result = new ArrayList<Game>();

            var sql = "SELECT * FROM chess.game;";
            try (var stmt = conn.prepareStatement(sql)) {
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    var gameID = rs.getString("gameID");
                    var whiteUsername = rs.getString("whiteUsername");
                    var blackUsername = rs.getString("blackUsername");
                    var gameName = rs.getString("gameName");
                    var observersString = rs.getString("observers");
                    HashSet<String> observers = new Gson().fromJson(observersString, new TypeToken<HashSet<String>>() {
                    }.getType());
                    var gameJSON = rs.getString("game");
                    var gson = new GsonBuilder().registerTypeAdapter(ChessGame.class, new ChessGame.ChessGameTA()).create();
                    var game = gson.fromJson(gameJSON, ChessGame.class);

                    result.add(new Game(gameID, whiteUsername, blackUsername, observers, gameName, game));
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error: " + e.getMessage());
            }
            return result.toArray(new Game[0]);
        } catch (Exception e) {
            throw new ServerException("Unable to read data", 500);
        }
    }

    public Game updateGame(Game game) throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

            if (!gameExists(game)) {
                throw new DataAccessException("Error: GameID does not exist");
            }

            var sql = "UPDATE chess.game SET whiteUsername = ?, blackUsername = ?, spectators = ?, gameName = ?, game = ? WHERE gameID = ?;";
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, game.getWhiteUsername());
                stmt.setString(2, game.getBlackUsername());
                stmt.setString(3, new Gson().toJson(game.getSpectators()));
                stmt.setString(4, game.getGameName());
                stmt.setString(5, ChessGame.toJSON(game.getGame()));
                stmt.setString(6, game.getGameID());
                stmt.executeUpdate();
            } catch (Exception e) {
                throw new DataAccessException("Error: " + e.getMessage());
            }

            return game;
        } catch (Exception e) {
            throw new ServerException("Unable to read data", 500);
        }
    }

    public void clear() throws ServerException {
        try (var conn = DatabaseManager.getConnection()) {

            var sql = "DELETE FROM chess.game;";
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new ServerException("Unable to read data", 500);
        }
    }

}
