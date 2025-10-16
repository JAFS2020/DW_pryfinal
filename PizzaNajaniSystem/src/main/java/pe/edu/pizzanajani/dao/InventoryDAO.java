package pe.edu.pizzanajani.dao;

import pe.edu.pizzanajani.model.Inventory;
import java.sql.SQLException;
import java.util.List;

public interface InventoryDAO {

    Inventory findById(Integer inventoryId) throws SQLException;

    Inventory findByProductId(Integer productId) throws SQLException;

    List<Inventory> findAll() throws SQLException;

    List<Inventory> findLowStock() throws SQLException;

    Integer create(Inventory inventory) throws SQLException;

    boolean update(Inventory inventory) throws SQLException;

    boolean updateStock(Integer productId, Integer newStock) throws SQLException;

    boolean incrementStock(Integer productId, Integer quantity) throws SQLException;

    boolean decrementStock(Integer productId, Integer quantity) throws SQLException;

    boolean delete(Integer inventoryId) throws SQLException;
}
