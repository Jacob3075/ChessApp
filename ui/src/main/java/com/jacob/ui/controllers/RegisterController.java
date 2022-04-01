package com.jacob.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        try {
            FXMLLoader targetFxmlLoader = new FXMLLoader(loginSceneFxml.getURL());
            targetFxmlLoader.setControllerFactory(context::getBean);
            Scene targetScene = new Scene(targetFxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(targetScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
