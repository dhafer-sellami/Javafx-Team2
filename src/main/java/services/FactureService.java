package services;

import entities.Facture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureService {

    private Connection connection;

    public FactureService() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/stvv", "root", ""
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterFacture(Facture facture) {
        String sql = "INSERT INTO facture (prix, idrdv) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, facture.getPrix());
            stmt.setInt(2, facture.getIdRdv());
            stmt.executeUpdate();
            System.out.println("Facture insérée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean existeFacturePourRendezVous(int idRdv) {
        String sql = "SELECT COUNT(*) FROM facture WHERE idrdv = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idRdv);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Facture getByRendezVousId(int idRdv) {
        String sql = "SELECT * FROM facture WHERE idrdv = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idRdv);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Facture(
                        rs.getInt("id"),
                        rs.getDouble("prix"),
                        rs.getInt("idrdv")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<Facture> getAll() {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM facture";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                factures.add(new Facture(
                        rs.getInt("id"),
                        rs.getDouble("prix"),
                        rs.getInt("idrdv")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

    public void supprimer(int id) {
        String sql = "DELETE FROM facture WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Facture supprimée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

