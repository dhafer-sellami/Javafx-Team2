package Controllers;

import Entity.Commentaire;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.MyDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentaireController {

    @FXML private Button btnAddCommentaire;
    @FXML private Button btnBack;
    @FXML private Button btnDeleteCommentaire;
    @FXML private Button btnUpdateCommentaire;

    @FXML private TableColumn<Commentaire, Integer> colIdCommentaire;
    @FXML private TableColumn<Commentaire, String> colContentCommentaire;
    @FXML private TableColumn<Commentaire, String> colAuthorCommentaire;
    @FXML private TableColumn<Commentaire, String> colDateCommentaire;
    @FXML private TableColumn<Commentaire, Integer> colArticleIdCommentaire;

    @FXML private TextArea contentComment;
    @FXML private TextField authorComment;
    @FXML private TextField articleIdComment;
    @FXML private DatePicker dateComment;

    @FXML private TableView<Commentaire> tableCommentaire;

    private ObservableList<Commentaire> commentaireList = FXCollections.observableArrayList();

    // Initialisation
    @FXML
    public void initialize() {
        loadCommentaires();

        // Set up table column values
        colIdCommentaire.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colContentCommentaire.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getContent()));
        colAuthorCommentaire.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getAuthor()));
        colArticleIdCommentaire.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getArticleId()).asObject());
        colDateCommentaire.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDate()));


        // Format pour parser les dates qui ont aussi l'heure
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Quand une ligne est sélectionnée, remplir les champs du formulaire
        tableCommentaire.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                contentComment.setText(newValue.getContent());
                authorComment.setText(newValue.getAuthor());
                articleIdComment.setText(String.valueOf(newValue.getArticleId()));

                try {
                    LocalDateTime dateTime = LocalDateTime.parse(newValue.getDate(), formatter);
                    dateComment.setValue(dateTime.toLocalDate());
                } catch (Exception e) {
                    e.printStackTrace(); // Affiche l'erreur si la date est mal formatée
                    dateComment.setValue(null);
                }
            }
        });

        // Lier la liste des commentaires au tableau
        tableCommentaire.setItems(commentaireList);
    }

    // Add Commentaire
    @FXML
    void addCommentaire(ActionEvent event) {
        String query = "INSERT INTO commentaire (content, author, article_id, date) VALUES (?, ?, ?, ?)";

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Check for empty fields
            if (contentComment.getText().isEmpty() || authorComment.getText().isEmpty() ||
                    articleIdComment.getText().isEmpty() || dateComment.getValue() == null) {
                return; // Do nothing if fields are empty
            }

            // Convert date
            Date date = Date.valueOf(dateComment.getValue());

            // Prepare query parameters
            stmt.setString(1, contentComment.getText());
            stmt.setString(2, authorComment.getText());
            stmt.setInt(3, Integer.parseInt(articleIdComment.getText()));
            stmt.setDate(4, date);
            stmt.executeUpdate();
            System.out.println("Commentaire ajouté !");
            clearFields();
            loadCommentaires();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Commentaire
    @FXML
    void deleteCommentaire(ActionEvent event) {
        Commentaire selected = tableCommentaire.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String query = "DELETE FROM commentaire WHERE id = ?";

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            System.out.println("Commentaire supprimé !");
            loadCommentaires();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Commentaire
    @FXML
    void updateCommentaire(ActionEvent event) {
        Commentaire selected = tableCommentaire.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String query = "UPDATE commentaire SET content = ?, author = ?, article_id = ?, date = ? WHERE id = ?";

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Check for empty fields
            if (contentComment.getText().isEmpty() || authorComment.getText().isEmpty() ||
                    articleIdComment.getText().isEmpty() || dateComment.getValue() == null) {
                return; // Do nothing if fields are empty
            }

            // Convert date
            Date date = Date.valueOf(dateComment.getValue());

            // Prepare query parameters
            stmt.setString(1, contentComment.getText());
            stmt.setString(2, authorComment.getText());
            stmt.setInt(3, Integer.parseInt(articleIdComment.getText()));
            stmt.setDate(4, date);
            stmt.setInt(5, selected.getId());

            stmt.executeUpdate();
            System.out.println("Commentaire mis à jour !");
            loadCommentaires();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load Commentaires into Table
    private void loadCommentaires() {
        commentaireList.clear();
        String query = "SELECT * FROM commentaire";  // Your query to fetch data from DB

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setContent(rs.getString("content"));
                commentaire.setAuthor(rs.getString("author"));
                commentaire.setArticleId(rs.getInt("article_id"));
                commentaire.setDate(rs.getString("date"));
                commentaireList.add(commentaire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Clear Fields
    private void clearFields() {
        contentComment.clear();
        authorComment.clear();
        articleIdComment.clear();
        dateComment.setValue(null);
    }

    // Back Button
    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Articles.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean hasDuplicateComment(String content, String author, int articleId) {
        String query = "SELECT COUNT(*) FROM commentaire WHERE content = ? AND author = ? AND article_id = ?";

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set query parameters
            stmt.setString(1, content);
            stmt.setString(2, author);
            stmt.setInt(3, articleId);

            // Execute the query and get the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // If count > 0, a duplicate comment exists
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
