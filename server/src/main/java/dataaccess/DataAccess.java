package dataaccess;

public class DataAccess {

    public DataAccess() {

    }

    static {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
