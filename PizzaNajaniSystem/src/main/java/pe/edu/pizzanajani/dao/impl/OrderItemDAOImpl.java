package pe.edu.pizzanajani.dao.impl;

import pe.edu.pizzanajani.config.DatabaseConnection;
import pe.edu.pizzanajani.dao.OrderItemDAO;
import pe.edu.pizzanajani.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO {

    @Override
    public OrderItem findById(Integer orderItemId) throws SQLException {
        String sql = "SELECT order_item_id, order_id, product_id, product_name, "
                + "quantity, unit_price, subtotal, special_instructions, created_at "
                + "FROM order_items WHERE order_item_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItemId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrderItem(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<OrderItem> findByOrderId(Integer orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT order_item_id, order_id, product_id, product_name, "
                + "quantity, unit_price, subtotal, special_instructions, created_at "
                + "FROM order_items WHERE order_id = ? ORDER BY order_item_id";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToOrderItem(rs));
                }
            }
        }
        return items;
    }

    @Override
    public Integer create(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, product_name, "
                + "quantity, unit_price, special_instructions) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getProductId());
            stmt.setString(3, orderItem.getProductName());
            stmt.setInt(4, orderItem.getQuantity());
            stmt.setBigDecimal(5, orderItem.getUnitPrice());
            stmt.setString(6, orderItem.getSpecialInstructions());

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
    public boolean update(OrderItem orderItem) throws SQLException {
        String sql = "UPDATE order_items SET quantity = ?, unit_price = ?, "
                + "special_instructions = ? WHERE order_item_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItem.getQuantity());
            stmt.setBigDecimal(2, orderItem.getUnitPrice());
            stmt.setString(3, orderItem.getSpecialInstructions());
            stmt.setInt(4, orderItem.getOrderItemId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer orderItemId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItemId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteByOrderId(Integer orderId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));
        item.setSpecialInstructions(rs.getString("special_instructions"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        return item;
    }
}
