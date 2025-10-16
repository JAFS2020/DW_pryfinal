package pe.edu.pizzanajani.dao;

import pe.edu.pizzanajani.model.Role;
import java.sql.SQLException;
import java.util.List;

public interface RoleDAO {

    Role findById(Integer roleId) throws SQLException;

    Role findByName(String roleName) throws SQLException;

    List<Role> findAll() throws SQLException;

    List<Role> findByUserId(Integer userId) throws SQLException;

    boolean assignRoleToUser(Integer userId, Integer roleId, Integer assignedBy) throws SQLException;

    boolean removeRoleFromUser(Integer userId, Integer roleId) throws SQLException;
}
