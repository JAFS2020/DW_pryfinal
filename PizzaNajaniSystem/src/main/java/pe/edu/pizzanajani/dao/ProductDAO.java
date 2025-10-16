package pe.edu.pizzanajani.dao;

import pe.edu.pizzanajani.model.Product;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {

    Product findById(Integer productId) throws SQLException;

    Product findByCode(String productCode) throws SQLException;

    List<Product> findAll() throws SQLException;

    List<Product> findByCategory(Integer categoryId) throws SQLException;

    List<Product> findAllAvailable() throws SQLException;

    List<Product> findAvailableByCategory(Integer categoryId) throws SQLException;

    List<Product> searchByName(String searchTerm) throws SQLException;

    Integer create(Product product) throws SQLException;

    boolean update(Product product) throws SQLException;

    boolean delete(Integer productId) throws SQLException;

    boolean updateAvailability(Integer productId, boolean isAvailable) throws SQLException;

    boolean updatePrice(Integer productId, java.math.BigDecimal newPrice) throws SQLException;
}
