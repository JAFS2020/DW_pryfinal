package pe.edu.pizzanajani.config;

public class DatabaseConfig {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/pizza_najani_db?connectTimeout=20000";
    private static final String DB_USER = "pizza_najani_app";
    private static final String DB_PASSWORD = "PizzaN@jani2025!";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }

    public static String getDbDriver() {
        return DB_DRIVER;
    }
}
