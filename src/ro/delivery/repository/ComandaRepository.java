package ro.delivery.repository;
import ro.delivery.config.DatabaseConfiguration;
import java.sql.*;
import java.util.*;

public class ComandaRepository {
    private final GenericRepository generic = GenericRepository.getInstance();

    public void create(int idClient, int idRest, double total) {
        String sql = "INSERT INTO comenzi (id_client, id_restaurant, total, status) VALUES (?, ?, ?, 'PENDING')";
        generic.executeUpdate(sql, idClient, idRest, total);
    }

    public void afiseazaComenziDisponibile() {
        String sql = "SELECT c.id, r.nume as rest, cl.nume as client, c.total FROM comenzi c " +
                "JOIN restaurante r ON c.id_restaurant = r.id JOIN clienti cl ON c.id_client = cl.id WHERE c.status = 'PENDING'";
        afiseazaTabel(sql, null);
    }

    public void afiseazaComenziSofer(int idSofer) {
        String sql = "SELECT c.id, r.nume as rest, cl.nume as client, c.total FROM comenzi c " +
                "JOIN restaurante r ON c.id_restaurant = r.id JOIN clienti cl ON c.id_client = cl.id " +
                "WHERE c.id_sofer = ? AND c.status = 'IN_PROGRESS'";
        afiseazaTabel(sql, idSofer);
    }

    public void afiseazaToateComenzile() {
        String sql = "SELECT c.id, cl.nume as client, r.nume as rest, c.total, c.status FROM comenzi c " +
                "JOIN restaurante r ON c.id_restaurant = r.id JOIN clienti cl ON c.id_client = cl.id ORDER BY c.id DESC";
        afiseazaTabel(sql, null);
    }

    private void afiseazaTabel(String sql, Integer param) {
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (param != null) pstmt.setInt(1, param);
            ResultSet rs = pstmt.executeQuery();
            boolean gasit = false;
            while (rs.next()) {
                gasit = true;
                String extra = "";
                try { extra = " | Status: " + rs.getString("status"); } catch (Exception e) {}
                System.out.println("ID #" + rs.getInt("id") + " | Client: " + rs.getString("client") + " | Rest: " + rs.getString("rest") + " | Total: " + rs.getDouble("total") + " RON" + extra);
            }
            if (!gasit) System.out.println("(!) Nu s-au gasit comenzi.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void preiaComanda(int idC, int idS) {
        String sql = "UPDATE comenzi SET id_sofer = ?, status = 'IN_PROGRESS' WHERE id = ? AND status = 'PENDING'";
        generic.executeUpdate(sql, idS, idC);
    }

    public void finalizeazaComanda(int idC, int idS) {
        String sql = "UPDATE comenzi SET status = 'DELIVERED' WHERE id = ? AND id_sofer = ? AND status = 'IN_PROGRESS'";
        generic.executeUpdate(sql, idC, idS);
    }

    public void afiseazaIstoricClient(int idC) {
        String sql = "SELECT c.id, r.nume as rest, c.total, c.status FROM comenzi c " +
                "JOIN restaurante r ON c.id_restaurant = r.id WHERE c.id_client = ? ORDER BY c.id DESC";
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idC);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\n--- ISTORICUL MEU ---");
            while (rs.next()) {
                String s = rs.getString("status");
                String prefix = s.equals("DELIVERED") ? "[✓]" : (s.equals("PENDING") ? "[WAIT]" : "[WAY]");
                System.out.println(prefix + " ID #" + rs.getInt("id") + " | " + rs.getString("rest") + " | " + rs.getDouble("total") + " RON");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}