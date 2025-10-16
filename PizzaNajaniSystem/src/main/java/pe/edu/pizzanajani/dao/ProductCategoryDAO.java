package pe.edu.pizzanajani.dao;

import pe.edu.pizzanajani.model.ProductCategory;
import java.sql.SQLException;
import java.util.List;

public interface ProductCategoryDAO {

    ProductCategory findById(Integer categoryId) throws SQLException;

    ProductCategory findByName(String categoryName) throws SQLException;

    List<ProductCategory> findAll() throws SQLException;

    List<ProductCategory> findAllActive() throws SQLException;

    Integer create(ProductCategory category) throws SQLException;

    boolean update(ProductCategory category) throws SQLException;

    boolean delete(Integer categoryId) throws SQLException;

    boolean updateStatus(Integer categoryId, boolean isActive) throws SQLException;
}
