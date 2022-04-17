package com.jacob.ui.home;

import com.jacob.ui.utils.JavaFxUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class HomePageController implements Initializable {
    @FXML private Button userButton;
    @FXML private Button newGameButton;
    @FXML private Button savedGamesButton;
    private final Resource viewPastGameFxml;
    private final ApplicationContext context;

    public HomePageController(
            @Value("classpath:/view/view_past_game.fxml") Resource viewPastGameFxml,
            ApplicationContext context) {
        this.viewPastGameFxml = viewPastGameFxml;
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        savedGamesButton.setOnMouseClicked(
                event -> JavaFxUtils.changeScene(event, viewPastGameFxml, context));
    }
}
