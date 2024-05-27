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
        if (userExists(user)) {
            throw new DataAccessException("Error: Username already exists");
        }

        users.put(user.getUsername(), user);
    }

    public User getUser(String username) throws DataAccessException {
        if (!userExists(new User(username, null, null))) {
            throw new DataAccessException("Error: Username does not exist");
        }

        return users.get(username);
    }

    public User updateUser(User user) throws DataAccessException {
        if (!userExists(user)) {
            throw new DataAccessException("Error: Username does not exist");
        }

        return users.put(user.getUsername(), user);
    }

    public boolean userExists(User user) {
        return users.containsKey(user.getUsername());
    }

    public void clear() throws DataAccessException {
        users.clear();
    }

    public void deleteUser(User user) throws DataAccessException {
        if (!userExists(user)) {
            throw new DataAccessException("Error: Username does not exist");
        }

        users.remove(user.getUsername());
    }

}
