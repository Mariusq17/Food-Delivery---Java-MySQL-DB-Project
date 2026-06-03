package ro.delivery.repository;
import ro.delivery.config.DatabaseConfiguration;
import ro.delivery.model.*;
import java.sql.*;
import java.util.*;

public class ClientRepository {
    private final GenericRepository generic = GenericRepository.getInstance();

    public void insert(Client c, String oras, String strada) {
        String sql = "INSERT INTO clienti (nume, oras, strada) VALUES (?, ?, ?)";
        generic.executeUpdate(sql, c.getNume(), oras, strada);
    }

    public List<Client> findAll() {
        List<Client> lista = new ArrayList<>();
        String sql = "SELECT * FROM clienti";
        try (Connection conn = DatabaseConfiguration.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Adresa adr = new Adresa(rs.getString("oras"), rs.getString("strada"));
                lista.add(new Client(rs.getInt("id"), rs.getString("nume"), adr));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Client findByName(String nume) {
        String sql = "SELECT * FROM clienti WHERE nume = ?";
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nume);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Adresa adr = new Adresa(rs.getString("oras"), rs.getString("strada"));
                return new Client(rs.getInt("id"), rs.getString("nume"), adr);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Client findById(int id) {
        String sql = "SELECT * FROM clienti WHERE id = ?";
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Adresa adr = new Adresa(rs.getString("oras"), rs.getString("strada"));
                return new Client(rs.getInt("id"), rs.getString("nume"), adr);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void update(int id, String nume, String oras, String strada) {
        String sql = "UPDATE clienti SET nume = ?, oras = ?, strada = ? WHERE id = ?";
        generic.executeUpdate(sql, nume, oras, strada, id);
    }

    public void delete(int id) {
        String sql = "DELETE FROM clienti WHERE id = ?";
        generic.executeUpdate(sql, id);
    }
}