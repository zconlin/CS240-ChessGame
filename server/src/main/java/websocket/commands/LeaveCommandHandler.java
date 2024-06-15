package websocket.commands;

import services.GameService;
import websocket.WSSessionsManager;
import dataaccess.DataAccessException;
import websocketmessages.usercommands.LeaveCommand;
import websocketmessages.usercommands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;

public class LeaveCommandHandler extends CommandHandler {

    public LeaveCommandHandler(WSSessionsManager sessionsManager, GameService gameService) {
        super(sessionsManager, gameService);
    }

    public void handle(UserGameCommand command) throws IOException, DataAccessException {
        LeaveCommand leaveCommand = (LeaveCommand) command;
        var gameID = leaveCommand.getGameID();
        var username = leaveCommand.getUsername();
        sessionsManager.removeSession(gameID, username);

        var game = gameService.getGame(gameID);
        if (Objects.equals(game.getWhiteUsername(), username)) {
            game.setWhiteUsername(null);
        } else if (Objects.equals(game.getBlackUsername(), username)) {
            game.setBlackUsername(null);
        } else {
            game.getSpectators().remove(username);
        }

        gameService.updateGame(game);

        var message = String.format("Player %s left the game", username);
        var notification = new websocketmessages.servermessages.NotificationMessage(message);
        sessionsManager.broadcast(gameID, notification, username);
    }
}
