package pe.edu.pizzanajani.dao.impl;

import pe.edu.pizzanajani.config.DatabaseConnection;
import pe.edu.pizzanajani.dao.InventoryDAO;
import pe.edu.pizzanajani.model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAOImpl implements InventoryDAO {

    @Override
    public Inventory findById(Integer inventoryId) throws SQLException {
        String sql = "SELECT i.inventory_id, i.product_id, i.current_stock, i.min_stock, "
                + "i.max_stock, i.last_restock_date, i.last_restock_quantity, i.updated_at, "
                + "p.product_name, p.product_code "
                + "FROM inventory i "
                + "INNER JOIN products p ON i.product_id = p.product_id "
                + "WHERE i.inventory_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inventoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventory(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Inventory findByProductId(Integer productId) throws SQLException {
        String sql = "SELECT i.inventory_id, i.product_id, i.current_stock, i.min_stock, "
                + "i.max_stock, i.last_restock_date, i.last_restock_quantity, i.updated_at, "
                + "p.product_name, p.product_code "
                + "FROM inventory i "
                + "INNER JOIN products p ON i.product_id = p.product_id "
                + "WHERE i.product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventory(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Inventory> findAll() throws SQLException {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "SELECT i.inventory_id, i.product_id, i.current_stock, i.min_stock, "
                + "i.max_stock, i.last_restock_date, i.last_restock_quantity, i.updated_at, "
                + "p.product_name, p.product_code "
                + "FROM inventory i "
                + "INNER JOIN products p ON i.product_id = p.product_id "
                + "ORDER BY p.product_name";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                inventories.add(mapResultSetToInventory(rs));
            }
        }
        return inventories;
    }

    @Override
    public List<Inventory> findLowStock() throws SQLException {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "SELECT i.inventory_id, i.product_id, i.current_stock, i.min_stock, "
                + "i.max_stock, i.last_restock_date, i.last_restock_quantity, i.updated_at, "
                + "p.product_name, p.product_code "
                + "FROM inventory i "
                + "INNER JOIN products p ON i.product_id = p.product_id "
                + "WHERE i.current_stock <= i.min_stock "
                + "ORDER BY i.current_stock ASC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                inventories.add(mapResultSetToInventory(rs));
            }
        }
        return inventories;
    }

    @Override
    public Integer create(Inventory inventory) throws SQLException {
        String sql = "INSERT INTO inventory (product_id, current_stock, min_stock, max_stock) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, inventory.getProductId());
            stmt.setInt(2, inventory.getCurrentStock());
            stmt.setInt(3, inventory.getMinStock());
            stmt.setInt(4, inventory.getMaxStock());

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
    public boolean update(Inventory inventory) throws SQLException {
        String sql = "UPDATE inventory SET current_stock = ?, min_stock = ?, max_stock = ? "
                + "WHERE inventory_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inventory.getCurrentStock());
            stmt.setInt(2, inventory.getMinStock());
            stmt.setInt(3, inventory.getMaxStock());
            stmt.setInt(4, inventory.getInventoryId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStock(Integer productId, Integer newStock) throws SQLException {
        String sql = "UPDATE inventory SET current_stock = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newStock);
            stmt.setInt(2, productId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean incrementStock(Integer productId, Integer quantity) throws SQLException {
        String sql = "UPDATE inventory SET current_stock = current_stock + ?, "
                + "last_restock_date = NOW(), last_restock_quantity = ? "
                + "WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, quantity);
            stmt.setInt(3, productId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean decrementStock(Integer productId, Integer quantity) throws SQLException {
        String sql = "UPDATE inventory SET current_stock = current_stock - ? "
                + "WHERE product_id = ? AND current_stock >= ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer inventoryId) throws SQLException {
        String sql = "DELETE FROM inventory WHERE inventory_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inventoryId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Inventory mapResultSetToInventory(ResultSet rs) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(rs.getInt("inventory_id"));
        inventory.setProductId(rs.getInt("product_id"));
        inventory.setCurrentStock(rs.getInt("current_stock"));
        inventory.setMinStock(rs.getInt("min_stock"));
        inventory.setMaxStock(rs.getInt("max_stock"));
        inventory.setLastRestockDate(rs.getTimestamp("last_restock_date"));
        inventory.setLastRestockQuantity(rs.getObject("last_restock_quantity", Integer.class));
        inventory.setUpdatedAt(rs.getTimestamp("updated_at"));
        inventory.setProductName(rs.getString("product_name"));
        inventory.setProductCode(rs.getString("product_code"));
        return inventory;
    }
}
