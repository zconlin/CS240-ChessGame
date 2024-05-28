package services;

import dataaccess.*;
import model.User;
import requestclasses.LoginRequest;
import resultclasses.LoginResult;
import model.AuthToken;
import server.ServerException;

public class LoginService extends Service {

    public LoginService() {
        super();
    }

    public LoginService(AuthDAO authDAO, UserDAO userDAO) {
        super(authDAO, null, userDAO);
    }

    public LoginResult login(LoginRequest request) throws ServerException {
        //Check if username or password are null
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new ServerException("Error: bad request", 400);
        }

        //Check if username and password match
        User user = null;
        try {
            user = userDAO.getUser(request.getUsername());
        } catch (DataAccessException e) {
            throw new ServerException("Error: unauthorized", 401);
        } catch (Exception e) {
            throw new ServerException("Error: " + e.getMessage(), 500);
        }
        if (user == null || !user.password().equals(request.getPassword())) {
            throw new ServerException("Error: unauthorized", 401);
        }
        //Add auth token
        var token = new AuthToken(request.getUsername());
        try {
            authDAO.addAuthToken(token);
        } catch (Exception e) {
            throw new ServerException("Error: " + e.getMessage(), 500);
        }
        return new LoginResult(token);
    }
}