package com.jacob.ui.home;

import com.jacob.ui.auth.UserAuthState;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HomePageController implements Initializable {
    @FXML private Label username;
    @FXML private Button newGameButton;
    @FXML private Button savedGamesButton;
    private final ApplicationContext context;
    private final UserAuthState userAuthState;

    public HomePageController(ApplicationContext context, UserAuthState userAuthState) {
        this.context = context;
        this.userAuthState = userAuthState;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        savedGamesButton.setOnMouseClicked(this::openPastGame);
        newGameButton.setOnMouseClicked(this::playNewGame);
        username.setText(userAuthState.getLoggedInUser().getUsername());
    }

    private void playNewGame(MouseEvent mouseEvent) {
        Stage stage = (Stage) username.getScene().getWindow();
        JavaFxUtils.changeScene(stage, JavaFxUtils.Views.PLAY_GAME, context);
    }

    private void openPastGame(MouseEvent event) {
        Stage stage = (Stage) username.getScene().getWindow();
        JavaFxUtils.changeScene(stage, JavaFxUtils.Views.SAVED_GAMES, context);
    }
}
