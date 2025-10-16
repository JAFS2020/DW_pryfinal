package pe.edu.pizzanajani.test;

import pe.edu.pizzanajani.util.PasswordUtil;

public class TestPasswordUtil {

    public static void main(String[] args) {
        System.out.println("=== GENERADOR DE HASHES SHA-256 ===\n");

        String password = "T3st$2025";

        System.out.println("Generando 5 hashes para los usuarios:");
        System.out.println("Password común: " + password);
        System.out.println();

        System.out.println("-- Hash para admin:");
        String hashAdmin = PasswordUtil.hashPassword(password);
        System.out.println("UPDATE users SET password_hash = '" + hashAdmin + "' WHERE username = 'admin';");

        System.out.println("\n-- Hash para vendedor1:");
        String hashVendedor1 = PasswordUtil.hashPassword(password);
        System.out.println("UPDATE users SET password_hash = '" + hashVendedor1 + "' WHERE username = 'vendedor1';");

        System.out.println("\n-- Hash para vendedor2:");
        String hashVendedor2 = PasswordUtil.hashPassword(password);
        System.out.println("UPDATE users SET password_hash = '" + hashVendedor2 + "' WHERE username = 'vendedor2';");

        System.out.println("\n-- Hash para cocinero1:");
        String hashCocinero1 = PasswordUtil.hashPassword(password);
        System.out.println("UPDATE users SET password_hash = '" + hashCocinero1 + "' WHERE username = 'cocinero1';");

        System.out.println("\n-- Hash para cocinero2:");
        String hashCocinero2 = PasswordUtil.hashPassword(password);
        System.out.println("UPDATE users SET password_hash = '" + hashCocinero2 + "' WHERE username = 'cocinero2';");

        System.out.println("\n\n=== PRUEBA DE VERIFICACIÓN ===\n");

        boolean isValid = PasswordUtil.verifyPassword(password, hashAdmin);
        System.out.println("Verificación con password correcta: " + (isValid ? "VÁLIDA" : "INVÁLIDA"));

        boolean isInvalid = PasswordUtil.verifyPassword("WrongPassword", hashAdmin);
        System.out.println("Verificación con password incorrecta: " + (isInvalid ? "ERROR" : "CORRECTA (debe ser false)"));

        System.out.println("\n=== COPIA LOS UPDATE STATEMENTS Y EJECÚTALOS EN MYSQL ===");
    }
}
