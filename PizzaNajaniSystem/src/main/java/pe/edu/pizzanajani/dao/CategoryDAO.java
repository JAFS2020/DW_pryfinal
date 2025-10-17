package pe.edu.pizzanajani.dao;

import pe.edu.pizzanajani.model.ProductCategory;
import java.util.List;

public interface CategoryDAO {

    Integer create(ProductCategory category);

    ProductCategory findById(Integer categoryId);

    List<ProductCategory> findAll();

    boolean update(ProductCategory category);

    boolean delete(Integer categoryId);

    List<ProductCategory> findActive();

    ProductCategory findByName(String categoryName);

    boolean updateStatus(Integer categoryId, Boolean isActive);
}
