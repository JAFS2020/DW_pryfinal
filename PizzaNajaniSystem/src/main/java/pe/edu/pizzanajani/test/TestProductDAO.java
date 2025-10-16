package pe.edu.pizzanajani.test;

import pe.edu.pizzanajani.dao.ProductDAO;
import pe.edu.pizzanajani.dao.ProductCategoryDAO;
import pe.edu.pizzanajani.dao.impl.ProductDAOImpl;
import pe.edu.pizzanajani.dao.impl.ProductCategoryDAOImpl;

public class TestProductDAO {

    public static void main(String[] args) {
        System.out.println("=== TEST DE PRODUCT DAO ===\n");

        ProductCategoryDAO categoryDAO = new ProductCategoryDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();

        try {
            System.out.println("1. Listar todas las categorías:");
            var categories = categoryDAO.findAllActive();
            System.out.println("   Total de categorías activas: " + categories.size());
            categories.forEach(c
                    -> System.out.println("      - " + c.getCategoryName() + " (" + c.getDescription() + ")")
            );

            System.out.println("\n2. Listar todos los productos disponibles:");
            var products = productDAO.findAllAvailable();
            System.out.println("   Total de productos disponibles: " + products.size());

            System.out.println("\n3. Listar pizzas disponibles:");
            var pizzas = productDAO.findAvailableByCategory(1);
            System.out.println("   Total de pizzas: " + pizzas.size());
            pizzas.forEach(p
                    -> System.out.println("      - " + p.getProductName() + " - S/. " + p.getPrice())
            );

            System.out.println("\n4. Buscar producto 'PIZ004':");
            var pizza = productDAO.findByCode("PIZ004");
            if (pizza != null) {
                System.out.println("   Encontrado: " + pizza.getProductName());
                System.out.println("   Precio: S/. " + pizza.getPrice());
                System.out.println("   Categoría: " + pizza.getCategoryName());
                System.out.println("   Tiempo prep: " + pizza.getPreparationTime() + " min");
            }

            // Test 5: Buscar productos por nombre
            System.out.println("\n5. Buscar productos con 'Pizza' en el nombre:");
            var searchResults = productDAO.searchByName("Pizza");
            System.out.println("   Resultados encontrados: " + searchResults.size());
            searchResults.forEach(p
                    -> System.out.println("      - " + p.getProductName())
            );

            System.out.println("\nTodos los tests completados exitosamente");

        } catch (Exception e) {
            System.err.println("Error en test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== FIN DEL TEST ===");
    }
}
