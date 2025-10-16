package pe.edu.pizzanajani.dao.impl;

import pe.edu.pizzanajani.config.DatabaseConnection;
import pe.edu.pizzanajani.dao.ProductCategoryDAO;
import pe.edu.pizzanajani.model.ProductCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDAOImpl implements ProductCategoryDAO {

    @Override
    public ProductCategory findById(Integer categoryId) throws SQLException {
        String sql = "SELECT category_id, category_name, description, display_order, "
                + "is_active, created_at, updated_at "
                + "FROM product_categories WHERE category_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
        }
        return null;
    }

    @Override
    public ProductCategory findByName(String categoryName) throws SQLException {
        String sql = "SELECT category_id, category_name, description, display_order, "
                + "is_active, created_at, updated_at "
                + "FROM product_categories WHERE category_name = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoryName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<ProductCategory> findAll() throws SQLException {
        List<ProductCategory> categories = new ArrayList<>();
        String sql = "SELECT category_id, category_name, description, display_order, "
                + "is_active, created_at, updated_at "
                + "FROM product_categories ORDER BY display_order, category_name";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        }
        return categories;
    }

    @Override
    public List<ProductCategory> findAllActive() throws SQLException {
        List<ProductCategory> categories = new ArrayList<>();
        String sql = "SELECT category_id, category_name, description, display_order, "
                + "is_active, created_at, updated_at "
                + "FROM product_categories WHERE is_active = TRUE "
                + "ORDER BY display_order, category_name";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        }
        return categories;
    }

    @Override
    public Integer create(ProductCategory category) throws SQLException {
        String sql = "INSERT INTO product_categories (category_name, description, display_order, is_active) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getDisplayOrder() != null ? category.getDisplayOrder() : 0);
            stmt.setBoolean(4, category.getIsActive() != null ? category.getIsActive() : true);

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
    public boolean update(ProductCategory category) throws SQLException {
        String sql = "UPDATE product_categories SET category_name = ?, description = ?, "
                + "display_order = ?, is_active = ? WHERE category_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getDisplayOrder());
            stmt.setBoolean(4, category.getIsActive());
            stmt.setInt(5, category.getCategoryId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer categoryId) throws SQLException {
        String sql = "DELETE FROM product_categories WHERE category_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(Integer categoryId, boolean isActive) throws SQLException {
        String sql = "UPDATE product_categories SET is_active = ? WHERE category_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isActive);
            stmt.setInt(2, categoryId);

            return stmt.executeUpdate() > 0;
        }
    }

    private ProductCategory mapResultSetToCategory(ResultSet rs) throws SQLException {
        ProductCategory category = new ProductCategory();
        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        category.setDisplayOrder(rs.getInt("display_order"));
        category.setIsActive(rs.getBoolean("is_active"));
        category.setCreatedAt(rs.getTimestamp("created_at"));
        category.setUpdatedAt(rs.getTimestamp("updated_at"));
        return category;
    }
}
