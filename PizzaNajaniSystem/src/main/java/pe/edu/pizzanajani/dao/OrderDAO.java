package pe.edu.pizzanajani.dao;

import pe.edu.pizzanajani.model.Order;
import java.sql.SQLException;
import java.util.List;

public interface OrderDAO {

    Order findById(Integer orderId) throws SQLException;

    Order findByOrderNumber(String orderNumber) throws SQLException;

    List<Order> findAll() throws SQLException;

    List<Order> findByStatus(String status) throws SQLException;

    List<Order> findByCreatedBy(Integer userId) throws SQLException;

    List<Order> findByAssignedCook(Integer cookId) throws SQLException;

    List<Order> findPendingOrders() throws SQLException;

    List<Order> findTodayOrders() throws SQLException;

    Integer create(Order order) throws SQLException;

    boolean update(Order order) throws SQLException;

    boolean updateStatus(Integer orderId, String newStatus, Integer userId) throws SQLException;

    boolean assignCook(Integer orderId, Integer cookId) throws SQLException;

    boolean markAsPaid(Integer orderId, String paymentMethod) throws SQLException;

    boolean cancelOrder(Integer orderId, Integer userId) throws SQLException;

    String generateOrderNumber() throws SQLException;
}
