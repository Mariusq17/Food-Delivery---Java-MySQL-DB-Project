package ro.delivery.repository;

import ro.delivery.config.DatabaseConfiguration;
import ro.delivery.model.Produs;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdusRepository {
    private final GenericRepository generic = GenericRepository.getInstance();

    // Inseram un produs legat de un restaurant anume
    public void insert(Produs p, int idRestaurant) {
        String sql = "INSERT INTO produse (nume, pret, id_restaurant) VALUES (?, ?, ?)";
        generic.executeUpdate(sql, p.getDenumire(), p.getPret(), idRestaurant);
    }

    public List<Produs> findByRestaurant(int idRestaurant) {
        List<Produs> lista = new ArrayList<>();
        String sql = "SELECT * FROM produse WHERE id_restaurant = ?";
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idRestaurant);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Produs(rs.getString("nume"), rs.getDouble("pret")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}