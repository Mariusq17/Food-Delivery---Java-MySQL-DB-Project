package ro.delivery.repository;

import ro.delivery.config.DatabaseConfiguration;
import java.sql.*;

// Singleton Generic pentru operatii DB
public class GenericRepository {
    private static GenericRepository instance;

    private GenericRepository() {}

    public static GenericRepository getInstance() {
        if (instance == null) {
            instance = new GenericRepository();
        }
        return instance;
    }

    // Metoda pentru Insert, Update, Delete
    public void executeUpdate(String sql, Object... params) {
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Eroare la executie SQL: " + e.getMessage());
        }
    }
}