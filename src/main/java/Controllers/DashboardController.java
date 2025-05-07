package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.stream.Collectors;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.MyDatabase;

public class DashboardController {

    @FXML
    private VBox addArticleContainer;
    @FXML
    private VBox addCommentContainer;

    // Article form fields
    @FXML
    private TextField nomField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker dateAjoutePicker;

    // Comment form fields
    @FXML
    private TextArea contentComment;
    @FXML
    private TextField authorComment;
    @FXML
    private TextField articleIdComment;
    @FXML
    private DatePicker dateComment;

    // Method to show the Add Article form when the button is clicked
    @FXML
    public void showAddArticleForm(ActionEvent event) {
        addArticleContainer.setVisible(true);
        addCommentContainer.setVisible(false); // Hide Add Comment form
    }

    // Method to show the Add Comment form when the link is clicked
    @FXML
    public void showAddCommentForm(ActionEvent event) {
        addArticleContainer.setVisible(false); // Hide Add Article form
        addCommentContainer.setVisible(true);  // Show Add Comment form
    }

    // Handle the form submission when the "Ajouter" button is clicked (Add Article)
    @FXML
    void handleAddArticle(ActionEvent event) {
        String query = "INSERT INTO article (nom, type, phonenumber, description, date_ajoute) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Check for empty fields
            if (nomField.getText().isEmpty() || typeField.getText().isEmpty() ||
                    phoneField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                    dateAjoutePicker.getValue() == null) {
                return; // Do nothing if fields are empty
            }

            // Convert date
            Date dateAjoute = Date.valueOf(dateAjoutePicker.getValue());

            // Prepare query parameters
            stmt.setString(1, nomField.getText());
            stmt.setString(2, typeField.getText());
            stmt.setString(3, phoneField.getText());
            stmt.setString(4, descriptionField.getText());
            stmt.setDate(5, dateAjoute);

            stmt.executeUpdate();
            System.out.println("Article ajouté !");

            clearFields();
            addArticleContainer.setVisible(false);

            loadArticles();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle the form submission when the "Ajouter" button is clicked (Add Comment)
    @FXML
    void addCommentaire(ActionEvent event) {
        String query = "INSERT INTO commentaire (content, author, article_id, date) VALUES (?, ?, ?, ?)";
        String content = contentComment.getText();
        String author = authorComment.getText();
        int articleId = Integer.parseInt(articleIdComment.getText());

        // Check for duplicate comment
        if (hasDuplicateComment(content, author, articleId)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Comment");
            alert.setContentText("Duuuuuude stop spamming the same comment ! :V.");
            alert.show();
            return; // Exit the method to prevent adding the duplicate comment
        }

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Check for empty fields
            if (contentComment.getText().isEmpty() || authorComment.getText().isEmpty() ||
                    articleIdComment.getText().isEmpty() || dateComment.getValue() == null) {
                return; // Do nothing if fields are empty
            }
            if (containsBadWords(content)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Contenu inapproprié");
                alert.setContentText("Le commentaire contient des mots interdits.");
                alert.show();
                return;
            }

            // Convert date
            Date date = Date.valueOf(dateComment.getValue());

            // Prepare query parameters
            stmt.setString(1, content);
            stmt.setString(2, author);
            stmt.setInt(3, articleId);
            stmt.setDate(4, date);

            stmt.executeUpdate();
            System.out.println("Commentaire ajouté !");

            clearFields();
            addCommentContainer.setVisible(false); // Hide Add Comment form

            loadCommentaires();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to clear the form fields
    private void clearFields() {
        nomField.clear();
        typeField.clear();
        phoneField.clear();
        descriptionField.clear();
        dateAjoutePicker.setValue(null);
        contentComment.clear();
        authorComment.clear();
        articleIdComment.clear();
        dateComment.setValue(null);
    }

    // Method to load all articles (for refreshing the list after adding one)
    private void loadArticles() {
        // Implement this method to refresh the list of articles after adding one
        // For example, you might want to load all articles and display them in a table.
    }

    // Method to load all comments (for refreshing the list after adding a comment)
    private void loadCommentaires() {
        // Implement this method to refresh the list of comments after adding one
        // For example, you might want to load all comments and display them in a table.
    }

    // Optional: Add a method to go back to the dashboard (hides Add Article and Add Comment forms)
    @FXML
    public void goBackToDashboard(ActionEvent event) {
        addArticleContainer.setVisible(false);  // Hide Add Article form
        addCommentContainer.setVisible(false);  // Hide Add Comment form
        // Add any other content to be displayed on the dashboard here
    }

    @FXML
    public void goToAdminPanel(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Articles.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Articles");

            // Ferme la fenêtre actuelle (dashboard)
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean containsBadWords(String content) {
        try {
            String apiKey = "068BzZ3dtpJpnlmkWcayqFQQriPhSjNQh0qseHCFYjHil5ae"; // Your actual key
            String url = "https://neutrinoapi.net/bad-word-filter";

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Prepare data to send
            String postData = "user-id=delpan&api-key=" + URLEncoder.encode(apiKey, "UTF-8")
                    + "&content=" + URLEncoder.encode(content, "UTF-8");

            // Send data
            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData.getBytes());
            }

            // Check response code (to ensure we get a successful response)
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Error: Response Code " + responseCode);
                return false;
            }

            // Read the response
            InputStream response = connection.getInputStream();
            String result = new BufferedReader(new InputStreamReader(response))
                    .lines().collect(Collectors.joining("\n"));

            // Check if "is-bad" is true in the response JSON
            return result.contains("\"is-bad\":true");

        } catch (Exception e) {
            e.printStackTrace();
            return false; // If there's an error, we assume no bad words (you can adjust this as per your needs)
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
