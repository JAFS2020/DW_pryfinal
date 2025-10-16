package pe.edu.pizzanajani.dao.impl;

import pe.edu.pizzanajani.config.DatabaseConnection;
import pe.edu.pizzanajani.dao.UserDAO;
import pe.edu.pizzanajani.dao.RoleDAO;
import pe.edu.pizzanajani.model.User;
import pe.edu.pizzanajani.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private final RoleDAO roleDAO;

    public UserDAOImpl() {
        this.roleDAO = new RoleDAOImpl();
    }

    @Override
    public User findById(Integer userId) throws SQLException {
        String sql = "SELECT user_id, username, email, password_hash, full_name, phone, "
                + "is_active, created_at, updated_at, last_login "
                + "FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    user.setRoles(roleDAO.findByUserId(userId));
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT user_id, username, email, password_hash, full_name, phone, "
                + "is_active, created_at, updated_at, last_login "
                + "FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    user.setRoles(roleDAO.findByUserId(user.getUserId()));
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT user_id, username, email, password_hash, full_name, phone, "
                + "is_active, created_at, updated_at, last_login "
                + "FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    user.setRoles(roleDAO.findByUserId(user.getUserId()));
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public User validateCredentials(String username, String password) throws SQLException {
        User user = findByUsername(username);

        if (user != null && user.getIsActive()) {
            if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, password_hash, full_name, phone, "
                + "is_active, created_at, updated_at, last_login "
                + "FROM users ORDER BY full_name";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

            rs.close();

            for (User user : users) {
                user.setRoles(roleDAO.findByUserId(user.getUserId()));
            }

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
            }
        }

        return users;
    }

    @Override
    public List<User> findByRole(String roleName) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT DISTINCT u.user_id, u.username, u.email, u.password_hash, "
                + "u.full_name, u.phone, u.is_active, u.created_at, u.updated_at, u.last_login "
                + "FROM users u "
                + "INNER JOIN user_roles ur ON u.user_id = ur.user_id "
                + "INNER JOIN roles r ON ur.role_id = r.role_id "
                + "WHERE r.role_name = ? AND u.is_active = TRUE "
                + "ORDER BY u.full_name";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, roleName);
            rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

            rs.close();

            for (User user : users) {
                user.setRoles(roleDAO.findByUserId(user.getUserId()));
            }

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
            }
        }

        return users;
    }

    @Override
    public Integer create(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password_hash, full_name, phone, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, PasswordUtil.hashPassword(user.getPasswordHash()));
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getPhone());
            stmt.setBoolean(6, user.getIsActive() != null ? user.getIsActive() : true);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(User user) throws SQLException {
        String sql = "UPDATE users SET email = ?, full_name = ?, phone = ?, "
                + "is_active = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getPhone());
            stmt.setBoolean(4, user.getIsActive());
            stmt.setInt(5, user.getUserId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public void updateLastLogin(Integer userId) throws SQLException {
        String sql = "UPDATE users SET last_login = NOW() WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean updateStatus(Integer userId, boolean isActive) throws SQLException {
        String sql = "UPDATE users SET is_active = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isActive);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setPhone(rs.getString("phone"));
        user.setIsActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        return user;
    }
}
