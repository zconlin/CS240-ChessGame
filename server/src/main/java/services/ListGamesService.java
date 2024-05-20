package services;

import dataaccess.*;
import requestclasses.ListGamesRequest;
import resultclasses.ListGamesResult;

public class ListGamesService extends Service {

    public ListGamesService() {
        super();
    }

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public ListGamesResult listGames(ListGamesRequest request) {
        //Check if authentication token is null
        if (request.getAuthToken() == null) {
            return new ListGamesResult(400, "Error: bad request");
        }

        //Check if authentication token is valid
        try {
            var token = authDAO.checkAuthToken(request.getAuthToken());
        } catch (Exception e) {
            return new ListGamesResult(401, "Error: unauthorized");
        }

        //List all active games
        try {
            var games = gameDAO.getAllGames();
            return new ListGamesResult(games);
        } catch (Exception e) {
            return new ListGamesResult(500, "Error: " + e.getMessage());
        }
    }
}