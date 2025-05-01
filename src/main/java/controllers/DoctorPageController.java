package controllers;

import entities.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.AlertMessage;
import utils.Database;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorPageController implements Initializable {
    @javafx.fxml.FXML
    private TextField login_showPassword;
    @javafx.fxml.FXML
    private AnchorPane register_form;
    @javafx.fxml.FXML
    private Button login_loginBtn;
    @javafx.fxml.FXML
    private TextField register_showPassword;
    @javafx.fxml.FXML
    private TextField register_fullName;
    @javafx.fxml.FXML
    private Hyperlink register_loginHere;
    @javafx.fxml.FXML
    private CheckBox register_checkBox;
    @javafx.fxml.FXML
    private Button register_signupBtn;
    @javafx.fxml.FXML
    private AnchorPane main_form;
    @javafx.fxml.FXML
    private CheckBox login_checkBox;
    @javafx.fxml.FXML
    private PasswordField login_password;
    @javafx.fxml.FXML
    private ComboBox login_user;
    @javafx.fxml.FXML
    private Hyperlink login_registerHere;
    @javafx.fxml.FXML
    private AnchorPane login_form;
    @javafx.fxml.FXML
    private TextField register_email;
    @javafx.fxml.FXML
    private TextField register_doctorID;
    @javafx.fxml.FXML
    private TextField login_doctorID;
    @javafx.fxml.FXML
    private PasswordField register_password;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private AlertMessage alert = new AlertMessage();

    @javafx.fxml.FXML

    void loginAccount(ActionEvent event) {

        if (login_doctorID.getText().isEmpty()
                || login_password.getText().isEmpty()) {
            alert.errorMessage("Incorrect Patient ID/Password");
        } else {

            String sql = "SELECT * FROM patient WHERE patient_id = ? AND password = ? AND date_delete IS NULL";
            connect = Database.connectDB();

            try {

                if (!login_showPassword.isVisible()) {
                    if (!login_showPassword.getText().equals(login_password.getText())) {
                        login_showPassword.setText(login_password.getText());
                    }
                } else {
                    if (!login_showPassword.getText().equals(login_password.getText())) {
                        login_password.setText(login_showPassword.getText());
                    }
                }

                // CHECK IF THE STATUS OF THE DOCTOR IS CONFIRM
                String checkStatus = "SELECT status FROM patient WHERE patient_id = '"
                        + login_doctorID.getText() + "' AND password = '"
                        + login_password.getText() + "' AND status = 'Confirm'";

                prepare = connect.prepareStatement(checkStatus);
                result = prepare.executeQuery();

                if (result.next()) {

                    if (!login_showPassword.isVisible()) {
                        if (!login_showPassword.getText().equals(login_password.getText())) {
                            login_showPassword.setText(login_password.getText());
                        }
                    } else {
                        if (!login_showPassword.getText().equals(login_password.getText())) {
                            login_password.setText(login_showPassword.getText());
                        }
                    }

                    alert.errorMessage("Need the confimation of the Admin!");
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, login_doctorID.getText());
                    prepare.setString(2, login_password.getText());

                    result = prepare.executeQuery();

                    if (result.next()) {
                        Data.patient_id = Integer.parseInt(login_doctorID.getText());

                        alert.successMessage("Login Successfully!");
                        // LINK YOUR PATIENT MAIN FORM
                        Parent root = FXMLLoader.load(getClass().getResource("PatientMainForm.fxml"));
                        Stage stage = new Stage();

                        stage.setScene(new Scene(root));
                        stage.show();

                        // TO HIDE YOUR LOGIN FORM
                        login_loginBtn.getScene().getWindow().hide();
                    } else {
                        alert.errorMessage("Incorrect Patient ID/Password");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @javafx.fxml.FXML
    void loginShowPassword(ActionEvent event) {

        if (login_checkBox.isSelected()) {
            login_showPassword.setText(login_password.getText());
            login_password.setVisible(false);
            login_showPassword.setVisible(true);
        } else {
            login_password.setText(login_showPassword.getText());
            login_password.setVisible(true);
            login_showPassword.setVisible(false);
        }

    }

    public void userList() {

        List<String> listU = new ArrayList<>();

        for (String data : Users.user) {
            listU.add(data);
        }

        ObservableList listData = FXCollections.observableList(listU);
        login_user.setItems(listData);
    }

    @javafx.fxml.FXML
    public void registerAccount(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    void switchPage(ActionEvent event) {

        if (login_user.getSelectionModel().getSelectedItem() == "Admin Portal") {

            try {

                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();

                stage.setTitle("Hospital Management System");

                stage.setMinWidth(340);
                stage.setMinHeight(580);

                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (login_user.getSelectionModel().getSelectedItem() == "Doctor Portal") {

            try {

                Parent root = FXMLLoader.load(getClass().getResource("DoctorPage.fxml"));
                Stage stage = new Stage();

                stage.setTitle("Hospital Management System");

                stage.setMinWidth(340);
                stage.setMinHeight(580);

                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (login_user.getSelectionModel().getSelectedItem() == "Patient Portal") {

            try {

                Parent root = FXMLLoader.load(getClass().getResource("PatientPage.fxml"));
                Stage stage = new Stage();

                stage.setTitle("Hospital Management System");

                stage.setMinWidth(340);
                stage.setMinHeight(580);

                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        login_user.getScene().getWindow().hide();

    }

    @javafx.fxml.FXML
    public void registerShowPassword(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    void switchForm(ActionEvent event) {

        if (event.getSource() == register_loginHere) {
            login_form.setVisible(true);
            register_form.setVisible(false);
        } else if (event.getSource() == login_registerHere) {
            login_form.setVisible(false);
            register_form.setVisible(true);
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList();
    }
}
