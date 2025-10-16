package pe.edu.pizzanajani.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DatabaseConfig.getDbDriver());

            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        DatabaseConfig.getDbUrl(),
                        DatabaseConfig.getDbUser(),
                        DatabaseConfig.getDbPassword()
                );
                System.out.println("Conexión exitosa a la base de datos");
            }

            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver MySQL no encontrado");
            throw new SQLException("Driver no encontrado", e);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            throw e;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            boolean isValid = conn != null && !conn.isClosed();
            System.out.println(isValid ? "Test de conexión: EXITOSO" : "Test de conexión: FALLIDO");
            return isValid;
        } catch (SQLException e) {
            System.err.println("Test de conexión: FALLIDO - " + e.getMessage());
            return false;
        }
    }
}
