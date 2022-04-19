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
            @NotNull Resource targetSceneFxml,
            @NotNull ApplicationContext context) {
        try {
            FXMLLoader targetFxmlLoader = new FXMLLoader(targetSceneFxml.getURL());
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
            @NotNull Resource targetSceneFxml,
            @NotNull ApplicationContext context) {
        try {
            FXMLLoader targetFxmlLoader = new FXMLLoader(targetSceneFxml.getURL());
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
}
