package websocket;

import model.AuthToken;
import services.AuthService;
import services.GameService;
import websocket.commands.*;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;

import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocketmessages.usercommands.*;

import java.io.IOException;

import static websocketmessages.usercommands.UserGameCommand.CommandType.JOIN_SPECTATOR;
import static websocketmessages.usercommands.UserGameCommand.CommandType.JOIN_PLAYER;


@WebSocket
public class WSServer {

    // Sessions Manager
    private final WSSessionsManager sessionsManager = new WSSessionsManager();

    // Auth Checker
    private final AuthService authService;

    // Game update Service
    private final GameService gameService;

    //Handlers
    private final JoinPlayerCommandHandler joinPlayerCommandHandler;
    private final JoinSpectatorCommandHandler joinSpectatorCommandHandler;
    private final LeaveCommandHandler leaveCommandHandler;
    private final MoveCommandHandler moveCommandHandler;
    private final ResignCommandHandler resignCommandHandler;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
        websocketmessages.usercommands.UserGameCommand command = new Gson().fromJson(message, websocketmessages.usercommands.UserGameCommand.class);
        var tmp = new AuthToken(command.getAuthString(), "");

        //Check AuthToken
        try {
            if (!authService.validateAuthToken(tmp)) {
                session.getRemote().sendString(new Gson().toJson(new websocketmessages.servermessages.ErrorMessage("AuthToken Error: Invalid AuthToken")));
                return;
            }
        } catch (Exception e) {
            session.getRemote().sendString(new Gson().toJson(new websocketmessages.servermessages.ErrorMessage("AuthToken Error: " + e.getMessage())));
            return;
        }

        var trueAuthToken = new AuthToken(command.getAuthString(), authService.getUsername(tmp));

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                command = new Gson().fromJson(message, websocketmessages.usercommands.UserGameCommand.class);
                command.setAuthToken(trueAuthToken);
                joinPlayerCommandHandler.handle(session, command);
            }
            case JOIN_SPECTATOR -> {
                command = new Gson().fromJson(message, websocketmessages.usercommands.UserGameCommand.class);
                command.setAuthToken(trueAuthToken);
                joinSpectatorCommandHandler.handle(session, command);
            }
            case LEAVE -> {
                command = new Gson().fromJson(message, websocketmessages.usercommands.UserGameCommand.class);
                command.setAuthToken(trueAuthToken);
                leaveCommandHandler.handle(command);
            }
            case MAKE_MOVE -> {
                var gson = new GsonBuilder().registerTypeAdapter(ChessMove.class, new ChessMove.ChessMoveTA()).create();
                command = gson.fromJson(message, websocketmessages.usercommands.UserGameCommand.class);
                command.setAuthToken(trueAuthToken);
                moveCommandHandler.handle(session, command);
            }
            case RESIGN -> {
                command = new Gson().fromJson(message, websocketmessages.usercommands.UserGameCommand.class);
                command.setAuthToken(trueAuthToken);
                resignCommandHandler.handle(session, command);
            }
        }
    }

    //Required methods for WebSocket, only logging for now
    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to " + session.toString());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("Closed: " + statusCode + " " + reason);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        System.out.println("Error: " + error.getMessage());
    }
    
    public WSServer(AuthService authService, GameService gameService) {
        this.gameService = gameService;
        this.authService = authService;
        this.joinPlayerCommandHandler = new JoinPlayerCommandHandler(sessionsManager, gameService);
        this.joinSpectatorCommandHandler = new JoinSpectatorCommandHandler(sessionsManager, gameService);
        this.leaveCommandHandler = new LeaveCommandHandler(sessionsManager, gameService);
        this.moveCommandHandler = new MoveCommandHandler(sessionsManager, gameService);
        this.resignCommandHandler = new ResignCommandHandler(sessionsManager, gameService);
    }
}
