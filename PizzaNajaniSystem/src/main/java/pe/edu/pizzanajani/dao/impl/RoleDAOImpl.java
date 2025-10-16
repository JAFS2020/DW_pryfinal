package pe.edu.pizzanajani.dao.impl;

import pe.edu.pizzanajani.config.DatabaseConnection;
import pe.edu.pizzanajani.dao.RoleDAO;
import pe.edu.pizzanajani.model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO {

    @Override
    public Role findById(Integer roleId) throws SQLException {
        String sql = "SELECT role_id, role_name, description, created_at, updated_at "
                + "FROM roles WHERE role_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRole(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Role findByName(String roleName) throws SQLException {
        String sql = "SELECT role_id, role_name, description, created_at, updated_at "
                + "FROM roles WHERE role_name = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roleName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRole(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Role> findAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT role_id, role_name, description, created_at, updated_at "
                + "FROM roles ORDER BY role_name";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }
        }
        return roles;
    }

    @Override
    public List<Role> findByUserId(Integer userId) throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT r.role_id, r.role_name, r.description, r.created_at, r.updated_at "
                + "FROM roles r "
                + "INNER JOIN user_roles ur ON r.role_id = ur.role_id "
                + "WHERE ur.user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roles.add(mapResultSetToRole(rs));
                }
            }
        }
        return roles;
    }

    @Override
    public boolean assignRoleToUser(Integer userId, Integer roleId, Integer assignedBy) throws SQLException {
        String sql = "INSERT INTO user_roles (user_id, role_id, assigned_by) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE assigned_by = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            stmt.setInt(3, assignedBy);
            stmt.setInt(4, assignedBy);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean removeRoleFromUser(Integer userId, Integer roleId) throws SQLException {
        String sql = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);

            return stmt.executeUpdate() > 0;
        }
    }

    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setRoleName(rs.getString("role_name"));
        role.setDescription(rs.getString("description"));
        role.setCreatedAt(rs.getTimestamp("created_at"));
        role.setUpdatedAt(rs.getTimestamp("updated_at"));
        return role;
    }
}
