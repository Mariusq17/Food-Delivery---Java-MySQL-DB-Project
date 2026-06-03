package ro.delivery.repository;
import ro.delivery.config.DatabaseConfiguration;
import ro.delivery.model.Restaurant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {
    private final GenericRepository generic = GenericRepository.getInstance();

    public void insert(Restaurant r) {
        String sql = "INSERT INTO restaurante (nume) VALUES (?)";
        generic.executeUpdate(sql, r.getNume());
    }

    public List<Restaurant> findAll() {
        List<Restaurant> lista = new ArrayList<>();
        String sql = "SELECT * FROM restaurante";
        try (Connection conn = DatabaseConfiguration.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Restaurant(rs.getInt("id"), rs.getString("nume")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Restaurant findById(int id) {
        String sql = "SELECT * FROM restaurante WHERE id = ?";
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return new Restaurant(rs.getInt("id"), rs.getString("nume"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void update(int id, String nume) {
        String sql = "UPDATE restaurante SET nume = ? WHERE id = ?";
        generic.executeUpdate(sql, nume, id);
    }

    public void delete(int id) {
        String sql = "DELETE FROM restaurante WHERE id = ?";
        generic.executeUpdate(sql, id);
    }
}