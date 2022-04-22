package com.jacob.ui.game.play;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class TimerController implements Initializable {
    private static final int TOTAL_SECONDS = 600;
    private final Timer timer = new Timer();
    @FXML public Label timerMinutes;
    @FXML public Label timerSeconds;
    private Runnable gameTimeOver;
    private int currentSeconds = 600;
    private TimerTask task;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        task = new TimerTask() {
            @Override
            public void run() {
                if (currentSeconds >= 0) {
                    Platform.runLater(() -> updateTime(currentSeconds));
                    currentSeconds--;
                } else {
                    Platform.runLater(gameTimeOver);
                    stopTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(
                task,
                1000,
                1000);
    }

    private void updateTime(int seconds) {
        timerMinutes.setText(String.format("%02d", seconds/60));
        timerSeconds.setText(String.format("%02d", seconds%60));
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
