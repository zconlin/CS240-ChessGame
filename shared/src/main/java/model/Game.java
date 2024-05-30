package model;

import chess.ChessGame;

import java.util.Collection;
import java.util.HashSet;

public class Game {

    private String gameID;
    private String whiteUsername;
    private String blackUsername;
    private HashSet<String> spectators;
    private String gameName;
    private ChessGame game;

    public Game() {
        this.gameID = null;
        this.whiteUsername = "";
        this.blackUsername = "";
        this.spectators = new HashSet<>();
        this.gameName = "";
        this.game = new ChessGame();
    }

    public Game(String gameID, String whiteUsername, String blackUsername, HashSet<String> spectators, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.spectators = spectators;
        this.gameName = gameName;
        this.game = game;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public HashSet<String> getSpectators() {
        return spectators;
    }

    public void setSpectators(HashSet<String> spectators) {
        this.spectators = spectators;
    }

    public void addSpectator(String observer) {
        this.spectators.add(observer);
    }

    public void removeSpectator(String observer) {
        this.spectators.remove(observer);
    }

    public boolean isSpectator(String observer) {
        return this.spectators.contains(observer);
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ChessGame getGame() {
        return game;
    }
}
