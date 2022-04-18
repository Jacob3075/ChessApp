package com.jacob.ui.game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PawnPromotionPopupController implements Initializable {
    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;
    private int selectedPiece = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button1.setOnMouseClicked(event -> setSelectedPiece(1));
        button2.setOnMouseClicked(event -> setSelectedPiece(2));
        button3.setOnMouseClicked(event -> setSelectedPiece(3));
        button4.setOnMouseClicked(event -> setSelectedPiece(4));
    }

    public int getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(int selectedPiece) {
        this.selectedPiece = selectedPiece;
        Stage stage = (Stage) button1.getScene().getWindow();
        stage.close();
    }
}
