package ro.delivery.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    private static final String URL = "jdbc:mysql://localhost:3306/food_delivery";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // La XAMPP parola e goala de obicei

    private static Connection connection;

    private DatabaseConfiguration() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}