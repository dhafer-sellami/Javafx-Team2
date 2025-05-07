package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private final String URL = "jdbc:mysql://localhost:3306/meditrackdb";
    private final String USER = "root";
    private final String PASS = "";

    private Connection connection;
    private static MyDatabase db;

    // Constructeur privé pour empêcher l'instanciation directe
    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    // Méthode statique pour récupérer l'instance unique
    public static MyDatabase getInstance() throws SQLException {
        if (db == null || db.connection == null || db.connection.isClosed()) {
            db = new MyDatabase(); // Crée une nouvelle instance si la connexion est fermée
        }
        return db;
    }

    // Récupérer la connexion
    public Connection getCon() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASS); // Rouvrir la connexion si elle est fermée
            }
        } catch (SQLException e) {
            System.err.println("Failed to reconnect: " + e.getMessage());
        }
        return connection;
    }

    // Méthode pour fermer la connexion
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }
}
