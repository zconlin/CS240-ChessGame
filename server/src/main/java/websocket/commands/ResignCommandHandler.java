package websocket.commands;

import services.GameService;
import websocket.WSSessionsManager;
import dataaccess.DataAccessException;
import websocketmessages.usercommands.UserGameCommand;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.Objects;

public class ResignCommandHandler extends CommandHandler {

    public ResignCommandHandler(WSSessionsManager sessionsManager, GameService gameService) {
        super(sessionsManager, gameService);
    }

    public void handle(Session session, UserGameCommand command) throws DataAccessException, IOException {
        var resignCommand = (websocketmessages.usercommands.ResignCommand) command;
        var gameID = resignCommand.getGameID();
        var username = resignCommand.getUsername();
        var game = gameService.getGame(gameID);

        if (!Objects.equals(game.getBlackUsername(), username) && !Objects.equals(game.getWhiteUsername(), username)) {
            session.getRemote().sendString(gson.toJson(new websocketmessages.servermessages.ErrorMessage("Error: Player not in game")));
            return;
        }

        if (game.isOver()) {
            session.getRemote().sendString(gson.toJson(new websocketmessages.servermessages.ErrorMessage("Error: Game is over")));
            return;
        }

        game.setOver();

        gameService.updateGame(game);

        var message = String.format("Player %s forfeits", username);
        var notification = new websocketmessages.servermessages.NotificationMessage(message);
//        sessionsManager.broadcast(gameID, notification, ""); TODO
    }
}
