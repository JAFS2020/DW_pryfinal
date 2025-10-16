package pe.edu.pizzanajani.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import pe.edu.pizzanajani.config.DatabaseConnection;

public class TestDbConnection {

    public static void main(String[] args) {
        System.out.println("=== TEST DE CONEXIÓN A BASE DE DATOS ===\n");

        if (DatabaseConnection.testConnection()) {
            System.out.println("\nConexión establecida correctamente\n");

            try {
                Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM users");
                if (rs.next()) {
                    System.out.println("Total de usuarios en BD: " + rs.getInt("total"));
                }

                rs = stmt.executeQuery("SELECT role_name FROM roles");
                System.out.println("\nRoles disponibles:");
                while (rs.next()) {
                    System.out.println("   - " + rs.getString("role_name"));
                }

                rs.close();
                stmt.close();

                System.out.println("\nTodas las pruebas completadas exitosamente");

            } catch (Exception e) {
                System.err.println("Error en consulta: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("\nNo se pudo establecer conexión");
        }

        DatabaseConnection.closeConnection();
        System.out.println("\n=== FIN DEL TEST ===");
    }
}
