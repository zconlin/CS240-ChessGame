package websocket.commands;

import model.Game;
import services.GameService;
import websocket.WSSessionsManager;
import com.google.gson.Gson;
import websocketmessages.usercommands.JoinSpectatorCommand;
import websocketmessages.usercommands.UserGameCommand;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

public class JoinSpectatorCommandHandler extends CommandHandler {

    public JoinSpectatorCommandHandler(WSSessionsManager sessionsManager, GameService gameService) {
        super(sessionsManager, gameService);
    }

    public void handle(Session session, UserGameCommand command) throws IOException {
        JoinSpectatorCommand joinSpectatorCommand = (JoinSpectatorCommand) command;

        Game game;
        try {
            game = gameService.getGame(joinSpectatorCommand.getGameID());
        } catch (Exception e) {
            session.getRemote().sendString(new Gson().toJson(new websocketmessages.servermessages.ErrorMessage("Error: Invalid GameID")));
            return;
        }

        sessionsManager.addSession(joinSpectatorCommand.getGameID(), joinSpectatorCommand.getUsername(), session);

        var loadGameMessage = new websocketmessages.servermessages.LoadGameMessage(game.getGame());
        session.getRemote().sendString(gson.toJson(loadGameMessage));

        var message = String.format("Player %s is now observing", joinSpectatorCommand.getUsername());
        var notification = new websocketmessages.servermessages.NotificationMessage(message);
//        sessionsManager.broadcast(joinSpectatorCommand.getGameID(), notification, joinSpectatorCommand.getUsername()); TODO
    }
}
