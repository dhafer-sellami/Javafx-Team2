package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Home {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private void handleOpenForm(ActionEvent event) {
        try {
            Parent formView = FXMLLoader.load(getClass().getResource("/RendezVousForm.fxml"));
            contentPane.getChildren().setAll(formView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenTable(ActionEvent event) {
        try {
            Parent tableView = FXMLLoader.load(getClass().getResource("/RendezVousTable.fxml"));
            contentPane.getChildren().setAll(tableView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handlesignin(ActionEvent event) {
        try {
            Parent formView = FXMLLoader.load(getClass().getResource("/Admin.fxml"));
            contentPane.getChildren().setAll(formView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handlearticles(ActionEvent event) {
        try {
            Parent formView = FXMLLoader.load(getClass().getResource("/Articles.fxml"));
            contentPane.getChildren().setAll(formView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handlemedicaments(ActionEvent event) {
        try {
            Parent formView = FXMLLoader.load(getClass().getResource("/medicament.fxml"));
            contentPane.getChildren().setAll(formView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
