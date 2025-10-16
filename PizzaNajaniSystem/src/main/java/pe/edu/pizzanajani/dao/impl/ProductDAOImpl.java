package pe.edu.pizzanajani.dao.impl;

import pe.edu.pizzanajani.config.DatabaseConnection;
import pe.edu.pizzanajani.dao.ProductDAO;
import pe.edu.pizzanajani.model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product findById(Integer productId) throws SQLException {
        String sql = "SELECT p.product_id, p.category_id, p.product_code, p.product_name, "
                + "p.description, p.price, p.cost, p.image_url, p.is_available, "
                + "p.preparation_time, p.created_at, p.updated_at, pc.category_name "
                + "FROM products p "
                + "INNER JOIN product_categories pc ON p.category_id = pc.category_id "
                + "WHERE p.product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Product findByCode(String productCode) throws SQLException {
        String sql = "SELECT p.product_id, p.category_id, p.product_code, p.product_name, "
                + "p.description, p.price, p.cost, p.image_url, p.is_available, "
                + "p.preparation_time, p.created_at, p.updated_at, pc.category_name "
                + "FROM products p "
                + "INNER JOIN product_categories pc ON p.category_id = pc.category_id "
                + "WHERE p.product_code = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.category_id, p.product_code, p.product_name, "
                + "p.description, p.price, p.cost, p.image_url, p.is_available, "
                + "p.preparation_time, p.created_at, p.updated_at, pc.category_name "
                + "FROM products p "
                + "INNER JOIN product_categories pc ON p.category_id = pc.category_id "
                + "ORDER BY pc.display_order, p.product_name";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    @Override
    public List<Product> findByCategory(Integer categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.category_id, p.product_code, p.product_name, "
                + "p.description, p.price, p.cost, p.image_url, p.is_available, "
                + "p.preparation_time, p.created_at, p.updated_at, pc.category_name "
                + "FROM products p "
                + "INNER JOIN product_categories pc ON p.category_id = pc.category_id "
                + "WHERE p.category_id = ? "
                + "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        }
        return products;
    }

    @Override
    public List<Product> findAllAvailable() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.category_id, p.product_code, p.product_name, "
                + "p.description, p.price, p.cost, p.image_url, p.is_available, "
                + "p.preparation_time, p.created_at, p.updated_at, pc.category_name "
                + "FROM products p "
                + "INNER JOIN product_categories pc ON p.category_id = pc.category_id "
                + "WHERE p.is_available = TRUE AND pc.is_active = TRUE "
                + "ORDER BY pc.display_order, p.product_name";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    @Override
    public List<Product> findAvailableByCategory(Integer categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.category_id, p.product_code, p.product_name, "
                + "p.description, p.price, p.cost, p.image_url, p.is_available, "
                + "p.preparation_time, p.created_at, p.updated_at, pc.category_name "
                + "FROM products p "
                + "INNER JOIN product_categories pc ON p.category_id = pc.category_id "
                + "WHERE p.category_id = ? AND p.is_available = TRUE "
                + "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        }
        return products;
    }

    @Override
    public List<Product> searchByName(String searchTerm) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.category_id, p.product_code, p.product_name, "
                + "p.description, p.price, p.cost, p.image_url, p.is_available, "
                + "p.preparation_time, p.created_at, p.updated_at, pc.category_name "
                + "FROM products p "
                + "INNER JOIN product_categories pc ON p.category_id = pc.category_id "
                + "WHERE p.product_name LIKE ? OR p.description LIKE ? "
                + "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String search = "%" + searchTerm + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        }
        return products;
    }

    @Override
    public Integer create(Product product) throws SQLException {
        String sql = "INSERT INTO products (category_id, product_code, product_name, description, "
                + "price, cost, image_url, is_available, preparation_time) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, product.getCategoryId());
            stmt.setString(2, product.getProductCode());
            stmt.setString(3, product.getProductName());
            stmt.setString(4, product.getDescription());
            stmt.setBigDecimal(5, product.getPrice());
            stmt.setBigDecimal(6, product.getCost());
            stmt.setString(7, product.getImageUrl());
            stmt.setBoolean(8, product.getIsAvailable() != null ? product.getIsAvailable() : true);
            stmt.setInt(9, product.getPreparationTime() != null ? product.getPreparationTime() : 15);

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
    public boolean update(Product product) throws SQLException {
        String sql = "UPDATE products SET category_id = ?, product_code = ?, product_name = ?, "
                + "description = ?, price = ?, cost = ?, image_url = ?, is_available = ?, "
                + "preparation_time = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getCategoryId());
            stmt.setString(2, product.getProductCode());
            stmt.setString(3, product.getProductName());
            stmt.setString(4, product.getDescription());
            stmt.setBigDecimal(5, product.getPrice());
            stmt.setBigDecimal(6, product.getCost());
            stmt.setString(7, product.getImageUrl());
            stmt.setBoolean(8, product.getIsAvailable());
            stmt.setInt(9, product.getPreparationTime());
            stmt.setInt(10, product.getProductId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateAvailability(Integer productId, boolean isAvailable) throws SQLException {
        String sql = "UPDATE products SET is_available = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, productId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePrice(Integer productId, BigDecimal newPrice) throws SQLException {
        String sql = "UPDATE products SET price = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, newPrice);
            stmt.setInt(2, productId);

            return stmt.executeUpdate() > 0;
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setProductCode(rs.getString("product_code"));
        product.setProductName(rs.getString("product_name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setCost(rs.getBigDecimal("cost"));
        product.setImageUrl(rs.getString("image_url"));
        product.setIsAvailable(rs.getBoolean("is_available"));
        product.setPreparationTime(rs.getInt("preparation_time"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        product.setCategoryName(rs.getString("category_name"));
        return product;
    }
}
