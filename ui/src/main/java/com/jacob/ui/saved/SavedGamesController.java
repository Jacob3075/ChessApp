package com.jacob.ui.saved;

import com.jacob.database.game_data.PastGame;
import com.jacob.ui.auth.UserAuthState;
import com.jacob.ui.game.view.ViewPastGameController;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class SavedGamesController implements Initializable {
    @FXML
    private ListView<SavedGameItem> pastGames;
    private final UserAuthState userAuthState;
    private final ApplicationContext context;

    public SavedGamesController(UserAuthState userAuthState, ApplicationContext context) {
        this.userAuthState = userAuthState;
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<PastGame> pastGamesPlayed = userAuthState.getLoggedInUser().getPastGamesPlayed();

        List<SavedGameItem> savedGameItems = pastGamesPlayed.stream()
                .map(SavedGameItem::createFromEntity)
                .toList();
        pastGames.getItems().addAll(savedGameItems);

        pastGames.setOnMouseClicked(click -> {
            if (click.getClickCount() != 2) return;

            int selectedIndex = pastGames.getSelectionModel().getSelectedIndex();
            PastGame pastGame = pastGamesPlayed.get(selectedIndex);
            Stage stage = (Stage)pastGames.getScene().getWindow();
            ViewPastGameController controller =
                    (ViewPastGameController)
                            JavaFxUtils.changeScene(stage, JavaFxUtils.Views.VIEW_GAME, context);

            assert controller != null;
            controller.setDate(pastGame);
            controller.initializePage();
        });
    }

    private record SavedGameItem(Long id, String createdTime, String result) {
        public static SavedGameItem createFromEntity(PastGame pastGame) {
            return new SavedGameItem(pastGame.getId(), pastGame.getCreatedTime(), pastGame.getResult());
        }

        @Override
        public String toString() {
            return "SavedGameItem{createdTime='%s', result='%s'}".formatted(createdTime, result);
        }
    }
}
