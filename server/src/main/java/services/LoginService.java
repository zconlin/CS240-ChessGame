package services;

import dataaccess.*;
import requestclasses.LoginRequest;
import resultclasses.LoginResult;
import model.AuthToken;

public class LoginService extends Service {

    public LoginService() {
        super();
    }

    public LoginService(AuthDAO authDAO, UserDAO userDAO) {
        super(authDAO, null, userDAO);
    }

    public LoginResult login(LoginRequest request) {
        //Check if username or password are null
        if (request.getUsername() == null || request.getPassword() == null) {
            return new LoginResult(400, "Error: bad request");
        }

        //Check if username and password match
        try {
            var user = userDAO.getUser(request.getUsername());
            if (user == null || !user.getPassword().equals(request.getPassword())) {
                return new LoginResult(401, "Error: unauthorized");
            }
        } catch (DataAccessException e) {
            return new LoginResult(401, "Error: unauthorized");
        } catch (Exception e) {
            return new LoginResult(500, "Error: " + e.getMessage());
        }
        //Add auth token
        var token = new AuthToken(request.getUsername());
        try {
            authDAO.addAuthToken(token);
        } catch (Exception e) {
            return new LoginResult(500, "Error: " + e.getMessage());
        }
        return new LoginResult(token);
    }
}