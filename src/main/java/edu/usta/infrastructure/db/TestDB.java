package edu.usta.infrastructure.db;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            Connection conn = db.getConnection();

            if (conn != null) {
                System.out.println("✔ Conexión exitosa a la base de datos.");
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión:");
            e.printStackTrace();
        }
    }
}
