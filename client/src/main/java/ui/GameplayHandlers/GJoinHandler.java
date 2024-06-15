package ui.GameplayHandlers;

import exceptions.ResponseException;
import model.AuthToken;
import chess.ChessGame;
import wsfacade.WSFacade;

public class GJoinHandler extends OnlineGameHandler {

    public GJoinHandler(WSFacade wsFacade, AuthToken authToken) {
        super(wsFacade, authToken);
    }

    public void joinGame(Integer gameId, ChessGame.TeamColor teamColor) throws ResponseException {
        wsFacade.sendJoinPlayer(authToken, gameId, teamColor);
    }
}
