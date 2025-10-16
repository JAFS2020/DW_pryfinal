package pe.edu.pizzanajani.dao.impl;

import pe.edu.pizzanajani.config.DatabaseConnection;
import pe.edu.pizzanajani.dao.OrderDAO;
import pe.edu.pizzanajani.dao.OrderItemDAO;
import pe.edu.pizzanajani.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    private final OrderItemDAO orderItemDAO;

    public OrderDAOImpl() {
        this.orderItemDAO = new OrderItemDAOImpl();
    }

    @Override
    public Order findById(Integer orderId) throws SQLException {
        String sql = "SELECT o.order_id, o.order_number, o.customer_name, o.customer_phone, "
                + "o.customer_address, o.created_by, o.order_status, o.order_type, "
                + "o.subtotal, o.tax, o.delivery_fee, o.total, o.payment_method, "
                + "o.payment_status, o.notes, o.assigned_cook, o.started_at, "
                + "o.completed_at, o.delivered_at, o.created_at, o.updated_at, "
                + "u.full_name as created_by_name, c.full_name as assigned_cook_name "
                + "FROM orders o "
                + "INNER JOIN users u ON o.created_by = u.user_id "
                + "LEFT JOIN users c ON o.assigned_cook = c.user_id "
                + "WHERE o.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(orderItemDAO.findByOrderId(orderId));
                    return order;
                }
            }
        }
        return null;
    }

    @Override
    public Order findByOrderNumber(String orderNumber) throws SQLException {
        String sql = "SELECT o.order_id, o.order_number, o.customer_name, o.customer_phone, "
                + "o.customer_address, o.created_by, o.order_status, o.order_type, "
                + "o.subtotal, o.tax, o.delivery_fee, o.total, o.payment_method, "
                + "o.payment_status, o.notes, o.assigned_cook, o.started_at, "
                + "o.completed_at, o.delivered_at, o.created_at, o.updated_at, "
                + "u.full_name as created_by_name, c.full_name as assigned_cook_name "
                + "FROM orders o "
                + "INNER JOIN users u ON o.created_by = u.user_id "
                + "LEFT JOIN users c ON o.assigned_cook = c.user_id "
                + "WHERE o.order_number = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(orderItemDAO.findByOrderId(order.getOrderId()));
                    return order;
                }
            }
        }
        return null;
    }

    @Override
    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_number, o.customer_name, o.customer_phone, "
                + "o.customer_address, o.created_by, o.order_status, o.order_type, "
                + "o.subtotal, o.tax, o.delivery_fee, o.total, o.payment_method, "
                + "o.payment_status, o.notes, o.assigned_cook, o.started_at, "
                + "o.completed_at, o.delivered_at, o.created_at, o.updated_at, "
                + "u.full_name as created_by_name, c.full_name as assigned_cook_name "
                + "FROM orders o "
                + "INNER JOIN users u ON o.created_by = u.user_id "
                + "LEFT JOIN users c ON o.assigned_cook = c.user_id "
                + "ORDER BY o.created_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }

            rs.close();

            for (Order order : orders) {
                order.setItems(orderItemDAO.findByOrderId(order.getOrderId()));
            }

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
            }
        }

        return orders;
    }

    @Override
    public List<Order> findByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_number, o.customer_name, o.customer_phone, "
                + "o.customer_address, o.created_by, o.order_status, o.order_type, "
                + "o.subtotal, o.tax, o.delivery_fee, o.total, o.payment_method, "
                + "o.payment_status, o.notes, o.assigned_cook, o.started_at, "
                + "o.completed_at, o.delivered_at, o.created_at, o.updated_at, "
                + "u.full_name as created_by_name, c.full_name as assigned_cook_name "
                + "FROM orders o "
                + "INNER JOIN users u ON o.created_by = u.user_id "
                + "LEFT JOIN users c ON o.assigned_cook = c.user_id "
                + "WHERE o.order_status = ? "
                + "ORDER BY o.created_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }

            rs.close();

            for (Order order : orders) {
                order.setItems(orderItemDAO.findByOrderId(order.getOrderId()));
            }

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
            }
        }

        return orders;
    }

    @Override
    public List<Order> findByCreatedBy(Integer userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_number, o.customer_name, o.customer_phone, "
                + "o.customer_address, o.created_by, o.order_status, o.order_type, "
                + "o.subtotal, o.tax, o.delivery_fee, o.total, o.payment_method, "
                + "o.payment_status, o.notes, o.assigned_cook, o.started_at, "
                + "o.completed_at, o.delivered_at, o.created_at, o.updated_at, "
                + "u.full_name as created_by_name, c.full_name as assigned_cook_name "
                + "FROM orders o "
                + "INNER JOIN users u ON o.created_by = u.user_id "
                + "LEFT JOIN users c ON o.assigned_cook = c.user_id "
                + "WHERE o.created_by = ? "
                + "ORDER BY o.created_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }

            rs.close();

            for (Order order : orders) {
                order.setItems(orderItemDAO.findByOrderId(order.getOrderId()));
            }

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
            }
        }

        return orders;
    }

    @Override
    public List<Order> findByAssignedCook(Integer cookId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_number, o.customer_name, o.customer_phone, "
                + "o.customer_address, o.created_by, o.order_status, o.order_type, "
                + "o.subtotal, o.tax, o.delivery_fee, o.total, o.payment_method, "
                + "o.payment_status, o.notes, o.assigned_cook, o.started_at, "
                + "o.completed_at, o.delivered_at, o.created_at, o.updated_at, "
                + "u.full_name as created_by_name, c.full_name as assigned_cook_name "
                + "FROM orders o "
                + "INNER JOIN users u ON o.created_by = u.user_id "
                + "LEFT JOIN users c ON o.assigned_cook = c.user_id "
                + "WHERE o.assigned_cook = ? "
                + "ORDER BY o.created_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cookId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }

            rs.close();

            for (Order order : orders) {
                order.setItems(orderItemDAO.findByOrderId(order.getOrderId()));
            }

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
            }
        }

        return orders;
    }

    @Override
    public List<Order> findPendingOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_number, o.customer_name, o.customer_phone, "
                + "o.customer_address, o.created_by, o.order_status, o.order_type, "
                + "o.subtotal, o.tax, o.delivery_fee, o.total, o.payment_method, "
                + "o.payment_status, o.notes, o.assigned_cook, o.started_at, "
                + "o.completed_at, o.delivered_at, o.created_at, o.updated_at, "
                + "u.full_name as created_by_name, c.full_name as assigned_cook_name "
                + "FROM orders o "
                + "INNER JOIN users u ON o.created_by = u.user_id "
                + "LEFT JOIN users c ON o.assigned_cook = c.user_id "
                + "WHERE o.order_status IN ('PENDING', 'IN_PROGRESS') "
                + "ORDER BY o.created_at ASC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }

            rs.close();

            for (Order order : orders) {
                order.setItems(orderItemDAO.findByOrderId(order.getOrderId()));
            }

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
            }
        }

        return orders;
    }

    @Override
    public List<Order> findTodayOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_number, o.customer_name, o.customer_phone, "
                + "o.customer_address, o.created_by, o.order_status, o.order_type, "
                + "o.subtotal, o.tax, o.delivery_fee, o.total, o.payment_method, "
                + "o.payment_status, o.notes, o.assigned_cook, o.started_at, "
                + "o.completed_at, o.delivered_at, o.created_at, o.updated_at, "
                + "u.full_name as created_by_name, c.full_name as assigned_cook_name "
                + "FROM orders o "
                + "INNER JOIN users u ON o.created_by = u.user_id "
                + "LEFT JOIN users c ON o.assigned_cook = c.user_id "
                + "WHERE DATE(o.created_at) = CURDATE() "
                + "ORDER BY o.created_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }

            rs.close();

            for (Order order : orders) {
                order.setItems(orderItemDAO.findByOrderId(order.getOrderId()));
            }

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
            }
        }

        return orders;
    }

    @Override
    public Integer create(Order order) throws SQLException {
        String sql = "INSERT INTO orders (order_number, customer_name, customer_phone, "
                + "customer_address, created_by, order_status, order_type, subtotal, tax, "
                + "delivery_fee, total, payment_method, payment_status, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getOrderNumber());
            stmt.setString(2, order.getCustomerName());
            stmt.setString(3, order.getCustomerPhone());
            stmt.setString(4, order.getCustomerAddress());
            stmt.setInt(5, order.getCreatedBy());
            stmt.setString(6, order.getOrderStatus());
            stmt.setString(7, order.getOrderType());
            stmt.setBigDecimal(8, order.getSubtotal());
            stmt.setBigDecimal(9, order.getTax());
            stmt.setBigDecimal(10, order.getDeliveryFee());
            stmt.setBigDecimal(11, order.getTotal());
            stmt.setString(12, order.getPaymentMethod());
            stmt.setString(13, order.getPaymentStatus());
            stmt.setString(14, order.getNotes());

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
    public boolean update(Order order) throws SQLException {
        String sql = "UPDATE orders SET customer_name = ?, customer_phone = ?, "
                + "customer_address = ?, order_status = ?, order_type = ?, "
                + "subtotal = ?, tax = ?, delivery_fee = ?, total = ?, "
                + "payment_method = ?, payment_status = ?, notes = ? "
                + "WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getCustomerName());
            stmt.setString(2, order.getCustomerPhone());
            stmt.setString(3, order.getCustomerAddress());
            stmt.setString(4, order.getOrderStatus());
            stmt.setString(5, order.getOrderType());
            stmt.setBigDecimal(6, order.getSubtotal());
            stmt.setBigDecimal(7, order.getTax());
            stmt.setBigDecimal(8, order.getDeliveryFee());
            stmt.setBigDecimal(9, order.getTotal());
            stmt.setString(10, order.getPaymentMethod());
            stmt.setString(11, order.getPaymentStatus());
            stmt.setString(12, order.getNotes());
            stmt.setInt(13, order.getOrderId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(Integer orderId, String newStatus, Integer userId) throws SQLException {
        String sql = "UPDATE orders SET order_status = ?, "
                + "started_at = CASE WHEN ? = 'IN_PROGRESS' AND started_at IS NULL THEN NOW() ELSE started_at END, "
                + "completed_at = CASE WHEN ? = 'READY' AND completed_at IS NULL THEN NOW() ELSE completed_at END, "
                + "delivered_at = CASE WHEN ? = 'DELIVERED' AND delivered_at IS NULL THEN NOW() ELSE delivered_at END "
                + "WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, newStatus);
            stmt.setString(3, newStatus);
            stmt.setString(4, newStatus);
            stmt.setInt(5, orderId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean assignCook(Integer orderId, Integer cookId) throws SQLException {
        String sql = "UPDATE orders SET assigned_cook = ? WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cookId);
            stmt.setInt(2, orderId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean markAsPaid(Integer orderId, String paymentMethod) throws SQLException {
        String sql = "UPDATE orders SET payment_status = 'PAID', payment_method = ? WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paymentMethod);
            stmt.setInt(2, orderId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cancelOrder(Integer orderId, Integer userId) throws SQLException {
        String sql = "UPDATE orders SET order_status = 'CANCELLED' WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public String generateOrderNumber() throws SQLException {
        String sql = "SELECT fn_generate_order_number() as order_number";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("order_number");
            }
        }

        return "ORD-" + System.currentTimeMillis();
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setCustomerPhone(rs.getString("customer_phone"));
        order.setCustomerAddress(rs.getString("customer_address"));
        order.setCreatedBy(rs.getInt("created_by"));
        order.setOrderStatus(rs.getString("order_status"));
        order.setOrderType(rs.getString("order_type"));
        order.setSubtotal(rs.getBigDecimal("subtotal"));
        order.setTax(rs.getBigDecimal("tax"));
        order.setDeliveryFee(rs.getBigDecimal("delivery_fee"));
        order.setTotal(rs.getBigDecimal("total"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setPaymentStatus(rs.getString("payment_status"));
        order.setNotes(rs.getString("notes"));
        order.setAssignedCook(rs.getObject("assigned_cook", Integer.class));
        order.setStartedAt(rs.getTimestamp("started_at"));
        order.setCompletedAt(rs.getTimestamp("completed_at"));
        order.setDeliveredAt(rs.getTimestamp("delivered_at"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        order.setCreatedByName(rs.getString("created_by_name"));
        order.setAssignedCookName(rs.getString("assigned_cook_name"));
        return order;
    }
}
