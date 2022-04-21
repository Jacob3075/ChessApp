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
    private int interval = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (interval < TOTAL_SECONDS) {
                            Platform.runLater(() -> updateTime(interval, interval));
                            interval++;
                        } else {
                            Platform.runLater(gameTimeOver);
                            stopTimer();
                        }
                    }
                },
                0,
                1000);
    }

    private void updateTime(Integer minutes, Integer seconds) {
        timerMinutes.setText(String.format("%02d", minutes));
        timerSeconds.setText(String.format("%02d", seconds));
    }

    public void setGameTimeOver(Runnable gameTimeOver) {
        this.gameTimeOver = gameTimeOver;
    }

    public void stopTimer() {
        timer.cancel();
    }
}
