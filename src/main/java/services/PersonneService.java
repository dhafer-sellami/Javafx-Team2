package services;

import entities.Personne;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonneService {

    private Connection connection;

    public PersonneService() {
        try {
            connection = DriverManager.getConnection(
                    "", "root", ""
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouter(Personne personne) throws SQLException {
        String sql = "INSERT INTO personne (nom, prenom, age) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, personne.getNom());
            ps.setString(2, personne.getPrenom());
            ps.setInt(3, personne.getAge());
            ps.executeUpdate();
        }
    }

    public void modifier(Personne personne) throws SQLException {
        String sql = "UPDATE personne SET nom = ?, prenom = ?, age = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, personne.getNom());
            ps.setString(2, personne.getPrenom());
            ps.setInt(3, personne.getAge());
            ps.setInt(4, personne.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(Personne personne) throws SQLException {
        String sql = "DELETE FROM personne WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, personne.getId());
            ps.executeUpdate();
        }
    }

    public List<Personne> recuperer() throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        String sql = "SELECT * FROM personne";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Personne p = new Personne(
                        rs.getInt("id"),
                        rs.getInt("age"),
                        rs.getString("nom"),
                        rs.getString("prenom")
                );
                personnes.add(p);
            }
        }
        return personnes;
    }
}

