package pe.edu.pizzanajani.dao;

import pe.edu.pizzanajani.model.OrderItem;
import java.sql.SQLException;
import java.util.List;

public interface OrderItemDAO {

    OrderItem findById(Integer orderItemId) throws SQLException;

    List<OrderItem> findByOrderId(Integer orderId) throws SQLException;

    Integer create(OrderItem orderItem) throws SQLException;

    boolean update(OrderItem orderItem) throws SQLException;

    boolean delete(Integer orderItemId) throws SQLException;

    boolean deleteByOrderId(Integer orderId) throws SQLException;
}
