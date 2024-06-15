package websocket.commands;

import services.GameService;
import websocket.WSSessionsManager;
import com.google.gson.Gson;

public class CommandHandler {
    protected final WSSessionsManager sessionsManager;

    protected final GameService gameService;

    protected final Gson gson = new Gson();

    public CommandHandler(WSSessionsManager sessionsManager, GameService gameService) {
        this.sessionsManager = sessionsManager;
        this.gameService = gameService;
    }
}
