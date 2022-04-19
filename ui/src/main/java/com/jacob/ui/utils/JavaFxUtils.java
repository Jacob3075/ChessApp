package com.jacob.ui.utils;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class JavaFxUtils {

    private JavaFxUtils() {}

    public static Object changeScene(
            @NotNull Event event,
            @NotNull String targetScenePath,
            @NotNull ApplicationContext context) {
        try {
            FXMLLoader targetFxmlLoader =
                    new FXMLLoader(JavaFxUtils.class.getResource(targetScenePath));
            targetFxmlLoader.setControllerFactory(context::getBean);
            Scene targetScene = new Scene(targetFxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(targetScene);
            stage.show();
            return targetFxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    public static Object changeScene(
            @NotNull Stage stage,
            @NotNull String targetScenePath,
            @NotNull ApplicationContext context) {
        try {
            FXMLLoader targetFxmlLoader =
                    new FXMLLoader(JavaFxUtils.class.getResource(targetScenePath));
            targetFxmlLoader.setControllerFactory(context::getBean);
            Scene targetScene = new Scene(targetFxmlLoader.load());
            stage.setScene(targetScene);
            stage.show();
            return targetFxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    public static void showPopupAndWait(
            @NotNull Resource targetSceneFxml, @NotNull Object controller) {
        try {
            FXMLLoader targetFxmlLoader = new FXMLLoader(targetSceneFxml.getURL());
            targetFxmlLoader.setController(controller);
            Scene targetScene = new Scene(targetFxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(targetScene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public static class Views {
        public static final String LOGIN = "/view/auth/login_page.fxml";
        public static final String REGISTER = "/view/auth/register_page.fxml";
        public static final String HOME = "/view/home_page.fxml";
        public static final String SAVED_GAMES = "/view/saved_games_page.fxml";
        public static final String PLAY_GAME = "/view/game/play/play_new_game.fxml";
        public static final String PAWN_PROMOTION_POPUP =
                "/view/game/play/pawn_promotion_popup.fxml";
        public static final String VIEW_GAME = "/view/game/view/view_past_game.fxml";

        private Views() {}
    }
}
