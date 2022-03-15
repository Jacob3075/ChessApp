package com.jacob.ui.controllers;

import com.jacob.ui.ChessAppUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable {
    @FXML private Button cancelButton;
    @FXML private Button loginButton;
    @FXML private Label loginMessageLabel;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField enterPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(this::loginButtonOnAction);
        cancelButton.setOnAction(this::cancelButtonOnAction);
    }

    private void loginButtonOnAction(ActionEvent event) {
        boolean isUserNameOrPasswordEmpty =
                usernameTextField.getText().isBlank() || enterPasswordField.getText().isBlank();
        if (isUserNameOrPasswordEmpty) {
            loginMessageLabel.setText("Please enter username and password.");
            return;
        }

        loginMessageLabel.setText("Logged in");
    }

    private void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void createAccountForm() {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(ChessAppUI.class.getResource("register_ui.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(scene);
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
