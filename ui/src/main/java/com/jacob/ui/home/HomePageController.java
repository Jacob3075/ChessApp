package com.jacob.ui.home;

import com.jacob.ui.auth.UserAuthState;
import com.jacob.ui.game.view.ViewPastGameController;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HomePageController implements Initializable {
    @FXML private Label username;
    @FXML private Button newGameButton;
    @FXML private Button savedGamesButton;
    private final Resource viewPastGameFxml;
    private final Resource playNewGameFxml;
    private final ApplicationContext context;
    private final UserAuthState userAuthState;

    public HomePageController(
            @Value("classpath:/view/game/view/view_past_game.fxml") Resource viewPastGameFxml,
            @Value("classpath:/view/game/play/play_new_game.fxml") Resource playNewGameFxml,
            ApplicationContext context,
            UserAuthState userAuthState) {
        this.viewPastGameFxml = viewPastGameFxml;
        this.playNewGameFxml = playNewGameFxml;
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
        JavaFxUtils.changeScene(mouseEvent, playNewGameFxml, context);
    }

    private void openPastGame(MouseEvent event) {
        var viewPastGameController =
                (ViewPastGameController) JavaFxUtils.changeScene(event, viewPastGameFxml, context);
        assert viewPastGameController != null;
        viewPastGameController.setDate(1);
        viewPastGameController.initializePage();
    }
}
