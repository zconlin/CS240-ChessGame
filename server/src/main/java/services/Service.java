package services;

import dataaccess.AuthSQL;
import dataaccess.DataAccessException;
import dataaccess.GameSQL;
import dataaccess.UserSQL;

public class Service {

    protected AuthSQL authSQL;
    protected GameSQL gameSQL;
    protected UserSQL userSQL;

    public Service() throws DataAccessException {
        authSQL = new AuthSQL();
        gameSQL = new GameSQL();
        userSQL = new UserSQL();
    }

    public Service(AuthSQL authSQL, GameSQL gameSQL, UserSQL userSQL) {
        this.authSQL = authSQL;
        this.gameSQL = gameSQL;
        this.userSQL = userSQL;
    }
}