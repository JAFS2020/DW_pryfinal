package pe.edu.pizzanajani.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import pe.edu.pizzanajani.config.AppConstants;
import pe.edu.pizzanajani.dao.CategoryDAO;
import pe.edu.pizzanajani.dao.impl.CategoryDAOImpl;
import pe.edu.pizzanajani.model.ProductCategory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "CategoryManagementServlet", urlPatterns = {"/admin/categories"})
public class CategoryManagementServlet extends HttpServlet {

    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        categoryDAO = new CategoryDAOImpl();
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
                List<ProductCategory> categories = categoryDAO.findAll();
                sendJsonResponse(response, true, "Categorías obtenidas", categoriesToJson(categories), 200);

            } else if ("findById".equals(action)) {
                Integer categoryId = Integer.parseInt(request.getParameter("id"));
                ProductCategory category = categoryDAO.findById(categoryId);

                if (category != null) {
                    sendJsonResponse(response, true, "Categoría encontrada", categoryToJson(category), 200);
                } else {
                    sendJsonResponse(response, false, "Categoría no encontrada", null, 404);
                }

            } else if ("listActive".equals(action)) {
                List<ProductCategory> categories = categoryDAO.findActive();
                sendJsonResponse(response, true, "Categorías activas obtenidas", categoriesToJson(categories), 200);

            } else {
                List<ProductCategory> categories = categoryDAO.findAll();
                sendJsonResponse(response, true, "Categorías obtenidas", categoriesToJson(categories), 200);
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
                createCategory(request, response);

            } else if ("update".equals(action)) {
                updateCategory(request, response);

            } else if ("updateStatus".equals(action)) {
                updateCategoryStatus(request, response);

            } else if ("delete".equals(action)) {
                deleteCategory(request, response);

            } else {
                sendJsonResponse(response, false, "Acción no válida", null, 400);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "Error: " + e.getMessage(), null, 500);
        }
    }

    private void createCategory(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");

        if (categoryName == null || categoryName.trim().isEmpty()) {
            sendJsonResponse(response, false, "El nombre de la categoría es requerido", null, 400);
            return;
        }

        ProductCategory existingCategory = categoryDAO.findByName(categoryName.trim());
        if (existingCategory != null) {
            sendJsonResponse(response, false, "Ya existe una categoría con ese nombre", null, 400);
            return;
        }

        ProductCategory category = new ProductCategory();
        category.setCategoryName(categoryName.trim());
        category.setDescription(description != null ? description.trim() : "");
        category.setIsActive(true);

        Integer categoryId = categoryDAO.create(category);

        if (categoryId != null) {
            String json = "{\"categoryId\":" + categoryId + ",\"message\":\"Categoría creada exitosamente\"}";
            sendJsonResponse(response, true, "Categoría creada exitosamente", json, 200);
        } else {
            sendJsonResponse(response, false, "Error al crear la categoría", null, 500);
        }
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");

        ProductCategory category = categoryDAO.findById(categoryId);
        if (category == null) {
            sendJsonResponse(response, false, "Categoría no encontrada", null, 404);
            return;
        }

        if (categoryName == null || categoryName.trim().isEmpty()) {
            sendJsonResponse(response, false, "El nombre de la categoría es requerido", null, 400);
            return;
        }

        category.setCategoryName(categoryName.trim());
        category.setDescription(description != null ? description.trim() : "");

        boolean updated = categoryDAO.update(category);

        if (updated) {
            sendJsonResponse(response, true, "Categoría actualizada exitosamente", null, 200);
        } else {
            sendJsonResponse(response, false, "Error al actualizar la categoría", null, 500);
        }
    }

    private void updateCategoryStatus(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
        Boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

        boolean updated = categoryDAO.updateStatus(categoryId, isActive);

        if (updated) {
            String message = isActive ? "Categoría activada" : "Categoría desactivada";
            sendJsonResponse(response, true, message, null, 200);
        } else {
            sendJsonResponse(response, false, "Error al cambiar el estado", null, 500);
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));

        boolean deleted = categoryDAO.delete(categoryId);

        if (deleted) {
            sendJsonResponse(response, true, "Categoría eliminada exitosamente", null, 200);
        } else {
            sendJsonResponse(response, false, "Error al eliminar la categoría", null, 500);
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

    private String categoriesToJson(List<ProductCategory> categories) {
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < categories.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append(categoryToJson(categories.get(i)));
        }

        json.append("]");
        return json.toString();
    }

    private String categoryToJson(ProductCategory category) {
        StringBuilder json = new StringBuilder("{");

        json.append("\"categoryId\":").append(category.getCategoryId()).append(",");
        json.append("\"categoryName\":\"").append(escapeJson(category.getCategoryName())).append("\",");
        json.append("\"description\":\"").append(escapeJson(category.getDescription() != null ? category.getDescription() : "")).append("\",");
        json.append("\"isActive\":").append(category.getIsActive());

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
