package com.jacob.ui.game;

import javafx.scene.Node;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;


public class GameTimer extends Node {
    JFrame display;
    JLabel counterLabel;
    Font font = new Font("Arial", Font.PLAIN, 70);

    Timer timer;

    int minute, second;
    String displaymin, displaysec;

    DecimalFormat displayFormat = new DecimalFormat("00");


    public static void main(String[] args) {
        new GameTimer();
    }

    public Node GameTimer() {
        display = new JFrame();
        display.setSize(200, 200);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setLayout(null);

        counterLabel = new JLabel();
        counterLabel.setBounds(0, 30, 200, 100);
        counterLabel.setHorizontalAlignment(JLabel.CENTER);
        counterLabel.setFont(font);

        display.add(counterLabel);
        display.setVisible(true);

        counterLabel.setText("05:00");
        second = 0;
        minute = 5;
        showTimer();
        timer.start();
        return null;
    }


    public void showTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                second--;
                displaysec = displayFormat.format(second);
                displaymin = displayFormat.format(minute);

                counterLabel.setText(displaymin + ":" + displaysec);
                if(second == -1){
                    second = 59;
                    minute--;
                    displaysec = displayFormat.format(second);
                    displaymin = displayFormat.format(minute);
                    counterLabel.setText(displaymin + ":" + displaysec);
                }
                if(minute==0 && second==0) {
                    timer.stop();
                }
            }
        });
    }



}


