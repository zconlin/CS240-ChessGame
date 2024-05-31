package dataaccess;

import model.User;
import dataaccess.DataAccessException;
import dataaccess.DataAccess;
import org.mindrot.jbcrypt.BCrypt;
import server.ServerException;


import java.sql.SQLException;

public class UserSQL extends DataAccess {

    public UserSQL() {
        super();
    }

    public void addUser(User user) throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

            if (userExists(user)) {
            throw new DataAccessException("Error: Username already exists");
        }

        var sql = "INSERT INTO chess.user (username, password, email) VALUES (?, ?, ?);";

        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    } catch (Exception e) {
            throw new ServerException("Unable to read data", 500);
        }
    }

    public User getUser(String username) throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

            if (!userExists(new User(username, null, null))) {
            throw new DataAccessException("Error: Username does not exist");
        }

        var sql = "SELECT * FROM chess.user WHERE username = ?;";
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    } catch (Exception e) {
        throw new ServerException("Unable to read data", 500);
    }
}

    public boolean userExists(User user) throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

        var sql = "SELECT * FROM chess.user WHERE username = ?;";
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    } catch (Exception e) {
            throw new ServerException("Unable to read data", 500);
        }
    }

    public void clear() throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

        var sql = "DELETE FROM chess.user;";
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    } catch (Exception e) {
        throw new ServerException("Unable to read data", 500);
    }
}

    public void deleteUser(User user) throws DataAccessException, ServerException {
        try (var conn = DatabaseManager.getConnection()) {

            if (!userExists(user)) {
                throw new DataAccessException("Error: Username does not exist");
            }

            var sql = "DELETE FROM chess.user WHERE username = ?;";
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getUsername());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new ServerException("Unable to read data", 500);
        }
    }
}
