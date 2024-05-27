package services;

import dataaccess.*;
import requestclasses.LogoutRequest;
import resultclasses.LogoutResult;
import server.Server;
import server.ServerException;

public class LogoutService extends Service {

    public LogoutService() {
        super();
    }

    public LogoutService(AuthDAO authDAO, UserDAO userDAO) {
        super(authDAO, null, userDAO);
    }

    public LogoutResult logout(LogoutRequest request) throws ServerException {
        //Check if authentication token or username is null
        if (request.getAuthToken() == null) {
            throw new ServerException("Error: bad request", 400);
        }

        //Check if authentication token is valid
        try {
            var token = authDAO.checkAuthToken(request.getAuthToken());
        } catch (DataAccessException e) {
            throw new ServerException("Error: unauthorized", 401);
        } catch (Exception e) {
            throw new ServerException("Error: " + e.getMessage(), 500);
        }

        //Delete auth token
        try {
            authDAO.deleteAuthToken(request.getAuthToken());
        } catch (Exception e) {
            throw new ServerException("Error: " + e.getMessage(), 500);
        }

        return new LogoutResult(200);
    }
}