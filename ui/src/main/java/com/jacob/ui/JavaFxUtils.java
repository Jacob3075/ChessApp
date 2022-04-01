package com.jacob.ui;

import javafx.event.ActionEvent;
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

    public static void changeScene(
            @NotNull ActionEvent event,
            @NotNull Resource targetSceneFxml,
            @NotNull ApplicationContext context) {
        try {
            FXMLLoader targetFxmlLoader = new FXMLLoader(targetSceneFxml.getURL());
            targetFxmlLoader.setControllerFactory(context::getBean);
            Scene targetScene = new Scene(targetFxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(targetScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
