package dataaccess;

import model.AuthToken;
import dataaccess.DataAccessException;

import java.util.HashSet;

public class AuthDAO extends DataAccess {

    private HashSet<AuthToken> authTokens = new HashSet<>();

    public AuthDAO() {
        super();
    }

    public void addAuthToken(AuthToken authToken) throws DataAccessException {
    }

    public boolean checkAuthToken(AuthToken authToken) throws DataAccessException {
        return false;
    }

    public void clear(){
    }

    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {
    }

    public String getUsername(AuthToken authToken) {
        return "";
    }
}
