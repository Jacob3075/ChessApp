package com.jacob.ui.events;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {
    private final Resource fxml;
    private final ApplicationContext context;

    public StageInitializer(
            @Value("classpath:/view/login_page.fxml") Resource fxml, ApplicationContext context) {
        this.fxml = fxml;
        this.context = context;
    }

    @Override
    public void onApplicationEvent(@NotNull StageReadyEvent event) {
        try {
            var stage = event.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(fxml.getURL());
            fxmlLoader.setControllerFactory(context::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
