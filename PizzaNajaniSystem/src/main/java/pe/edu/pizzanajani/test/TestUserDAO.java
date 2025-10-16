package pe.edu.pizzanajani.test;

import pe.edu.pizzanajani.dao.UserDAO;
import pe.edu.pizzanajani.dao.impl.UserDAOImpl;
import pe.edu.pizzanajani.model.User;

public class TestUserDAO {

    public static void main(String[] args) {
        System.out.println("=== TEST DE USER DAO ===\n");

        UserDAO userDAO = new UserDAOImpl();

        try {
            System.out.println("1. Buscar usuario 'admin':");
            User admin = userDAO.findByUsername("admin");
            if (admin != null) {
                System.out.println("   Usuario encontrado: " + admin.getFullName());
                System.out.println("   Email: " + admin.getEmail());
                System.out.println("   Roles: ");
                admin.getRoles().forEach(role
                        -> System.out.println("      - " + role.getRoleName())
                );
            } else {
                System.out.println("   Usuario no encontrado");
            }

            System.out.println("\n2. Validar credenciales:");
            User validUser = userDAO.validateCredentials("vendedor1", "$2a$10$rZJ8KJbM9xvLq5z7Y3H3uOXKj5L8N9mP0qR1sT2uV3wX4yZ5aB6cC");
            if (validUser != null) {
                System.out.println("   Credenciales válidas para: " + validUser.getFullName());
                System.out.println("   Roles del usuario:");
                validUser.getRoles().forEach(role
                        -> System.out.println("      - " + role.getRoleName())
                );
            } else {
                System.out.println("   Credenciales inválidas");
            }

            System.out.println("\n3. Buscar todos los vendedores:");
            var vendedores = userDAO.findByRole("VENDEDOR");
            System.out.println("   Total de vendedores: " + vendedores.size());
            vendedores.forEach(v
                    -> System.out.println("      - " + v.getFullName() + " (" + v.getUsername() + ")")
            );

            System.out.println("\n4. Buscar todos los cocineros:");
            var cocineros = userDAO.findByRole("COCINERO");
            System.out.println("   Total de cocineros: " + cocineros.size());
            cocineros.forEach(c
                    -> System.out.println("      - " + c.getFullName() + " (" + c.getUsername() + ")")
            );

            System.out.println("\n5. Listar todos los usuarios:");
            var allUsers = userDAO.findAll();
            System.out.println("   Total de usuarios: " + allUsers.size());
            allUsers.forEach(u -> {
                System.out.println("      - " + u.getFullName() + " (" + u.getUsername() + ")");
                System.out.print("        Roles: ");
                u.getRoles().forEach(r -> System.out.print(r.getRoleName() + " "));
                System.out.println();
            });

            System.out.println("\n Todos los tests completados exitosamente");

        } catch (Exception e) {
            System.err.println("Error en test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== FIN DEL TEST ===");
    }
}