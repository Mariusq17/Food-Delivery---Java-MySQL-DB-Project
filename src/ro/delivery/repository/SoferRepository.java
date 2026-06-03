package ro.delivery.repository;
import ro.delivery.config.DatabaseConfiguration;
import ro.delivery.model.Sofer;
import java.sql.*;
import java.util.*;

public class SoferRepository {
    private final GenericRepository generic = GenericRepository.getInstance();

    public void insert(Sofer s) {
        String sql = "INSERT INTO soferi (nume, vehicul) VALUES (?, ?)";
        generic.executeUpdate(sql, s.getNume(), s.getVehicul());
    }

    public List<Sofer> findAll() {
        List<Sofer> lista = new ArrayList<>();
        String sql = "SELECT * FROM soferi";
        try (Connection conn = DatabaseConfiguration.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Sofer(rs.getInt("id"), rs.getString("nume"), rs.getString("vehicul")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Sofer findByName(String nume) {
        String sql = "SELECT * FROM soferi WHERE nume = ?";
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nume);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return new Sofer(rs.getInt("id"), rs.getString("nume"), rs.getString("vehicul"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Sofer findById(int id) {
        String sql = "SELECT * FROM soferi WHERE id = ?";
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return new Sofer(rs.getInt("id"), rs.getString("nume"), rs.getString("vehicul"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void update(int id, String nume, String vehicul) {
        String sql = "UPDATE soferi SET nume = ?, vehicul = ? WHERE id = ?";
        generic.executeUpdate(sql, nume, vehicul, id);
    }

    public void delete(int id) {
        String sql = "DELETE FROM soferi WHERE id = ?";
        generic.executeUpdate(sql, id);
    }
}