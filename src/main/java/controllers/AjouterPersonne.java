package controllers;

import entities.Personne;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import services.PersonneService;

import java.sql.SQLException;

public class AjouterPersonne {

    @javafx.fxml.FXML
    private TextField ageTF;
    @javafx.fxml.FXML
    private TextField nomTF;
    @javafx.fxml.FXML
    private TextField prenomTF;

    @javafx.fxml.FXML
    public void AfficherPersonne(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void AjouterPersonne(ActionEvent actionEvent) {
        PersonneService ps = new PersonneService();
        String nom = nomTF.getText();
        String prenom = prenomTF.getText();
        int age = Integer.parseInt(ageTF.getText());
        Personne p = new Personne(age, nom , prenom);
        try {
            ps.ajouter(p);
        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText(e.getMessage());
            a.show();
        }
        }
        @javafx.fxml.FXML
    public void AfficherPersonne(MouseEvent mouseEvent) {}
    }
