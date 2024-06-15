package websocket;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

import org.eclipse.jetty.websocket.api.Session;
import websocketmessages.servermessages.LoadGameMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WSSessionsManager {
    private final Map<Integer, Map<String, Session>> sessions;

    public WSSessionsManager() {
        sessions = new HashMap<>();
    }

    public void addSession(int gameId, String username, Session session) {
        if (!sessions.containsKey(gameId)) {
            sessions.put(gameId, new TreeMap<>());
        }
        sessions.get(gameId).put(username, session);
    }

    public void removeSession(int gameId, String username) {
        if (sessions.containsKey(gameId)) {
            sessions.get(gameId).remove(username);
        }
    }

    public Session getSession(int gameId, String username) {
        if (sessions.containsKey(gameId)) {
            return sessions.get(gameId).get(username);
        }
        return null;
    }

    public Map<String, Session> getSessions(int gameId) {
        if (sessions.containsKey(gameId)) {
            return sessions.get(gameId);
        }
        return null;
    }

    public void broadcast(int gameId, LoadGameMessage message, String usernameToExclude) throws IOException {
        if (sessions.containsKey(gameId)) {
            for (Map.Entry<String, Session> entry : sessions.get(gameId).entrySet()) {
                if (!entry.getKey().equals(usernameToExclude)) {
                    entry.getValue().getRemote().sendString(new Gson().toJson(message));
                }
            }
        }
    }

    public void sendMessage(int gameId, ServerMessage message, String username) throws IOException {
        if (sessions.containsKey(gameId)) {
            Session session = sessions.get(gameId).get(username);
            if (session != null) {
                session.getRemote().sendString(new Gson().toJson(message));
            }
        }
    }
}


