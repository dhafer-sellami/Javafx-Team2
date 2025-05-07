package Controllers;

import Entity.Article;
import com.itextpdf.layout.element.Paragraph;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.MyDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Desktop;


public class ArticleController {

    @FXML private Button btnAddArticle;
    @FXML private Button btnBack;
    @FXML private Button btnDeleteArticle;
    @FXML private Button btnUpdateArticle;

    @FXML private TableColumn<Article, Integer> colIdArticle;
    @FXML private TableColumn<Article, String> colNomArticle;
    @FXML private TableColumn<Article, String> colTypeArticle;
    @FXML private TableColumn<Article, String> colPhoneArticle;
    @FXML private TableColumn<Article, String> colDescriptionArticle;
    @FXML private TableColumn<Article, LocalDate> colDateAjoute;  // Corrected to LocalDate

    @FXML private TextField nomArticle;
    @FXML private TextField typeArticle;
    @FXML private TextField phoneArticle;
    @FXML private TextField descriptionArticle;
    @FXML private DatePicker dateAjouteArticle;

    @FXML private TableView<Article> tableArticle;
    @FXML
    private TextField searchField;  // Reference to the search field in FXML
    @FXML
    private TableColumn<Article, Void> colDownloadPDF;
    private ObservableList<Article> filteredArticles = FXCollections.observableArrayList();  // To hold filtered articles

    private ObservableList<Article> articleList = FXCollections.observableArrayList();

    // Initialisation
    @FXML
    public void initialize() {
        loadArticles();

        // Set up table column values
        colIdArticle.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colNomArticle.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNom()));
        colTypeArticle.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType()));
        colPhoneArticle.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPhonenumber()));
        colDescriptionArticle.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDescription()));
        colDateAjoute.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getDateAjoute().toLocalDate()));

        // Add the "Download PDF" button column
        TableColumn<Article, Void> colDownloadPDF = new TableColumn<>("Download PDF");

        colDownloadPDF.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Download PDF");

            {
                btn.setOnAction((ActionEvent event) -> {
                    Article article = getTableView().getItems().get(getIndex());
                    generateAndDownloadPDF(article); // Call your PDF generation function
                });
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        // Add all columns including the new one
        tableArticle.getColumns().add(colDownloadPDF);

        // Set table data
        tableArticle.setItems(articleList);

        // Populate form fields when a row is selected
        tableArticle.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nomArticle.setText(newValue.getNom());
                typeArticle.setText(newValue.getType());
                phoneArticle.setText(newValue.getPhonenumber());
                descriptionArticle.setText(newValue.getDescription());
                dateAjouteArticle.setValue(newValue.getDateAjoute().toLocalDate());
            }
        });

        filteredArticles.setAll(articleList);
    }




    // Add Article
    @FXML
    void addArticle(ActionEvent event) {
        String query = "INSERT INTO article (nom, type, phonenumber, description, date_ajoute) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Check for empty fields
            if (nomArticle.getText().isEmpty() || typeArticle.getText().isEmpty() ||
                    phoneArticle.getText().isEmpty() || descriptionArticle.getText().isEmpty() ||
                    dateAjouteArticle.getValue() == null) {
                return; // Do nothing if fields are empty
            }

            // Convert date
            Date dateAjoute = Date.valueOf(dateAjouteArticle.getValue());

            // Prepare query parameters
            stmt.setString(1, nomArticle.getText());
            stmt.setString(2, typeArticle.getText());
            stmt.setString(3, phoneArticle.getText());
            stmt.setString(4, descriptionArticle.getText());
            stmt.setDate(5, dateAjoute);

            stmt.executeUpdate();
            System.out.println("Article ajouté !");
            clearFields();
            loadArticles();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Article
    @FXML
    void deleteArticle(ActionEvent event) {
        Article selected = tableArticle.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String query = "DELETE FROM article WHERE id = ?";

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            System.out.println("Article supprimé !");
            loadArticles();
            SmsService.sendSms("+21656576911", "String message = \"Le support a supprimé votre article pour des raisons de droits d’auteur. Contactez-les pour en savoir plus.\";\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Article
    @FXML
    void updateArticle(ActionEvent event) {
        Article selected = tableArticle.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String query = "UPDATE article SET nom = ?, type = ?, phonenumber = ?, description = ?, date_ajoute = ? WHERE id = ?";

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Check for empty fields
            if (nomArticle.getText().isEmpty() || typeArticle.getText().isEmpty() ||
                    phoneArticle.getText().isEmpty() || descriptionArticle.getText().isEmpty() ||
                    dateAjouteArticle.getValue() == null) {
                return; // Do nothing if fields are empty
            }

            // Convert date
            Date dateAjoute = Date.valueOf(dateAjouteArticle.getValue());

            // Prepare query parameters
            stmt.setString(1, nomArticle.getText());
            stmt.setString(2, typeArticle.getText());
            stmt.setString(3, phoneArticle.getText());
            stmt.setString(4, descriptionArticle.getText());
            stmt.setDate(5, dateAjoute);
            stmt.setInt(6, selected.getId());

            stmt.executeUpdate();
            System.out.println("Article mis à jour !");
            loadArticles();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load Articles into Table
    private void loadArticles() {
        articleList.clear();
        String query = "SELECT * FROM article";  // Your query to fetch data from DB

        try (Connection conn = MyDatabase.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Article article = new Article();
                article.setId(rs.getInt("id"));
                article.setNom(rs.getString("nom"));
                article.setType(rs.getString("type"));
                article.setPhonenumber(rs.getString("phonenumber"));
                article.setDescription(rs.getString("description"));
                article.setDateAjoute(rs.getDate("date_ajoute"));
                articleList.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Clear Fields
    private void clearFields() {
        nomArticle.clear();
        typeArticle.clear();
        phoneArticle.clear();
        descriptionArticle.clear();
        dateAjouteArticle.setValue(null);
    }

    // Back Button
    @FXML
    void Commentaires(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Commentaire.fxml"));
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

    @FXML
    public void filterArticles(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();  // Convert to lower case for case-insensitive search

        if (searchText.isEmpty()) {
            // Show all articles if the search field is empty
            filteredArticles.setAll(articleList);
        } else {
            // Filter articles based on the search text in the name or description
            List<Article> filteredList = articleList.stream()
                    .filter(article -> article.getNom().toLowerCase().contains(searchText) ||
                            article.getDescription().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());

            // Set filtered list to filteredArticles
            filteredArticles.setAll(filteredList);
        }

        // Update the table to show filtered articles
        tableArticle.setItems(filteredArticles);  // Ensure table is updated with filtered articles
    }
    private void generateAndDownloadPDF(Article article) {
        String fileName = "article_" + article.getId() + ".pdf";

        try {
            // Create PDF writer
            PdfWriter writer = new PdfWriter(fileName);

            // Create PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Create document layout
            Document document = new Document(pdf);

            // Add content
            document.add(new Paragraph("Article Information"));
            document.add(new Paragraph("ID: " + article.getId()));
            document.add(new Paragraph("Name: " + article.getNom()));
            document.add(new Paragraph("Type: " + article.getType()));
            document.add(new Paragraph("Phone: " + article.getPhonenumber()));
            document.add(new Paragraph("Description: " + article.getDescription()));
            document.add(new Paragraph("Date Added: " + article.getDateAjoute().toLocalDate()));

            document.close();

            // Open the file automatically
            openFile(fileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.out.println("Desktop not supported.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
