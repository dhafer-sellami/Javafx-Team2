package com.example.meditrack;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;

public class MediTrackApp extends Application {
    private TableView<Emplacement> table;
    private ObservableList<Emplacement> emplacementData;

    private TableView<Categorie> categorieTable;
    private ObservableList<Categorie> categorieData;
    private Object emplacementTable;
    private TableView<Assurance> assuranceTable;
    private ObservableList<Assurance> assuranceData;

    private TableView<Reclamation> reclamationTable;
    private ObservableList<Reclamation> reclamationData;


    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MediTrack");

        // Main layout
        VBox mainLayout = new VBox();
        mainLayout.setStyle("-fx-background-color: white;");

        // Navigation Bar
        HBox navBar = createNavigationBar();

        // TabPane for switching between Emplacement and Categorie
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: white;");

        // Emplacement Tab
        Tab emplacementTab = new Tab("Emplacements");
        VBox emplacementContent = createEmplacementContent();
        emplacementTab.setContent(emplacementContent);
        emplacementTab.setClosable(false);

        // Categorie Tab
        Tab categorieTab = new Tab("Categories");
        VBox categorieContent = createCategorieContent();
        categorieTab.setContent(categorieContent);
        categorieTab.setClosable(false);

        Tab assuranceTab = new Tab("Assurance");
        VBox assuranceContent = createAssuranceContent();
        assuranceTab.setContent(assuranceContent);
        assuranceTab.setClosable(false);

        Tab reclamationTab = new Tab("Reclamation");
        VBox reclamationContent = createReclamationContent();
        reclamationTab.setContent(reclamationContent);
        reclamationTab.setClosable(false);


        // Add tabs to TabPane
        tabPane.getTabs().addAll(emplacementTab, categorieTab,assuranceTab,reclamationTab);

        // Footer
        Label footerLabel = new Label("Created By Team MediTrack | All Right Reserved");
        footerLabel.setStyle("-fx-text-fill: #7f8c8d;");

        // Assemble main layout
        mainLayout.getChildren().addAll(navBar, tabPane);

