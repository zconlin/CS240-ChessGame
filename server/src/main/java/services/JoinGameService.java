package services;

import dataaccess.*;
import requestclasses.JoinGameRequest;
import resultclasses.JoinGameResult;

public class JoinGameService extends Service {

    public JoinGameService() {
        super();
    }

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public JoinGameResult joinGame(JoinGameRequest request) {
        //Check if authentication token or gameID are null
        if (request.getAuthToken() == null || request.getGameID() == 0) {
            return new JoinGameResult(400, "Error: bad request");
        }

        //Check if authentication token is valid
        try {
            authDAO.checkAuthToken(request.getAuthToken());
        } catch (Exception e) {
            return new JoinGameResult(401, "Error: unauthorized");
        }

        // Check if gameID exists
        try {
            var game = gameDAO.getGame(String.valueOf(request.getGameID()));
        } catch (Exception e) {
            return new JoinGameResult(400, "Error: game does not exist");
        }

        // Discover Username
        String username;
        try {
            username = authDAO.getUsername(request.getAuthToken());
        } catch (Exception e) {
            return new JoinGameResult(500, "Error: " + e.getMessage());
        }

        // Join game
        try {
            gameDAO.claimSpot(request.getGameID(), username, request.getPlayerColor());
            return new JoinGameResult(200);
        } catch (DataAccessException e) {
            return new JoinGameResult(403, "Error: already taken");
        } catch (Exception e) {
            return new JoinGameResult(500, "Error: " + e.getMessage());
        }
    }
}