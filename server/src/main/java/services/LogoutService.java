package services;

import dataaccess.*;
import requestclasses.LogoutRequest;
import resultclasses.LogoutResult;

public class LogoutService extends Service {

    public LogoutService() {
        super();
    }

    public LogoutService(AuthDAO authDAO, UserDAO userDAO) {
        super(authDAO, null, userDAO);
    }

    public LogoutResult logout(LogoutRequest request) {
        //Check if authentication token is null
        if (request.getAuthToken() == null) {
            return new LogoutResult(400, "Error: bad request");
        }

        //Check if authentication token is valid
        try {
            var token = authDAO.checkAuthToken(request.getAuthToken());
        } catch (DataAccessException e) {
            return new LogoutResult(401, "Error: unauthorized");
        } catch (Exception e) {
            return new LogoutResult(500, "Error: " + e.getMessage());
        }

        //Delete auth token
        try {
            authDAO.deleteAuthToken(request.getAuthToken());
        } catch (Exception e) {
            return new LogoutResult(500, "Error: " + e.getMessage());
        }

        return new LogoutResult(200);
    }
}