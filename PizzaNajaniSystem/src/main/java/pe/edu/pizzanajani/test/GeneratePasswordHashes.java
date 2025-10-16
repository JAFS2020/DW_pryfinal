package pe.edu.pizzanajani.test;

import pe.edu.pizzanajani.util.PasswordUtil;
import pe.edu.pizzanajani.util.PasswordUtil;

public class GeneratePasswordHashes {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     GENERADOR DE HASHES SHA-256 - PIZZERÍA NAJANI         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        String password = "Pizza123!";
        
        System.out.println("🔑 Contraseña original: " + password);
        System.out.println("🔐 Algoritmo: SHA-256 con Salt");
        System.out.println("📦 Formato: salt:hash\n");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        System.out.println("👤 Usuario: admin");
        String hashAdmin = PasswordUtil.hashPassword(password);
        System.out.println("Hash: " + hashAdmin);
        System.out.println("\nSQL:");
        System.out.println("UPDATE users SET password_hash = '" + hashAdmin + "' WHERE username = 'admin';");
        System.out.println("\n-----------------------------------------------------------\n");
        
        System.out.println("👤 Usuario: vendedor1");
        String hashVendedor1 = PasswordUtil.hashPassword(password);
        System.out.println("Hash: " + hashVendedor1);
        System.out.println("\nSQL:");
        System.out.println("UPDATE users SET password_hash = '" + hashVendedor1 + "' WHERE username = 'vendedor1';");
        System.out.println("\n-----------------------------------------------------------\n");
        
        System.out.println("👤 Usuario: vendedor2");
        String hashVendedor2 = PasswordUtil.hashPassword(password);
        System.out.println("Hash: " + hashVendedor2);
        System.out.println("\nSQL:");
        System.out.println("UPDATE users SET password_hash = '" + hashVendedor2 + "' WHERE username = 'vendedor2';");
        System.out.println("\n-----------------------------------------------------------\n");
        
        System.out.println("👤 Usuario: cocinero1");
        String hashCocinero1 = PasswordUtil.hashPassword(password);
        System.out.println("Hash: " + hashCocinero1);
        System.out.println("\nSQL:");
        System.out.println("UPDATE users SET password_hash = '" + hashCocinero1 + "' WHERE username = 'cocinero1';");
        System.out.println("\n-----------------------------------------------------------\n");
        
        System.out.println("👤 Usuario: cocinero2");
        String hashCocinero2 = PasswordUtil.hashPassword(password);
        System.out.println("Hash: " + hashCocinero2);
        System.out.println("\nSQL:");
        System.out.println("UPDATE users SET password_hash = '" + hashCocinero2 + "' WHERE username = 'cocinero2';");
        System.out.println("\n═══════════════════════════════════════════════════════════════\n");
        
        System.out.println("🧪 PRUEBA DE VERIFICACIÓN:\n");
        
        System.out.println("Test 1 - Verificar password correcta:");
        boolean test1 = PasswordUtil.verifyPassword(password, hashAdmin);
        System.out.println("   Password: '" + password + "'");
        System.out.println("   Hash: " + hashAdmin.substring(0, 30) + "...");
        System.out.println("   Resultado: " + (test1 ? "✅ CORRECTA" : "❌ FALLIDA"));
        
        System.out.println("\nTest 2 - Verificar password incorrecta:");
        boolean test2 = PasswordUtil.verifyPassword("WrongPassword", hashAdmin);
        System.out.println("   Password: 'WrongPassword'");
        System.out.println("   Hash: " + hashAdmin.substring(0, 30) + "...");
        System.out.println("   Resultado: " + (test2 ? "❌ ERROR (debería ser false)" : "✅ CORRECTA (rechazó la incorrecta)"));
        
        System.out.println("\nTest 3 - Verificar todos los hashes generados:");
        boolean allValid = 
            PasswordUtil.verifyPassword(password, hashAdmin) &&
            PasswordUtil.verifyPassword(password, hashVendedor1) &&
            PasswordUtil.verifyPassword(password, hashVendedor2) &&
            PasswordUtil.verifyPassword(password, hashCocinero1) &&
            PasswordUtil.verifyPassword(password, hashCocinero2);
        System.out.println("   Todos los hashes: " + (allValid ? "✅ VÁLIDOS" : "❌ ERROR"));
        
        System.out.println("\n═══════════════════════════════════════════════════════════════\n");
        System.out.println("📋 INSTRUCCIONES:\n");
        System.out.println("1. Copia TODOS los UPDATE statements de arriba");
        System.out.println("2. Pégalos y ejecútalos en MySQL");
        System.out.println("3. Verifica con: SELECT username, password_hash FROM users;");
        System.out.println("4. Inicia sesión en el sistema con:");
        System.out.println("   - Usuario: admin (o cualquier otro)");
        System.out.println("   - Contraseña: Pizza123!");
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
}