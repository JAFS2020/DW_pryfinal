package pe.edu.pizzanajani.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import pe.edu.pizzanajani.config.AppConstants;
import pe.edu.pizzanajani.dao.UserDAO;
import pe.edu.pizzanajani.dao.RoleDAO;
import pe.edu.pizzanajani.dao.impl.UserDAOImpl;
import pe.edu.pizzanajani.dao.impl.RoleDAOImpl;
import pe.edu.pizzanajani.model.User;
import pe.edu.pizzanajani.model.Role;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "UserManagementServlet", urlPatterns = {"/admin/users"})
public class UserManagementServlet extends HttpServlet {

    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private SimpleDateFormat dateFormat;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        roleDAO = new RoleDAOImpl();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!isAuthenticated(session, AppConstants.ROLE_ADMIN)) {
            sendJsonResponse(response, false, "No autorizado", null, 401);
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("listAll".equals(action)) {
                List<User> users = userDAO.findAll();
                sendJsonResponse(response, true, "Usuarios obtenidos", usersToJson(users), 200);

            } else if ("findById".equals(action)) {
                Integer userId = Integer.parseInt(request.getParameter("id"));
                User user = userDAO.findById(userId);

                if (user != null) {
                    sendJsonResponse(response, true, "Usuario encontrado", userToJson(user), 200);
                } else {
                    sendJsonResponse(response, false, "Usuario no encontrado", null, 404);
                }

            } else if ("findByRole".equals(action)) {
                String roleName = request.getParameter("role");
                List<User> users = userDAO.findByRole(roleName);
                sendJsonResponse(response, true, "Usuarios obtenidos", usersToJson(users), 200);

            } else {
                List<User> users = userDAO.findAll();
                sendJsonResponse(response, true, "Usuarios obtenidos", usersToJson(users), 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "Error: " + e.getMessage(), null, 500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!isAuthenticated(session, AppConstants.ROLE_ADMIN)) {
            sendJsonResponse(response, false, "No autorizado", null, 401);
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                createUser(request, response, session);

            } else if ("update".equals(action)) {
                updateUser(request, response);

            } else if ("updateStatus".equals(action)) {
                updateUserStatus(request, response);

            } else if ("delete".equals(action)) {
                deleteUser(request, response);

            } else {
                sendJsonResponse(response, false, "Acción no válida", null, 400);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "Error: " + e.getMessage(), null, 500);
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws Exception {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String roleIdStr = request.getParameter("roleId");

        if (username == null || username.trim().isEmpty()) {
            sendJsonResponse(response, false, "El nombre de usuario es requerido", null, 400);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            sendJsonResponse(response, false, "El email es requerido", null, 400);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            sendJsonResponse(response, false, "La contraseña es requerida", null, 400);
            return;
        }

        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            sendJsonResponse(response, false, "El rol es requerido", null, 400);
            return;
        }

        Integer roleId = Integer.parseInt(roleIdStr);

        User existingUser = userDAO.findByUsername(username);
        if (existingUser != null) {
            sendJsonResponse(response, false, "El nombre de usuario ya existe", null, 400);
            return;
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        user.setPasswordHash(password);
        user.setFullName(fullName != null ? fullName.trim() : "");
        user.setPhone(phone != null ? phone.trim() : "");
        user.setIsActive(true);

        Integer userId = userDAO.create(user);

        if (userId != null) {
            Integer currentUserId = (Integer) session.getAttribute(AppConstants.SESSION_USER_ID);
            roleDAO.assignRoleToUser(userId, roleId, currentUserId);

            String json = "{\"userId\":" + userId + ",\"message\":\"Usuario creado exitosamente\"}";
            sendJsonResponse(response, true, "Usuario creado exitosamente", json, 200);
        } else {
            sendJsonResponse(response, false, "Error al crear el usuario", null, 500);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer userId = Integer.parseInt(request.getParameter("userId"));
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");

        User user = userDAO.findById(userId);
        if (user == null) {
            sendJsonResponse(response, false, "Usuario no encontrado", null, 404);
            return;
        }

        user.setEmail(email != null ? email.trim() : user.getEmail());
        user.setFullName(fullName != null ? fullName.trim() : user.getFullName());
        user.setPhone(phone != null ? phone.trim() : user.getPhone());

        boolean updated = userDAO.update(user);

        if (updated) {
            sendJsonResponse(response, true, "Usuario actualizado exitosamente", null, 200);
        } else {
            sendJsonResponse(response, false, "Error al actualizar el usuario", null, 500);
        }
    }

    private void updateUserStatus(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer userId = Integer.parseInt(request.getParameter("userId"));
        Boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

        boolean updated = userDAO.updateStatus(userId, isActive);

        if (updated) {
            String message = isActive ? "Usuario activado" : "Usuario desactivado";
            sendJsonResponse(response, true, message, null, 200);
        } else {
            sendJsonResponse(response, false, "Error al cambiar el estado", null, 500);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer userId = Integer.parseInt(request.getParameter("userId"));

        if (userId == 1) {
            sendJsonResponse(response, false, "No se puede eliminar el usuario administrador principal", null, 400);
            return;
        }

        boolean deleted = userDAO.delete(userId);

        if (deleted) {
            sendJsonResponse(response, true, "Usuario eliminado exitosamente", null, 200);
        } else {
            sendJsonResponse(response, false, "Error al eliminar el usuario", null, 500);
        }
    }

    private boolean isAuthenticated(HttpSession session, String requiredRole) {
        if (session == null) {
            return false;
        }

        String userRole = (String) session.getAttribute(AppConstants.SESSION_USER_ROLE);
        return userRole != null && userRole.equals(requiredRole);
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, String data, int statusCode)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\":").append(success).append(",");
        json.append("\"message\":\"").append(escapeJson(message)).append("\"");

        if (data != null && !data.isEmpty()) {
            json.append(",\"data\":").append(data);
        }

        json.append("}");

        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }

    private String usersToJson(List<User> users) {
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < users.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append(userToJson(users.get(i)));
        }

        json.append("]");
        return json.toString();
    }

    private String userToJson(User user) {
        StringBuilder json = new StringBuilder("{");

        json.append("\"userId\":").append(user.getUserId()).append(",");
        json.append("\"username\":\"").append(escapeJson(user.getUsername())).append("\",");
        json.append("\"email\":\"").append(escapeJson(user.getEmail())).append("\",");
        json.append("\"fullName\":\"").append(escapeJson(user.getFullName())).append("\",");
        json.append("\"phone\":\"").append(escapeJson(user.getPhone() != null ? user.getPhone() : "")).append("\",");
        json.append("\"isActive\":").append(user.getIsActive()).append(",");

        json.append("\"roles\":[");
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (int i = 0; i < user.getRoles().size(); i++) {
                if (i > 0) {
                    json.append(",");
                }
                Role role = user.getRoles().get(i);
                json.append("{");
                json.append("\"roleId\":").append(role.getRoleId()).append(",");
                json.append("\"roleName\":\"").append(escapeJson(role.getRoleName())).append("\"");
                json.append("}");
            }
        }
        json.append("],");

        json.append("\"createdAt\":\"").append(user.getCreatedAt() != null ? dateFormat.format(user.getCreatedAt()) : "").append("\",");
        json.append("\"lastLogin\":\"").append(user.getLastLogin() != null ? dateFormat.format(user.getLastLogin()) : "").append("\"");

        json.append("}");
        return json.toString();
    }

    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
