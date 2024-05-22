package services;

import dataaccess.*;
import requestclasses.ClearDBRequest;
import resultclasses.ClearDBResult;

public class ClearDBService extends Service {
    public ClearDBService() {
    }

    public ClearDBService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public ClearDBResult clearDB(ClearDBRequest request) {
        try {
            this.authDAO.clear();
            this.gameDAO.clear();
            this.userDAO.clear();
        } catch (Exception e) {
            return new ClearDBResult(500, "Error: " + e.getMessage());
        }

        return new ClearDBResult(200);
    }
}
