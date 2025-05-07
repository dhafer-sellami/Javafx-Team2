package controllers;

import entities.Facture;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.FactureService;

public class FactureFormController {

    @FXML private TextField prixField;

    private int rendezVousId;
    private FactureService factureService;

    public void setRendezVousId(int id) {
        this.rendezVousId = id;
    }

    public void setFactureService(FactureService service) {
        this.factureService = service;
    }

    @FXML
    public void ajouterFacture() {
        double prix = Double.parseDouble(prixField.getText());
        Facture facture = new Facture(prix, rendezVousId);
        try {
            factureService.ajouterFacture(facture);
            showAlert("le prix a été fixé avec succès  !");
            ((Stage) prixField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}