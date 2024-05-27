package dataaccess;

import model.AuthToken;
import dataaccess.DataAccessException;

import java.util.HashMap;

public class AuthDAO extends DataAccess {

    private HashMap<String, AuthToken> authTokens = new HashMap<>();

    public AuthDAO() {
        super();
    }

    public void addAuthToken(AuthToken authToken) throws DataAccessException {
        authTokens.put(authToken.getAuthToken(), authToken);
    }

    public boolean checkAuthToken(String authToken) throws DataAccessException {
        if (!authTokenExists(authToken)) {
            throw new DataAccessException("Error: Authentication token does not exist");
        }
        return true;
    }

    public void clear(){
        authTokens.clear();
    }

    public void deleteAuthToken(String authToken) throws DataAccessException {
        if (!authTokenExists(authToken)) {
            throw new DataAccessException("Error: Authentication token does not exist");
        }
        authTokens.remove(authToken);
    }

    public AuthToken getAuthToken(String authToken) throws DataAccessException {
        if (!authTokenExists(authToken)) {
            throw new DataAccessException("Error: Authentication token does not exist");
        }
        return authTokens.get(authToken);
    }

    public String getUsername(String authToken) {
        return authTokens.get(authToken).getUsername();
    }

    public boolean authTokenExists(String authToken) throws DataAccessException {
        return authTokens.containsKey(authToken);
    }
}
