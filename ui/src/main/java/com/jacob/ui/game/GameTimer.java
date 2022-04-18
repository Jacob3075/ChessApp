package com.jacob.ui.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameTimer {
    private static final int START_SECONDS = 59;
    private static final int START_MIN = 1;

    private final Runnable gameTimeOver;
    private final Label timerMinutes;
    private final Label timerSeconds;
    private final Timeline timeline = new Timeline();
    private int timeSeconds = START_SECONDS;
    private int timeMinutes = START_MIN;

    public GameTimer(Runnable gameTimeOver, Label timerMinutes, Label timerSeconds) {
        this.gameTimeOver = gameTimeOver;
        this.timerMinutes = timerMinutes;
        this.timerSeconds = timerSeconds;
    }

    public void startCountDown() {
        if (timeMinutes < 0) return;

        updateTime(timeMinutes, timeSeconds);
        KeyFrame keyframe =
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            timeSeconds--;
                            boolean isSecondsZero = timeSeconds == 0;
                            boolean isMinutesZero = timeMinutes == 0;
                            if (isSecondsZero) {
                                timeSeconds--;
                                timeSeconds = 60;
                                timeMinutes--;
                                System.out.println(timeMinutes + "+" + timeSeconds);
                            }
                            if (isMinutesZero && isSecondsZero) {
                                timeline.stop();
                                gameTimeOver.run();
                                timeMinutes = 0;
                                timeSeconds = 0;
                            }
                            updateTime(timeMinutes, timeSeconds);
                        });
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(keyframe);
        timeline.playFromStart();
    }

    private void updateTime(Integer minutes, Integer seconds) {
        timerMinutes.setText(String.format("%02d", minutes));
        timerSeconds.setText(String.format("%02d", seconds));
    }
}
