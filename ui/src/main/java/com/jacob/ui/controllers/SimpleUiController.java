package com.jacob.ui.controllers;

import com.jacob.database.SampleDB;
import com.jacob.database.SampleModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class SimpleUiController {
    private final SampleDB sampleDB;
    @FXML private Label myLabel;
    @FXML private Button myButton;

    public SimpleUiController(SampleDB sampleDB) {
        this.sampleDB = sampleDB;
    }

    @FXML
    private void initialize() {
        myButton.setOnAction(
                it -> {
                    sampleDB.save(new SampleModel(0L, "Value 1", "Value 2", "Value 3"));
                    myLabel.setText("Clicked");
                });
    }
}
