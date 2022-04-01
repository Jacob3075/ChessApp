package com.jacob.ui.controllers;

import com.jacob.ui.JavaFxUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable {
    @FXML private Button registerButton;
    @FXML private Button loginButton;
    @FXML private Label loginMessageLabel;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField enterPasswordField;

    private final Resource registerSceneFxml;
    private final ApplicationContext context;

    public LoginController(
            @Value("classpath:/register_ui.fxml") Resource registerSceneFxml,
            ApplicationContext context) {
        this.registerSceneFxml = registerSceneFxml;
        this.context = context;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(this::loginButtonOnAction);
        registerButton.setOnAction(this::showRegisterScreen);
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

    private void showRegisterScreen(@NotNull ActionEvent event) {
        JavaFxUtils.changeScene(event, registerSceneFxml, context);
    }
}
