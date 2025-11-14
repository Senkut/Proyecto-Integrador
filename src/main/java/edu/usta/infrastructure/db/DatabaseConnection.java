package edu.usta.infrastructure.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * La clase DatabaseConeection implementaci el patron de diseño
 * creacional Singleton, el cual permite crear una unica instancia
 * que es usada en toda La aplicacion, permitiendo que no se creen
 * multiples objetos de esta clase, reduciendo asi uso de memoria
 * y evitando multiples llamados a la base de datos.
 */

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String url, user, password;

    private DatabaseConnection() {
        var dotenv = Dotenv.load();
        url = dotenv.get("DB_URL");
        user = dotenv.get("DB_USER");
        password = dotenv.get("DB_PASSWORD");
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null)
            instance = new DatabaseConnection();

        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}
