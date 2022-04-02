package com.jacob.ui.auth;

import com.jacob.database.user.UserService;
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
    private final Resource gameSceneFxml;
    private final ApplicationContext context;
    private final UserService userService;

    public LoginController(
            @Value("classpath:/view/register_ui.fxml") Resource registerSceneFxml,
            @Value("classpath:/view/chess-board.fxml") Resource gameSceneFxml,
            ApplicationContext context,
            UserService userService) {
        this.registerSceneFxml = registerSceneFxml;
        this.gameSceneFxml = gameSceneFxml;
        this.context = context;
        this.userService = userService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(this::loginButtonOnAction);
        registerButton.setOnAction(this::showRegisterScreen);
    }

    private void loginButtonOnAction(ActionEvent event) {
        loginMessageLabel.setText("");

        String username = usernameTextField.getText();
        String password = enterPasswordField.getText();
        boolean isUserNameOrPasswordEmpty = username.isBlank() || password.isBlank();

        if (isUserNameOrPasswordEmpty) {
            loginMessageLabel.setText("Please enter username and password.");
            return;
        }

        if (userService.login(username, password)) {
            loginMessageLabel.setText("Logged in");
            JavaFxUtils.changeScene(event, gameSceneFxml, context);
        } else {
            loginMessageLabel.setText("Invalid username and password");
        }
    }

    private void showRegisterScreen(@NotNull ActionEvent event) {
        JavaFxUtils.changeScene(event, registerSceneFxml, context);
    }
}
