package controllers;

import entities.RendezVous;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.*;
import services.ChatBotService;
import services.RendezVousService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RendezVousFormController {

    @FXML private TextField emailField;
    @FXML private TextField numField;
    @FXML private TextField etatField;
    @FXML private DatePicker datePicker;
    @FXML private Button btnAide;
    @FXML private TextArea txtChatBot;
    @FXML private TextField userQuestionField;


    private RendezVousService service = new RendezVousService();
    private RendezVous rendezVous;
    @FXML
    private Button saveButton;

    public void setRendezVous(RendezVous rdv) {
        this.rendezVous = rdv;

        emailField.setText(rdv.getEmail());
        numField.setText(rdv.getNum());
        etatField.setText(rdv.getEtat());
        datePicker.setValue(rdv.getDate());
    }

    @FXML
    public void handleSave() {
        String email = emailField.getText();
        String num = numField.getText();
        String etat = etatField.getText();
        LocalDate localDate = datePicker.getValue();

        if (email.isEmpty() || num.isEmpty() || etat.isEmpty() || localDate == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }
        if (!num.matches("\\d+")) {
            showAlert("Erreur", "Le champ numéro doit contenir uniquement des chiffres.");
            return;
        }

        if (num.length() != 8) {
            showAlert("Erreur", "Le numéro doit être égal à 8 chiffres.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showAlert("Erreur", "Le format de l'email est invalide.");
            return;
        }

        if (!etat.matches("[a-zA-Z]+")) {
            showAlert("Erreur", "Le champ 'État' ne doit contenir que des lettres.");
            return;
        }

        if (localDate.isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date ne peut pas être dans le passé !");
            return;
        }

        if (rendezVous == null && service.getCountByDate(localDate) >= 2) {
            showAlert("Erreur", "Il y a déjà 2 rendez-vous pour cette date !");
            return;
        }

        if (rendezVous == null) {
            RendezVous rdv = new RendezVous(localDate, email, num, etat);
            service.ajouter(rdv);
            showAlert("Succès", "Rendez-vous ajouté !");
        } else {
            rendezVous.setDate(localDate);
            rendezVous.setEmail(email);
            rendezVous.setNum(num);
            rendezVous.setEtat(etat);
            service.modifier(rendezVous);
            showAlert("Succès", "Rendez-vous modifié !");
        }
    }

    private Date convertirLocalDateVersUtilDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleAIHelp() {
        String question = userQuestionField.getText().trim();

        if (question.isEmpty()) {
            txtChatBot.setText("Veuillez entrer une question.");
            return;
        }

        new Thread(() -> {
            try {
                String response = ChatBotService.getChatResponse(question);
                Platform.runLater(() -> txtChatBot.setText(response));
            } catch (IOException e) {
                Platform.runLater(() -> txtChatBot.setText("Erreur réseau : " + e.getMessage()));
            }
        }).start();
    }



}
