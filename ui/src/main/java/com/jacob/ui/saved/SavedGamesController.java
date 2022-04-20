package com.jacob.ui.saved;

import com.jacob.database.game_data.PastGame;
import com.jacob.ui.auth.UserAuthState;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class SavedGamesController implements Initializable {
    private final UserAuthState userAuthState;
    @FXML
    private ListView<SavedGameItem> pastGames;

    public SavedGamesController(UserAuthState userAuthState) {
        this.userAuthState = userAuthState;
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
            System.out.println("pastGame.getId() = " + pastGame.getId());

        });
    }
}

record SavedGameItem(Long id, String createdTime, String result) {
    public static SavedGameItem createFromEntity(PastGame pastGame) {
        return new SavedGameItem(pastGame.getId(), pastGame.getCreatedTime(), pastGame.getResult());
    }

    @Override
    public String toString() {
        return "SavedGameItem{createdTime='%s', result='%s'}".formatted(createdTime, result);
    }

}
