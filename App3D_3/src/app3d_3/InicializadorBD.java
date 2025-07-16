
package app3d_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InicializadorBD {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    public static void inicializar() {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Statement stmt = conn.createStatement()) {

            // Crear base de datos si no existe
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS clima CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci");

            // Usar la base y crear tabla usuario si no existe
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS clima.usuario (" +
                "nickname VARCHAR(50) PRIMARY KEY, " +
                "contrasena VARCHAR(50), " +
                "pais VARCHAR(50), " +
                "nombre VARCHAR(50), " +
                "apellido VARCHAR(50)" +
                ")"
            );

            System.out.println("✅ Base de datos y tabla 'usuario' listas.");

        } catch (Exception e) {
            System.err.println("❌ Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
