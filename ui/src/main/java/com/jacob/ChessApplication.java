package com.jacob;

import com.jacob.ui.ChessAppUI;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChessApplication {

    public static void main(String[] args) {
        Application.launch(ChessAppUI.class, args);
    }
}
