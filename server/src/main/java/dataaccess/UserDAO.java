package dataaccess;

import model.User;
import dataaccess.DataAccessException;

import java.util.HashMap;

public class UserDAO extends DataAccess {

    private HashMap<String, User> users = new HashMap<>();

    public UserDAO() {
        super();
    }

    public void addUser(User user) throws DataAccessException {
    }

    public User getUser(String username) throws DataAccessException {
        return null;
    }

    public User updateUser(User user) throws DataAccessException {
        return null;
    }

    public void clear(){
    }

    public void deleteUser(User user) throws DataAccessException {
    }
}
