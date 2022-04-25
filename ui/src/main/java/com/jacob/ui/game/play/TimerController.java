package com.jacob.ui.game.play;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TimerController implements Initializable {
    private Timer timer = new Timer(true);
    @FXML private Label timerMinutes;
    @FXML private Label timerSeconds;
    private Runnable gameTimeOver;
    private int currentSeconds = 600;
    private TimerTask task;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTime();
    }

    public void startTime() {
        timer = new Timer(true);
        task =
                new TimerTask() {
                    @Override
                    public void run() {
                        if (currentSeconds > 0) {
                            Platform.runLater(() -> updateTime(currentSeconds));
                            currentSeconds--;
                        } else {
                            Platform.runLater(gameTimeOver);
                            stopTimer();
                        }
                    }
                };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    private void updateTime(int seconds) {
        timerMinutes.setText(String.format("%02d", seconds / 60));
        timerSeconds.setText(String.format("%02d", seconds % 60));
    }

    public void setGameTimeOver(Runnable gameTimeOver) {
        this.gameTimeOver = gameTimeOver;
    }

    public void stopTimer() {
        task.cancel();
        timer.cancel();
        timer.purge();
    }
}
