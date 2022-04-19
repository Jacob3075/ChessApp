package com.jacob.ui.auth;

import com.jacob.database.user.User;
import com.jacob.database.user.UserService;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegisterController implements Initializable {
    @FXML private TextField usernameTextField;
    @FXML private PasswordField setPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerButton;
    @FXML private Button loginButton;
    @FXML private Label registerMessageLabel;
    private final ApplicationContext context;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    public RegisterController(ApplicationContext context, UserService userService) {
        this.context = context;
        this.userService = userService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(this::showLoginScreen);
        registerButton.setOnAction(this::registerUser);
    }

    private void registerUser(ActionEvent event) {
        registerMessageLabel.setText("");

        String username = usernameTextField.getText();
        String password = setPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        boolean isUserNameOrPasswordEmpty =
                username.isBlank() || password.isBlank() || confirmPassword.isBlank();

        if (isUserNameOrPasswordEmpty) {
            registerMessageLabel.setText("Please enter username and password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerMessageLabel.setText("Password mismatch");
            return;
        }

        User user = userService.registerUser(username, password);
        logger.debug("user = {}", user);
        registerMessageLabel.setText("Registered");

        showLoginScreen(event);
    }

    private void showLoginScreen(@NotNull ActionEvent event) {
        Stage stage = (Stage) registerMessageLabel.getScene().getWindow();
        JavaFxUtils.changeScene(stage, JavaFxUtils.Views.LOGIN, context);
    }
}
