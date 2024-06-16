package websocket.commands;

import model.Game;
import services.GameService;
import websocket.WSSessionsManager;
import chess.ChessGame;
import com.google.gson.Gson;

import org.eclipse.jetty.websocket.api.Session;
import websocketmessages.usercommands.JoinPlayerCommand;

import java.io.IOException;
import java.util.Objects;

public class JoinPlayerCommandHandler extends CommandHandler {

    public JoinPlayerCommandHandler(WSSessionsManager sessionsManager, GameService gameService) {
        super(sessionsManager, gameService);
    }

    public void handle(Session session, UserGameCommand command) throws IOException {
//        JoinPlayerCommand joinPlayerCommand = (JoinPlayerCommand) command; TODO
//
//        if (joinPlayerCommand.getPlayerColor() == null) {
//            session.getRemote().sendString(new Gson().toJson(new websocketmessages.servermessages.ErrorMessage("Error: Player color not specified")));
//            return;
//        }
//
//        //Make sure GameID is valid
//        Game game;
//        try {
//            game = gameService.getGame(joinPlayerCommand.getGameID());
//        } catch (Exception e) {
//            session.getRemote().sendString(new Gson().toJson(new websocketmessages.servermessages.ErrorMessage("Error: Invalid GameID")));
//            return;
//        }
//
//        //Make sure the user is joining the right team
//        if (joinPlayerCommand.getPlayerColor() == ChessGame.TeamColor.BLACK && !Objects.equals(game.getBlackUsername(), command.getUsername())) {
//            session.getRemote().sendString(new Gson().toJson(new websocketmessages.servermessages.ErrorMessage("Error: You are not the black player")));
//            return;
//        }
//
//        if (joinPlayerCommand.getPlayerColor() == ChessGame.TeamColor.WHITE && !Objects.equals(game.getWhiteUsername(), command.getUsername())) {
//            session.getRemote().sendString(new Gson().toJson(new websocketmessages.servermessages.ErrorMessage("Error: You are not the white player")));
//            return;
//        }
//
//        sessionsManager.addSession(joinPlayerCommand.getGameID(), joinPlayerCommand.getUsername(), session);
//
//        var loadGameMessage = new websocketmessages.servermessages.LoadGameMessage(game.getGame());
//        session.getRemote().sendString(gson.toJson(loadGameMessage));
//
//        var message = String.format("Player %s joined the game", joinPlayerCommand.getUsername());
//        var notification = new websocketmessages.servermessages.NotificationMessage(message);
//        sessionsManager.broadcast(joinPlayerCommand.getGameID(), notification, joinPlayerCommand.getUsername());
    }
}
