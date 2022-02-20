package com.jacob.chessapp;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.jacob.database"})
@SpringBootApplication
public class ChessAppApplication {

    public static void main(String[] args) {
        Application.launch(ChessAppUI.class, args);
    }
}
