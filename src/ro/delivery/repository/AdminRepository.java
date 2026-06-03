package ro.delivery.repository;
import ro.delivery.config.DatabaseConfiguration;
import java.sql.*;

public class AdminRepository {
    public boolean login(String user, String pass) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) { return false; }
    }
}