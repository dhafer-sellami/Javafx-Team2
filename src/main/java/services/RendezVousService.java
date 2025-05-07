package services;

import entities.RendezVous;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RendezVousService {

    private Connection connection;

    public RendezVousService() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/3a62", "root", ""
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void ajouter(RendezVous rdv) {
        LocalDate rdvDate = rdv.getDate();



        if (getCountByDate(rdvDate) >= 10) {
            System.out.println("Erreur : Il y a déjà 10 rendez-vous pour cette date.");
            return;
        }

        String sql = "INSERT INTO rendezvous (date, email, num, etat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(rdv.getDate()));
            stmt.setString(2, rdv.getEmail());
            stmt.setString(3, rdv.getNum());
            stmt.setString(4, rdv.getEtat());

            int rows = stmt.executeUpdate();
            System.out.println("Lignes insérées : " + rows);
            System.out.println("Rendez-vous inséré dans la base !");
        } catch (SQLException e) {
            System.out.println("Erreur d'insertion !");
            e.printStackTrace();
        }
    }


    public List<RendezVous> getAll() {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendezvous";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                RendezVous rdv = new RendezVous(
                        rs.getInt("id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("email"),
                        rs.getString("num"),
                        rs.getString("etat")
                );
                list.add(rdv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    public void modifier(RendezVous rdv) {
        String sql = "UPDATE rendezvous SET date = ?, email = ?, num = ?, etat = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(rdv.getDate()));
            stmt.setString(2, rdv.getEmail());
            stmt.setString(3, rdv.getNum());
            stmt.setString(4, rdv.getEtat());
            stmt.setInt(5, rdv.getId());
            int rows = stmt.executeUpdate();
            System.out.println("Lignes modifiées : " + rows);
            System.out.println("Rendez-vous modifié dans la base !");
        } catch (SQLException e) {
            System.out.println("Erreur de modification !");
            e.printStackTrace();
        }
    }


    public void supprimer(int id) {
        String sql = "DELETE FROM rendezvous WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println("Lignes supprimées : " + rows);
            System.out.println("Rendez-vous supprimé de la base !");
        } catch (SQLException e) {
            System.out.println("Erreur de suppression !");
            e.printStackTrace();
        }
    }


    public int getCountByDate(LocalDate date) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM rendezvous WHERE DATE(date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}

