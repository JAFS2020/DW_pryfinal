package pe.edu.pizzanajani.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import pe.edu.pizzanajani.config.AppConstants;
import pe.edu.pizzanajani.dao.InventoryDAO;
import pe.edu.pizzanajani.dao.impl.InventoryDAOImpl;
import pe.edu.pizzanajani.model.Inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "InventoryManagementServlet", urlPatterns = {"/admin/inventory"})
public class InventoryManagementServlet extends HttpServlet {

    private InventoryDAO inventoryDAO;

    @Override
    public void init() throws ServletException {
        inventoryDAO = new InventoryDAOImpl();
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
                List<Inventory> inventories = inventoryDAO.findAll();
                sendJsonResponse(response, true, "Inventario obtenido", inventoriesToJson(inventories), 200);

            } else if ("findById".equals(action)) {
                Integer inventoryId = Integer.parseInt(request.getParameter("id"));
                Inventory inventory = inventoryDAO.findById(inventoryId);

                if (inventory != null) {
                    sendJsonResponse(response, true, "Inventario encontrado", inventoryToJson(inventory), 200);
                } else {
                    sendJsonResponse(response, false, "Inventario no encontrado", null, 404);
                }

            } else if ("findByProduct".equals(action)) {
                Integer productId = Integer.parseInt(request.getParameter("productId"));
                Inventory inventory = inventoryDAO.findByProductId(productId);

                if (inventory != null) {
                    sendJsonResponse(response, true, "Inventario encontrado", inventoryToJson(inventory), 200);
                } else {
                    sendJsonResponse(response, false, "Inventario no encontrado", null, 404);
                }

            } else if ("listLowStock".equals(action)) {
                List<Inventory> inventories = inventoryDAO.findLowStock();
                sendJsonResponse(response, true, "Productos con stock bajo obtenidos", inventoriesToJson(inventories), 200);

            } else {
                List<Inventory> inventories = inventoryDAO.findAll();
                sendJsonResponse(response, true, "Inventario obtenido", inventoriesToJson(inventories), 200);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "Error de base de datos: " + e.getMessage(), null, 500);
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
                createInventory(request, response);

            } else if ("update".equals(action)) {
                updateInventory(request, response);

            } else if ("restock".equals(action)) {
                restockInventory(request, response);

            } else if ("updateStock".equals(action)) {
                updateStock(request, response);

            } else if ("delete".equals(action)) {
                deleteInventory(request, response);

            } else {
                sendJsonResponse(response, false, "Acción no válida", null, 400);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "Error de base de datos: " + e.getMessage(), null, 500);
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(response, false, "Error: " + e.getMessage(), null, 500);
        }
    }

    private void createInventory(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String productIdStr = request.getParameter("productId");
        String currentStockStr = request.getParameter("currentStock");
        String minStockStr = request.getParameter("minStock");
        String maxStockStr = request.getParameter("maxStock");

        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            sendJsonResponse(response, false, "El ID del producto es requerido", null, 400);
            return;
        }

        Integer productId = Integer.parseInt(productIdStr);
        Integer currentStock = (currentStockStr != null && !currentStockStr.trim().isEmpty())
                ? Integer.parseInt(currentStockStr) : 0;
        Integer minStock = (minStockStr != null && !minStockStr.trim().isEmpty())
                ? Integer.parseInt(minStockStr) : 5;
        Integer maxStock = (maxStockStr != null && !maxStockStr.trim().isEmpty())
                ? Integer.parseInt(maxStockStr) : 100;

        Inventory existingInventory = inventoryDAO.findByProductId(productId);
        if (existingInventory != null) {
            sendJsonResponse(response, false, "Ya existe inventario para este producto", null, 400);
            return;
        }

        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setCurrentStock(currentStock);
        inventory.setMinStock(minStock);
        inventory.setMaxStock(maxStock);

        Integer inventoryId = inventoryDAO.create(inventory);

        if (inventoryId != null) {
            String json = "{\"inventoryId\":" + inventoryId + ",\"message\":\"Inventario creado exitosamente\"}";
            sendJsonResponse(response, true, "Inventario creado exitosamente", json, 200);
        } else {
            sendJsonResponse(response, false, "Error al crear el inventario", null, 500);
        }
    }

    private void updateInventory(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer inventoryId = Integer.parseInt(request.getParameter("inventoryId"));
        String currentStockStr = request.getParameter("currentStock");
        String minStockStr = request.getParameter("minStock");
        String maxStockStr = request.getParameter("maxStock");

        Inventory inventory = inventoryDAO.findById(inventoryId);
        if (inventory == null) {
            sendJsonResponse(response, false, "Inventario no encontrado", null, 404);
            return;
        }

        Integer currentStock = (currentStockStr != null && !currentStockStr.trim().isEmpty())
                ? Integer.parseInt(currentStockStr) : inventory.getCurrentStock();
        Integer minStock = (minStockStr != null && !minStockStr.trim().isEmpty())
                ? Integer.parseInt(minStockStr) : inventory.getMinStock();
        Integer maxStock = (maxStockStr != null && !maxStockStr.trim().isEmpty())
                ? Integer.parseInt(maxStockStr) : inventory.getMaxStock();

        if (currentStock < 0) {
            sendJsonResponse(response, false, "El stock no puede ser negativo", null, 400);
            return;
        }

        if (minStock > maxStock) {
            sendJsonResponse(response, false, "El stock mínimo no puede ser mayor al máximo", null, 400);
            return;
        }

        inventory.setCurrentStock(currentStock);
        inventory.setMinStock(minStock);
        inventory.setMaxStock(maxStock);

        boolean updated = inventoryDAO.update(inventory);

        if (updated) {
            sendJsonResponse(response, true, "Inventario actualizado exitosamente", null, 200);
        } else {
            sendJsonResponse(response, false, "Error al actualizar el inventario", null, 500);
        }
    }

    private void restockInventory(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer productId = Integer.parseInt(request.getParameter("productId"));
        Integer quantity = Integer.parseInt(request.getParameter("quantity"));

        if (quantity <= 0) {
            sendJsonResponse(response, false, "La cantidad debe ser mayor a 0", null, 400);
            return;
        }

        Inventory inventory = inventoryDAO.findByProductId(productId);
        if (inventory == null) {
            sendJsonResponse(response, false, "Inventario no encontrado para este producto", null, 404);
            return;
        }

        boolean updated = inventoryDAO.incrementStock(productId, quantity);

        if (updated) {
            sendJsonResponse(response, true, "Stock reabastecido exitosamente. Cantidad agregada: " + quantity, null, 200);
        } else {
            sendJsonResponse(response, false, "Error al reabastecer el stock", null, 500);
        }
    }

    private void updateStock(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer productId = Integer.parseInt(request.getParameter("productId"));
        Integer newStock = Integer.parseInt(request.getParameter("newStock"));

        if (newStock < 0) {
            sendJsonResponse(response, false, "El stock no puede ser negativo", null, 400);
            return;
        }

        boolean updated = inventoryDAO.updateStock(productId, newStock);

        if (updated) {
            sendJsonResponse(response, true, "Stock actualizado exitosamente", null, 200);
        } else {
            sendJsonResponse(response, false, "Error al actualizar el stock", null, 500);
        }
    }

    private void deleteInventory(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer inventoryId = Integer.parseInt(request.getParameter("inventoryId"));

        boolean deleted = inventoryDAO.delete(inventoryId);

        if (deleted) {
            sendJsonResponse(response, true, "Inventario eliminado exitosamente", null, 200);
        } else {
            sendJsonResponse(response, false, "Error al eliminar el inventario", null, 500);
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

    private String inventoriesToJson(List<Inventory> inventories) {
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < inventories.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append(inventoryToJson(inventories.get(i)));
        }

        json.append("]");
        return json.toString();
    }

    private String inventoryToJson(Inventory inventory) {
        StringBuilder json = new StringBuilder("{");

        json.append("\"inventoryId\":").append(inventory.getInventoryId()).append(",");
        json.append("\"productId\":").append(inventory.getProductId()).append(",");
        json.append("\"productName\":\"").append(escapeJson(inventory.getProductName() != null ? inventory.getProductName() : "")).append("\",");
        json.append("\"productCode\":\"").append(escapeJson(inventory.getProductCode() != null ? inventory.getProductCode() : "")).append("\",");
        json.append("\"currentStock\":").append(inventory.getCurrentStock()).append(",");
        json.append("\"minStock\":").append(inventory.getMinStock()).append(",");
        json.append("\"maxStock\":").append(inventory.getMaxStock()).append(",");
        json.append("\"isLowStock\":").append(inventory.isLowStock()).append(",");
        json.append("\"stockNeeded\":").append(inventory.getStockNeeded());

        if (inventory.getLastRestockDate() != null) {
            json.append(",\"lastRestockDate\":\"").append(inventory.getLastRestockDate().toString()).append("\"");
        }
        if (inventory.getLastRestockQuantity() != null) {
            json.append(",\"lastRestockQuantity\":").append(inventory.getLastRestockQuantity());
        }

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
