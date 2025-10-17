package pe.edu.pizzanajani.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import pe.edu.pizzanajani.config.AppConstants;
import pe.edu.pizzanajani.dao.ProductDAO;
import pe.edu.pizzanajani.dao.impl.ProductDAOImpl;
import pe.edu.pizzanajani.model.Product;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ProductManagementServlet", urlPatterns = {"/admin/products"})
public class ProductManagementServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
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
                List<Product> products = productDAO.findAll();
                sendJsonResponse(response, true, "Productos obtenidos", productsToJson(products), 200);

            } else if ("findById".equals(action)) {
                Integer productId = Integer.parseInt(request.getParameter("id"));
                Product product = productDAO.findById(productId);

                if (product != null) {
                    sendJsonResponse(response, true, "Producto encontrado", productToJson(product), 200);
                } else {
                    sendJsonResponse(response, false, "Producto no encontrado", null, 404);
                }

            } else if ("listByCategory".equals(action)) {
                Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
                List<Product> products = productDAO.findByCategory(categoryId);
                sendJsonResponse(response, true, "Productos obtenidos", productsToJson(products), 200);

            } else if ("listAvailable".equals(action)) {
                List<Product> products = productDAO.findAllAvailable();
                sendJsonResponse(response, true, "Productos disponibles obtenidos", productsToJson(products), 200);

            } else {
                List<Product> products = productDAO.findAll();
                sendJsonResponse(response, true, "Productos obtenidos", productsToJson(products), 200);
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
                createProduct(request, response);

            } else if ("update".equals(action)) {
                updateProduct(request, response);

            } else if ("updateAvailability".equals(action)) {
                updateProductAvailability(request, response);

            } else if ("delete".equals(action)) {
                deleteProduct(request, response);

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

    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String productCode = request.getParameter("productCode");
        String productName = request.getParameter("productName");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String costStr = request.getParameter("cost");
        String categoryIdStr = request.getParameter("categoryId");
        String imageUrl = request.getParameter("imageUrl");
        String prepTimeStr = request.getParameter("preparationTime");

        if (productName == null || productName.trim().isEmpty()) {
            sendJsonResponse(response, false, "El nombre del producto es requerido", null, 400);
            return;
        }

        if (priceStr == null || priceStr.trim().isEmpty()) {
            sendJsonResponse(response, false, "El precio es requerido", null, 400);
            return;
        }

        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            sendJsonResponse(response, false, "La categoría es requerida", null, 400);
            return;
        }

        BigDecimal price = new BigDecimal(priceStr);
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            sendJsonResponse(response, false, "El precio debe ser mayor a 0", null, 400);
            return;
        }

        Integer categoryId = Integer.parseInt(categoryIdStr);
        BigDecimal cost = (costStr != null && !costStr.trim().isEmpty()) ? new BigDecimal(costStr) : BigDecimal.ZERO;
        Integer prepTime = (prepTimeStr != null && !prepTimeStr.trim().isEmpty()) ? Integer.parseInt(prepTimeStr) : 15;

        Product product = new Product();
        product.setProductCode(productCode != null ? productCode.trim() : null);
        product.setProductName(productName.trim());
        product.setDescription(description != null ? description.trim() : "");
        product.setPrice(price);
        product.setCost(cost);
        product.setCategoryId(categoryId);
        product.setImageUrl(imageUrl != null ? imageUrl.trim() : "");
        product.setIsAvailable(true);
        product.setPreparationTime(prepTime);

        Integer productId = productDAO.create(product);

        if (productId != null) {
            String json = "{\"productId\":" + productId + ",\"message\":\"Producto creado exitosamente\"}";
            sendJsonResponse(response, true, "Producto creado exitosamente", json, 200);
        } else {
            sendJsonResponse(response, false, "Error al crear el producto", null, 500);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer productId = Integer.parseInt(request.getParameter("productId"));
        String productCode = request.getParameter("productCode");
        String productName = request.getParameter("productName");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String costStr = request.getParameter("cost");
        String categoryIdStr = request.getParameter("categoryId");
        String imageUrl = request.getParameter("imageUrl");
        String prepTimeStr = request.getParameter("preparationTime");

        Product product = productDAO.findById(productId);
        if (product == null) {
            sendJsonResponse(response, false, "Producto no encontrado", null, 404);
            return;
        }

        if (productName == null || productName.trim().isEmpty()) {
            sendJsonResponse(response, false, "El nombre del producto es requerido", null, 400);
            return;
        }

        BigDecimal price = new BigDecimal(priceStr);
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            sendJsonResponse(response, false, "El precio debe ser mayor a 0", null, 400);
            return;
        }

        Integer categoryId = Integer.parseInt(categoryIdStr);
        BigDecimal cost = (costStr != null && !costStr.trim().isEmpty()) ? new BigDecimal(costStr) : BigDecimal.ZERO;
        Integer prepTime = (prepTimeStr != null && !prepTimeStr.trim().isEmpty()) ? Integer.parseInt(prepTimeStr) : 15;

        product.setProductCode(productCode != null ? productCode.trim() : product.getProductCode());
        product.setProductName(productName.trim());
        product.setDescription(description != null ? description.trim() : "");
        product.setPrice(price);
        product.setCost(cost);
        product.setCategoryId(categoryId);
        product.setImageUrl(imageUrl != null ? imageUrl.trim() : "");
        product.setPreparationTime(prepTime);

        boolean updated = productDAO.update(product);

        if (updated) {
            sendJsonResponse(response, true, "Producto actualizado exitosamente", null, 200);
        } else {
            sendJsonResponse(response, false, "Error al actualizar el producto", null, 500);
        }
    }

    private void updateProductAvailability(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer productId = Integer.parseInt(request.getParameter("productId"));
        Boolean isAvailable = Boolean.parseBoolean(request.getParameter("isAvailable"));

        boolean updated = productDAO.updateAvailability(productId, isAvailable);

        if (updated) {
            String message = isAvailable ? "Producto disponible" : "Producto no disponible";
            sendJsonResponse(response, true, message, null, 200);
        } else {
            sendJsonResponse(response, false, "Error al cambiar disponibilidad", null, 500);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Integer productId = Integer.parseInt(request.getParameter("productId"));

        boolean deleted = productDAO.delete(productId);

        if (deleted) {
            sendJsonResponse(response, true, "Producto eliminado exitosamente", null, 200);
        } else {
            sendJsonResponse(response, false, "Error al eliminar el producto", null, 500);
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

    private String productsToJson(List<Product> products) {
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < products.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append(productToJson(products.get(i)));
        }

        json.append("]");
        return json.toString();
    }

    private String productToJson(Product product) {
        StringBuilder json = new StringBuilder("{");

        json.append("\"productId\":").append(product.getProductId()).append(",");
        json.append("\"productCode\":\"").append(escapeJson(product.getProductCode() != null ? product.getProductCode() : "")).append("\",");
        json.append("\"productName\":\"").append(escapeJson(product.getProductName())).append("\",");
        json.append("\"description\":\"").append(escapeJson(product.getDescription() != null ? product.getDescription() : "")).append("\",");
        json.append("\"price\":").append(product.getPrice()).append(",");
        json.append("\"cost\":").append(product.getCost() != null ? product.getCost() : 0).append(",");
        json.append("\"categoryId\":").append(product.getCategoryId()).append(",");
        json.append("\"categoryName\":\"").append(escapeJson(product.getCategoryName() != null ? product.getCategoryName() : "Sin categoría")).append("\",");
        json.append("\"imageUrl\":\"").append(escapeJson(product.getImageUrl() != null ? product.getImageUrl() : "")).append("\",");
        json.append("\"isAvailable\":").append(product.getIsAvailable()).append(",");
        json.append("\"preparationTime\":").append(product.getPreparationTime() != null ? product.getPreparationTime() : 15);

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
