package pe.edu.pizzanajani.dao;

import pe.edu.pizzanajani.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    User findById(Integer userId) throws SQLException;

    User findByUsername(String username) throws SQLException;

    User findByEmail(String email) throws SQLException;

    User validateCredentials(String username, String password) throws SQLException;

    List<User> findAll() throws SQLException;

    List<User> findByRole(String roleName) throws SQLException;

    Integer create(User user) throws SQLException;

    boolean update(User user) throws SQLException;

    void updateLastLogin(Integer userId) throws SQLException;

    boolean updateStatus(Integer userId, boolean isActive) throws SQLException;

    boolean delete(Integer userId) throws SQLException;
}
