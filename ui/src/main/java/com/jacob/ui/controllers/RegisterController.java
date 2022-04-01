package com.jacob.ui.controllers;

import com.jacob.ui.JavaFxUtils;
import javafx.event.ActionEvent;
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
public class RegisterController implements Initializable {
    public TextField usernameTextField;
    public PasswordField setPasswordField;
    public PasswordField confirmPasswordField;
    public Button registerButton;
    public Button loginButton;
    public Label registerMessageLabel;
    private final Resource loginSceneFxml;
    private final ApplicationContext context;

    public RegisterController(
            @Value("classpath:/login_ui.fxml") Resource loginSceneFxml,
            ApplicationContext context) {
        this.loginSceneFxml = loginSceneFxml;
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(this::showLoginScreen);
        registerButton.setOnAction(this::registerUser);
    }

    private void registerUser(ActionEvent event) {}

    private void showLoginScreen(@NotNull ActionEvent event) {
        JavaFxUtils.changeScene(event, loginSceneFxml, context);
    }
}
