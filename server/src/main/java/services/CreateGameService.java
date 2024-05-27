package services;

import dataaccess.*;
import model.AuthToken;
import requestclasses.CreateGameRequest;
import resultclasses.CreateGameResult;

public class CreateGameService extends Service {

    public CreateGameService() {
        super();
    }

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {
        //Check if authentication token or gameID are null
        if (request.getAuthToken() == null || request.getGameName() == null) {
            return new CreateGameResult(400, "Error: bad request");
        }

        //Check if authentication token is valid
        try {
            var token = authDAO.checkAuthToken(request.getAuthToken());
        } catch (DataAccessException e) {
            return new CreateGameResult(401, "Error: unauthorized");
        }

        String username = authDAO.getUsername(request.getAuthToken());
        AuthToken authToken = authDAO.getAuthToken(request.getAuthToken());

        //Create game
        try {
            var game = new model.Game();
            game.setGameName(request.getGameName());
            gameDAO.addGame(game);
            return new CreateGameResult(game.getGameID());
        } catch (Exception e) {
            return new CreateGameResult(500, "Error: " + e.getMessage());
        }
    }
}