        // Add footer to bottom
        VBox footerBox = new VBox(footerLabel);
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(20, 0, 10, 0));
        mainLayout.getChildren().add(footerBox);

        // Scene and styling
        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Reclamation> createReclamationTable() {
        TableColumn<Reclamation, Number> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        idColumn.setPrefWidth(50);

        TableColumn<Reclamation, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        dateColumn.setPrefWidth(150);

        TableColumn<Reclamation, String> descColumn = new TableColumn<>("Description");
        descColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        descColumn.setPrefWidth(400);

        // Sample data
        reclamationData = FXCollections.observableArrayList(
                new Reclamation(1, "2025-05-01", "Late delivery"),
                new Reclamation(2, "2025-05-10", "Product damaged")
        );

        TableView<Reclamation> table = new TableView<>(reclamationData);
        table.getColumns().addAll(idColumn, dateColumn, descColumn);

        // Actions column
        TableColumn<Reclamation, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(180);
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final HBox box = new HBox(10);
            private final Button showBtn = new Button("Show");
            private final Button editBtn = new Button("Edit");

            {
                showBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                box.setAlignment(Pos.CENTER);
                box.getChildren().addAll(showBtn, editBtn);

                showBtn.setOnAction(e -> {
                    Reclamation r = getTableView().getItems().get(getIndex());
                    showReclamationDetails(r);
                });

                editBtn.setOnAction(e -> {
                    Reclamation r = getTableView().getItems().get(getIndex());
                    editReclamation(r);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        table.getColumns().add(actionsColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }
    private void showReclamationDetails(Reclamation r) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reclamation Details");
        alert.setHeaderText("Reclamation #" + r.getId());
        alert.setContentText("Date: " + r.getDate() + "\nDescription: " + r.getDescription());
        alert.showAndWait();
    }
    private void editReclamation(Reclamation r) {
        Stage dialog = new Stage();
        dialog.setTitle("Edit Reclamation");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField dateField = new TextField(r.getDate());
        TextField descField = new TextField(r.getDescription());

        grid.addRow(0, new Label("Date (YYYY-MM-DD):"), dateField);
        grid.addRow(1, new Label("Description:"), descField);

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> {
            r.setDate(dateField.getText());
            r.setDescription(descField.getText());
            reclamationTable.refresh();
            dialog.close();
        });

        VBox vbox = new VBox(15, grid, saveBtn);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        dialog.setScene(new Scene(vbox, 400, 250));
        dialog.show();
    }
    private void showCreateNewReclamationDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Create Reclamation");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField dateField = new TextField();
        TextField descField = new TextField();

        grid.addRow(0, new Label("Date (YYYY-MM-DD):"), dateField);
        grid.addRow(1, new Label("Description:"), descField);

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> {
            int newId = reclamationData.size() + 1;
            Reclamation newRec = new Reclamation(newId, dateField.getText(), descField.getText());
            reclamationData.add(newRec);
            dialog.close();
        });

        VBox vbox = new VBox(15, grid, saveBtn);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        dialog.setScene(new Scene(vbox, 400, 250));
        dialog.show();
    }
    private VBox createReclamationContent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        Label title = new Label("Reclamations");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");


        reclamationTable = createReclamationTable();

        Button createBtn = new Button("Create New Reclamation");
        createBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        createBtn.setOnAction(e -> showCreateNewReclamationDialog());

        root.getChildren().addAll(title, reclamationTable, createBtn);
        return root;
    }



    private TableView<Assurance> createAssuranceTable() {
        // Create table columns
        TableColumn<Assurance, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        idColumn.setPrefWidth(50);

        TableColumn<Assurance, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        nomColumn.setPrefWidth(200);

        TableColumn<Assurance, String> libelleColumn = new TableColumn<>("Libelle");
        libelleColumn.setCellValueFactory(cellData -> cellData.getValue().libelleProperty());
        libelleColumn.setPrefWidth(400);

        // Initialize table with data
        assuranceData = FXCollections.observableArrayList(
                new Assurance(1, "GAT", "Assurance Vie"),
                new Assurance(2, "Ami", "Assurance Vie"),
                new Assurance(3, "Maghrebia", "Assurance Vie"),
                new Assurance(4, "LLOYD", "Assurance Vie Auto"),
                new Assurance(5, "STAR", "Assurance Vie Auto")
        );

        TableView<Assurance> table = new TableView<>(assuranceData);
        table.getColumns().addAll(idColumn, nomColumn, libelleColumn);

        // Actions Column
        TableColumn<Assurance, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button showBtn = new Button("Show");
            private final Button editBtn = new Button("Edit");

            {
                String buttonStyle =
                        "-fx-background-color: #2ecc71;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 3px;" +
                                "-fx-padding: 5px 10px;";

                showBtn.setStyle(buttonStyle);
                editBtn.setStyle(buttonStyle);

                showBtn.setOnAction(event -> {
                    Assurance assurance = getTableView().getItems().get(getIndex());
                    showAssuranceDetails(assurance);
                });

                editBtn.setOnAction(event -> {
                    Assurance assurance = getTableView().getItems().get(getIndex());
                    editAssurance(assurance);
                });

                buttons.getChildren().addAll(showBtn, editBtn);
                buttons.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
        table.getColumns().add(actionsColumn);

        // Table styling
        table.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-background-color: white;" +
                        "-fx-table-cell-border-color: #ecf0f1;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }
    private void showAssuranceDetails(Assurance assurance) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Assurance Details");
        alert.setHeaderText("Details for Assurance #" + assurance.getId());
        alert.setContentText(
                "Nom: " + assurance.getNom() + "\n" +
                        "Libelle: " + assurance.getLibelle()
        );
        alert.showAndWait();
    }

    private void editAssurance(Assurance assurance) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Assurance");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Form fields pre-filled with existing data
        TextField nomField = new TextField(assurance.getNom());
        TextField libelleField = new TextField(assurance.getLibelle());

        // Labels with styling
        Label nomLabel = new Label("Nom:");
        Label libelleLabel = new Label("Libelle:");

        grid.addRow(0, nomLabel, nomField);
        grid.addRow(1, libelleLabel, libelleField);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        saveButton.setOnAction(e -> {
            assurance.setNom(nomField.getText());
            assurance.setLibelle(libelleField.getText());
            assuranceTable.refresh();
            dialogStage.close();
        });

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(grid, saveButton);
        layout.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(layout, 400, 250);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }private VBox createAssuranceContent() {
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label("Assurance Index");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");
        titleLabel.setPadding(new Insets(0, 0, 10, 0));

        // Create table
        assuranceTable = createAssuranceTable();

        // Create New Button
        Button createNewButton = new Button("Create New Assurance");
        createNewButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        createNewButton.setOnAction(e -> showCreateNewAssuranceDialog());

        // Assemble content
        contentArea.getChildren().addAll(titleLabel, assuranceTable, createNewButton);

        return contentArea;
    }
    private void showCreateNewAssuranceDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Create New Assurance");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Form fields
        TextField nomField = new TextField();
        TextField libelleField = new TextField();

        // Labels with styling
        Label nomLabel = new Label("Nom:");
        Label libelleLabel = new Label("Libelle:");

        grid.addRow(0, nomLabel, nomField);
        grid.addRow(1, libelleLabel, libelleField);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        saveButton.setOnAction(e -> {
            int newId = assuranceData.size() + 1;
            Assurance newAssurance = new Assurance(
                    newId,
                    nomField.getText(),
                    libelleField.getText()
            );
            assuranceData.add(newAssurance);
            dialogStage.close();
        });

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(grid, saveButton);
        layout.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(layout, 400, 250);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
    private void showCreateNewEmplacementDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Create New Emplacement");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Form fields
        TextField addressField = new TextField();
        TextField cityField = new TextField();
        TextField postalCodeField = new TextField();

        // Labels with styling
        Label addressLabel = new Label("Address:");
        Label cityLabel = new Label("City:");
        Label postalCodeLabel = new Label("Postal Code:");

        grid.addRow(0, addressLabel, addressField);
        grid.addRow(1, cityLabel, cityField);
        grid.addRow(2, postalCodeLabel, postalCodeField);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        saveButton.setOnAction(e -> {
            int newId = emplacementData.size() + 1;
            Emplacement newEmplacement = new Emplacement(
                    newId,
                    addressField.getText(),
                    cityField.getText(),
                    postalCodeField.getText()
            );
            emplacementData.add(newEmplacement);
            dialogStage.close();
        });

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(grid, saveButton);
        layout.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(layout, 400, 250);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private VBox createEmplacementContent() {
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label("Emplacement Index");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");
        titleLabel.setPadding(new Insets(0, 0, 10, 0));

        // Create table
        TableView<Emplacement> emplacementTable = createEmplacementTable();

        // Create New Button
        Button createNewButton = new Button("Create New Emplacement");
        createNewButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        createNewButton.setOnAction(e -> showCreateNewEmplacementDialog());

        // Assemble content
        contentArea.getChildren().addAll(titleLabel, emplacementTable, createNewButton);

        return contentArea;
    }
    private void showCreateNewCategorieDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Create New Category");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Form fields
        TextField nameField = new TextField();
        TextField descriptionField = new TextField();

        // Labels with styling
        Label nameLabel = new Label("Name:");
        Label descriptionLabel = new Label("Description:");

        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, descriptionLabel, descriptionField);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        saveButton.setOnAction(e -> {
            int newId = categorieData.size() + 1;
            Categorie newCategorie = new Categorie(
                    newId,
                    nameField.getText(),
                    descriptionField.getText()
            );
            categorieData.add(newCategorie);
            dialogStage.close();
        });

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(grid, saveButton);
        layout.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(layout, 400, 250);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private VBox createCategorieContent() {
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label("Category Index");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");
        titleLabel.setPadding(new Insets(0, 0, 10, 0));

        // Create table
        categorieTable = createCategorieTable();

        // Create New Button
        Button createNewButton = new Button("Create New Category");
        createNewButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        createNewButton.setOnAction(e -> showCreateNewCategorieDialog());

        // Assemble content
        contentArea.getChildren().addAll(titleLabel, categorieTable, createNewButton);

        return contentArea;
    }
    private void showCategorieDetails(Categorie categorie) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Category Details");
        alert.setHeaderText("Details for Category #" + categorie.getId());
        alert.setContentText(
                "Name: " + categorie.getName() + "\n" +
                        "Description: " + categorie.getDescription()
        );
        alert.showAndWait();
    }

    private void editCategorie(Categorie categorie) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Category");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Form fields pre-filled with existing data
        TextField nameField = new TextField(categorie.getName());
        TextField descriptionField = new TextField(categorie.getDescription());

        // Labels with styling
        Label nameLabel = new Label("Name:");
        Label descriptionLabel = new Label("Description:");

        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, descriptionLabel, descriptionField);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        saveButton.setOnAction(e -> {
            categorie.setName(nameField.getText());
            categorie.setDescription(descriptionField.getText());
            categorieTable.refresh();
            dialogStage.close();
        });

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(grid, saveButton);
        layout.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(layout, 400, 250);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
    private TableView<Categorie> createCategorieTable() {
        // Create table columns
        TableColumn<Categorie, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        idColumn.setPrefWidth(50);

        TableColumn<Categorie, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setPrefWidth(200);

        TableColumn<Categorie, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        descriptionColumn.setPrefWidth(400);

        // Initialize table with data
        categorieData = FXCollections.observableArrayList(
                new Categorie(1, "Medication", "Storage for various types of medications"),
                new Categorie(2, "Medical Equipment", "Storage for medical devices and tools"),
                new Categorie(3, "Medical Supplies", "Disposable medical supplies and consumables")
        );

        TableView<Categorie> table = new TableView<>(categorieData);
        table.getColumns().addAll(idColumn, nameColumn, descriptionColumn);

        // Actions Column
        TableColumn<Categorie, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button showBtn = new Button("Show");
            private final Button editBtn = new Button("Edit");

            {
                // Style for buttons
                String buttonStyle =
                        "-fx-background-color: #2ecc71;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 3px;" +
                                "-fx-padding: 5px 10px;";

                showBtn.setStyle(buttonStyle);
                editBtn.setStyle(buttonStyle);

                showBtn.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    showCategorieDetails(categorie);
                });

                editBtn.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    editCategorie(categorie);
                });

                buttons.getChildren().addAll(showBtn, editBtn);
                buttons.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
        table.getColumns().add(actionsColumn);

        // Table styling
        table.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-background-color: white;" +
                        "-fx-table-cell-border-color: #ecf0f1;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private HBox createNavigationBar() {
        HBox navBar = new HBox(20);
        navBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-padding: 15px 20px;" +
                        "-fx-border-color: #ecf0f1;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        navBar.setAlignment(Pos.CENTER_LEFT);

        // Logo and Title
        HBox logoTitleBox = new HBox(10);
        logoTitleBox.setAlignment(Pos.CENTER_LEFT);

        // Heart Logo (using a Label with custom styling to mimic a heart icon)
        Label heartLogo = new Label("♥");
        heartLogo.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-text-fill: #2ecc71;" +
                        "-fx-font-weight: bold;"
        );

        // Logo Title
        Label logoLabel = new Label("MediTrack");
        logoLabel.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2ecc71;"
        );

        logoTitleBox.getChildren().addAll(heartLogo, logoLabel);

        // Spacer to push navigation items to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Navigation Items
        String[] navItemNames = {"Home", "About", "Doctors", "Book An Appointment", "Sign In", "Sign Up"};
        HBox navItemsBox = new HBox(15);
        navItemsBox.setAlignment(Pos.CENTER_RIGHT);

        for (String itemName : navItemNames) {
            Label navLabel = new Label(itemName);
            navLabel.setStyle(
                    "-fx-text-fill: #2c3e50;" +
                            "-fx-font-size: 14px;" +
                            "-fx-cursor: hand;"
            );

            // Highlight effect for Sign Up
            if (itemName.equals("Sign Up")) {
                navLabel.setStyle(
                        "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-background-color: #2ecc71;" +
                                "-fx-padding: 8px 15px;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-cursor: hand;"
                );
            }

            navLabel.setOnMouseEntered(e -> {
                if (!itemName.equals("Sign Up")) {
                    navLabel.setStyle(
                            "-fx-text-fill: #2ecc71;" +
                                    "-fx-font-size: 14px;" +
                                    "-fx-cursor: hand;"
                    );
                }
            });

            navLabel.setOnMouseExited(e -> {
                if (!itemName.equals("Sign Up")) {
                    navLabel.setStyle(
                            "-fx-text-fill: #2c3e50;" +
                                    "-fx-font-size: 14px;" +
                                    "-fx-cursor: hand;"
                    );
                }
            });

            navItemsBox.getChildren().add(navLabel);
        }

        navBar.getChildren().addAll(logoTitleBox, spacer, navItemsBox);
        return navBar;
    }
    private TableView<Emplacement> createEmplacementTable() {
        // Create table columns
        TableColumn<Emplacement, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        idColumn.setPrefWidth(50);

        TableColumn<Emplacement, String> addressColumn = new TableColumn<>("Adresse");
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        addressColumn.setPrefWidth(300);

        TableColumn<Emplacement, String> cityColumn = new TableColumn<>("Ville");
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        cityColumn.setPrefWidth(150);

        TableColumn<Emplacement, String> postalCodeColumn = new TableColumn<>("Code_Postal");
        postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
        postalCodeColumn.setPrefWidth(100);

        // Initialize table with data
        emplacementData = FXCollections.observableArrayList(
                new Emplacement(1, "Rue Othmane Bahri Ain Zaghouan", "Tunis", "2046"),
                new Emplacement(2, "Rue De Fathi Zouiri", "Ariana", "2057"),
                new Emplacement(3, "Rue Radhia Haddad", "Tunis", "1001"),
                new Emplacement(4, "Rue De Russie", "Ben Arous", "2000"),
                new Emplacement(5, "Rue De Municipalité", "Manouba", "2160")
        );

        TableView<Emplacement> table = new TableView<>(emplacementData);
        table.getColumns().addAll(idColumn, addressColumn, cityColumn, postalCodeColumn);

        // Actions Column
        TableColumn<Emplacement, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button showBtn = new Button("Show");
            private final Button editBtn = new Button("Edit");

            {
                // Style for buttons
                String buttonStyle =
                        "-fx-background-color: #2ecc71;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 3px;" +
                                "-fx-padding: 5px 10px;";

                showBtn.setStyle(buttonStyle);
                editBtn.setStyle(buttonStyle);

                showBtn.setOnAction(event -> {
                    Emplacement emplacement = getTableView().getItems().get(getIndex());
                    showEmplacementDetails(emplacement);
                });

                editBtn.setOnAction(event -> {
                    Emplacement emplacement = getTableView().getItems().get(getIndex());
                    editEmplacement(emplacement);
                });

                buttons.getChildren().addAll(showBtn, editBtn);
                buttons.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
        table.getColumns().add(actionsColumn);

        // Table styling
        table.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-background-color: white;" +
                        "-fx-table-cell-border-color: #ecf0f1;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }
    // Update the footer creation in the start method
    // Add this method to the existing MediTrackApp class

    private void showCreateNewDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Create New Emplacement");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Form fields
        TextField addressField = new TextField();
        TextField cityField = new TextField();
        TextField postalCodeField = new TextField();

        // Labels with styling
        Label addressLabel = new Label("Address:");
        Label cityLabel = new Label("City:");
        Label postalCodeLabel = new Label("Postal Code:");

        grid.addRow(0, addressLabel, addressField);
        grid.addRow(1, cityLabel, cityField);
        grid.addRow(2, postalCodeLabel, postalCodeField);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        saveButton.setOnAction(e -> {
            int newId = emplacementData.size() + 1;
            Emplacement newEmplacement = new Emplacement(
                    newId,
                    addressField.getText(),
                    cityField.getText(),
                    postalCodeField.getText()
            );
            emplacementData.add(newEmplacement);
            dialogStage.close();
        });

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(grid, saveButton);
        layout.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(layout, 400, 250);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private void showEmplacementDetails(Emplacement emplacement) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Emplacement Details");
        alert.setHeaderText("Details for Emplacement #" + emplacement.getId());
        alert.setContentText(
                "Address: " + emplacement.getAddress() + "\n" +
                        "City: " + emplacement.getCity() + "\n" +
                        "Postal Code: " + emplacement.getPostalCode()
        );
        alert.showAndWait();
    }

    private void editEmplacement(Emplacement emplacement) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Emplacement");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Form fields pre-filled with existing data
        TextField addressField = new TextField(emplacement.getAddress());
        TextField cityField = new TextField(emplacement.getCity());
        TextField postalCodeField = new TextField(emplacement.getPostalCode());

        // Labels with styling
        Label addressLabel = new Label("Address:");
        Label cityLabel = new Label("City:");
        Label postalCodeLabel = new Label("Postal Code:");

        grid.addRow(0, addressLabel, addressField);
        grid.addRow(1, cityLabel, cityField);
        grid.addRow(2, postalCodeLabel, postalCodeField);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle(
                "-fx-background-color: #2ecc71;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 5px;"
        );
        saveButton.setOnAction(e -> {
            emplacement.setAddress(addressField.getText());
            emplacement.setCity(cityField.getText());
            emplacement.setPostalCode(postalCodeField.getText());
            table.refresh();
            dialogStage.close();
        });

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(grid, saveButton);
        layout.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(layout, 400, 250);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
}