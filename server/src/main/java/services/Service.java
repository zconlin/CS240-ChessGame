package services;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class Service {

    protected AuthDAO authDAO;
    protected GameDAO gameDAO;
    protected UserDAO userDAO;

    public Service() {
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        userDAO = new UserDAO();
    }

    public Service(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public AuthDAO getAuthDAO() {
        return authDAO;
    }

    public void setAuthDAO(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public GameDAO getGameDAO() {
        return gameDAO;
    }

    public void setGameDAO(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}