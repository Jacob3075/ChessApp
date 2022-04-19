package com.jacob.ui.auth;

import com.jacob.database.user.UserService;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoginController implements Initializable {
    @FXML private Button registerButton;
    @FXML private Button loginButton;
    @FXML private Label loginMessageLabel;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField enterPasswordField;

    private final ApplicationContext context;
    private final UserService userService;
    private final UserAuthState userAuthState;

    public LoginController(
            ApplicationContext context, UserService userService, UserAuthState userAuthState) {
        this.context = context;
        this.userService = userService;
        this.userAuthState = userAuthState;
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

        userService
                .login(username, password)
                .ifPresentOrElse(
                        user -> {
                            loginMessageLabel.setText("Logged in");
                            userAuthState.loginUser(user);
                            JavaFxUtils.changeScene(event, JavaFxUtils.Views.HOME, context);
                        },
                        () -> loginMessageLabel.setText("Invalid username and password"));
    }

    private void showRegisterScreen(@NotNull ActionEvent event) {
        JavaFxUtils.changeScene(event, JavaFxUtils.Views.REGISTER, context);
    }
}
