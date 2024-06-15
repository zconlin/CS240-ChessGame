package websocket.commands;

import services.GameService;
import websocket.WSSessionsManager;
import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import websocketmessages.usercommands.MoveCommand;
import websocketmessages.usercommands.UserGameCommand;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

public class MoveCommandHandler extends CommandHandler {

    public MoveCommandHandler(WSSessionsManager sessionsManager, GameService gameService) {
        super(sessionsManager, gameService);
    }

    public void handle(Session session, UserGameCommand command) throws IOException, InvalidMoveException, DataAccessException {
        MoveCommand moveCommand = (MoveCommand) command;
        var gameID = moveCommand.getGameID();
        var username = moveCommand.getUsername();
        var move = moveCommand.getMove();
        var game = gameService.getGame(gameID);
        ChessGame.TeamColor playerTeam;

        //Check if player is in game
        if (game.getWhiteUsername().equals(username)) {
            playerTeam = ChessGame.TeamColor.WHITE;
        } else if (game.getBlackUsername().equals(username)) {
            playerTeam = ChessGame.TeamColor.BLACK;
        } else {
            session.getRemote().sendString(gson.toJson(new websocketmessages.servermessages.ErrorMessage("Error: Player not in game")));
            return;
        }

        //Check turn
        var chessGame = game.getGame();
        if (chessGame.getTeamTurn() != playerTeam) {
            session.getRemote().sendString(gson.toJson(new websocketmessages.servermessages.ErrorMessage("Error: Not your turn")));
            return;
        }

        //Check if move is valid
        var moves = chessGame.validMoves(move.getStartPosition());
        if (moves == null) {
            session.getRemote().sendString(gson.toJson(new websocketmessages.servermessages.ErrorMessage("Error: Invalid move")));
            return;
        }

        if (!chessGame.validMoves(move.getStartPosition()).contains(move)) {
            session.getRemote().sendString(gson.toJson(new websocketmessages.servermessages.ErrorMessage("Error: Invalid move")));
            return;
        }

        //Check if game is over
        if (game.isOver()) {
            session.getRemote().sendString(gson.toJson(new websocketmessages.servermessages.ErrorMessage("Error: Game is over")));
            return;
        }

        game.getGame().makeMove(move);
        gameService.updateGame(game);
        sessionsManager.broadcast(gameID, new websocketmessages.servermessages.LoadGameMessage(game.getGame()), "");

        //flip player team
        playerTeam = playerTeam == ChessGame.TeamColor.BLACK ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

        //Broadcast move
        var message = String.format("Player %s moved %s", username, move);
        var notification = new websocketmessages.servermessages.NotificationMessage(message);
        sessionsManager.broadcast(gameID, notification, username);

        //Switch username
        username = playerTeam == ChessGame.TeamColor.WHITE ? game.getWhiteUsername() : game.getBlackUsername();

        //Check if player is in Check
        if (game.getGame().isInCheck(playerTeam)) {
            var alert = String.format("Player %s is in check", username);
            var notif = new websocketmessages.servermessages.NotificationMessage(alert);
            sessionsManager.broadcast(gameID, notif, "");
        }

        //Check if player is in Checkmate
        if (game.getGame().isInCheckmate(playerTeam)) {
            var alert = String.format("Player %s is in checkmate", username);
            var notif = new websocketmessages.servermessages.NotificationMessage(alert);
            sessionsManager.broadcast(gameID, notif, "");
        }


    }
}
