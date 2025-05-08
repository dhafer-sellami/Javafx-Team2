package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Facture;
import entities.RendezVous;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.FactureService;
import services.RendezVousService;

//import javax.mail.*;
//import javax.mail.internet.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class RendezVousTableController {

    @FXML private TableView<RendezVous> tableView;
    @FXML private TableColumn<RendezVous, String> emailColumn;
    @FXML private TableColumn<RendezVous, String> numColumn;
    @FXML private TableColumn<RendezVous, String> etatColumn;
    @FXML private TableColumn<RendezVous, LocalDate> dateColumn;
    @FXML private TableColumn<RendezVous, Void> actionColumn;
    @FXML private Pagination pagination;
    @FXML private TextField etatFilterField;

    private RendezVousService service;
    private ObservableList<RendezVous> allRendezVous;
    private ObservableList<RendezVous> filteredList;
    private final int rowsPerPage = 9;
    private final FactureService factureService = new FactureService();

    @FXML
    public void initialize() {
        service = new RendezVousService();

        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        numColumn.setCellValueFactory(cellData -> cellData.getValue().numProperty());
        etatColumn.setCellValueFactory(cellData -> cellData.getValue().etatProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        etatFilterField.textProperty().addListener((observable, oldValue, newValue) -> filterData());
        loadData();

        addActionButtons();
    }

    private void loadData() {
        allRendezVous = FXCollections.observableArrayList(service.getAll());
        filterData();
    }

    public void filterData() {
        String filter = etatFilterField.getText();
        List<RendezVous> filtered = allRendezVous.stream()
                .filter(rv -> filter == null || filter.isEmpty() || rv.getEtat().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());

        filteredList = FXCollections.observableArrayList(filtered);
        updatePagination();
    }

    private void updatePagination() {
        int itemCount = filteredList.size();
        int pageCount = (int) Math.ceil((double) itemCount / rowsPerPage);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, filteredList.size());
        ObservableList<RendezVous> pageData = FXCollections.observableArrayList(filteredList.subList(fromIndex, toIndex));
        tableView.setItems(pageData);
        tableView.refresh();
        return new VBox(tableView);
    }

    private void genererFacturePdf(RendezVous rv, Facture facture, String outputPath) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

//        Image logo = Image.getInstance(Objects.requireNonNull(getClass().getResource("/design/meditrack-icon.png")));
//
//        logo.scaleToFit(80, 80);
//        logo.setAlignment(Image.ALIGN_LEFT);
//
//        document.add(logo);


        Paragraph title = new Paragraph("Facture MediTrack\n\n", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);


        document.add(new Paragraph("Nom du patient : " + rv.getEtat()));
        document.add(new Paragraph("Email : " + rv.getEmail()));
        document.add(new Paragraph("Date du rendez-vous : " + rv.getDate()));
        document.add(new Paragraph("\n"));


        PdfPTable table = new PdfPTable(2);
        table.addCell("Désignation");
        table.addCell("Montant (TND)");
        table.addCell("Consultation");
        table.addCell(String.format("%.2f", facture.getPrix()));
        document.add(table);


        document.add(new Paragraph("\nMerci pour votre confiance.\nMediTrack © 2025"));

        document.close();
    }


    private void envoyerEmailAvecPdf(String toEmail, String pdfPath) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ESP8266ARDPROJ@gmail.com", "tafdciapwfuvknqt");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("ESP8266ARDPROJ@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Votre facture MediTrack");

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Bonjour,\n\nVeuillez trouver ci-joint votre facture.\n\nCordialement,\nL’équipe MediTrack");

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(new java.io.File(pdfPath));

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);

        Transport.send(message);
    }



    private void addActionButtons() {
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button modifierBtn = new Button("M");
            private final Button supprimerBtn = new Button("S");
            private final Button genererFactureBtn = new Button("G");
            private final Button envoyerBtn = new Button("P");
            private final HBox box = new HBox(10, modifierBtn, supprimerBtn, genererFactureBtn, envoyerBtn);

            {
                modifierBtn.setOnAction(event -> {
                    RendezVous rendezVous = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RendezVousForm.fxml"));
                        Parent root = loader.load();

                        RendezVousFormController controller = loader.getController();
                        controller.setRendezVous(rendezVous);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Modifier Rendez-vous");
                        stage.showAndWait();

                        loadData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                supprimerBtn.setOnAction(event -> {
                    RendezVous rendezVous = getTableView().getItems().get(getIndex());
                    service.supprimer(rendezVous.getId());
                    loadData();
                });

                genererFactureBtn.setOnAction(event -> {
                    RendezVous rendezVous = getTableView().getItems().get(getIndex());

                    if (rendezVous.getDate().isAfter(LocalDate.now())) {
                        showAlert("Vous ne pouvez pas générer une facture pour un rendez-vous non encore effectué.");
                        return;
                    }

                    boolean factureExiste = factureService.existeFacturePourRendezVous(rendezVous.getId());

                    if (factureExiste) {
                        showAlert("Une facture existe déjà pour ce rendez-vous.");
                        return;
                    }

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouter-facture.fxml"));
                        Parent root = loader.load();

                        FactureFormController controller = loader.getController();
                        controller.setRendezVousId(rendezVous.getId());
                        controller.setFactureService(factureService);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Générer Facture");
                        stage.showAndWait();

                        loadData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                envoyerBtn.setOnAction(event -> {
                    RendezVous rendezVous = getTableView().getItems().get(getIndex());
                    Facture facture = factureService.getByRendezVousId(rendezVous.getId());

                    if (facture == null) {
                        showAlert("Aucune facture trouvée pour ce rendez-vous.");
                        return;
                    }

                    String cheminPdf = "facture_" + facture.getId() + ".pdf";

                    try {
                        genererFacturePdf(rendezVous, facture, cheminPdf);
                        envoyerEmailAvecPdf(rendezVous.getEmail(), cheminPdf);
                        showAlert("Facture envoyée avec succès à " + rendezVous.getEmail());
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert("Échec de l'envoi : " + e.getMessage());
                    }
                });

                // Style
                modifierBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-background-radius: 10;");
                supprimerBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-background-radius: 10;");
                genererFactureBtn.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-background-radius: 10;");
                envoyerBtn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 10;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }
    @FXML
    private void handleOpenForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RendezVousForme.fxml"));
            Parent root = loader.load();
            BorderPane mainPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenTable(ActionEvent event) {
        // Charger Table.fxml (rester sur place ou recharger)
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }
}
