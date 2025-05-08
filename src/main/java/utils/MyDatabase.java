package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String url = "jdbc:mysql://localhost:3306/3a62";
    private final String user = "root";
    private final String password = "";
    private Connection cnx;
    private static MyDatabase instance;

    private MyDatabase() {
        try {
            cnx = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion établie");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if (instance == null)
            instance = new MyDatabase();
        return instance;
    }

    public Connection getCnx() {

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = DriverManager.getConnection(url, user, password);
                System.out.println("Connexion rétablie");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la rétablissement de la connexion: " + e.getMessage());
            cnx = null;
        }
        return cnx;
    }
}

//
//    // Récupérer la connexion
//    public Connection getCon() {
//        try {
//            if (connection == null || connection.isClosed()) {
//                connection = DriverManager.getConnection(URL, USER, PASS); // Rouvrir la connexion si elle est fermée
//            }
//        } catch (SQLException e) {
//            System.err.println("Failed to reconnect: " + e.getMessage());
//        }
//        return connection;
//    }
//
//    // Méthode pour fermer la connexion
//    public void closeConnection() {
//        try {
//            if (connection != null && !connection.isClosed()) {
//                connection.close();
//                System.out.println("Connection closed");
//            }
//        } catch (SQLException e) {
//            System.err.println("Failed to close connection: " + e.getMessage());
//        }
//>>>>>>> origin/cheridy-harb
//    }
//}
