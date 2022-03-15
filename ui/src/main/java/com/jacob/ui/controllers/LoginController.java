package com.jacob.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable {
    @FXML private Button cancelButton;
    @FXML private Button loginButton;
    @FXML private Label loginMessageLabel;
    @FXML private ImageView chessImageView;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField enterPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File chessFile = new File("src/images/chessLogo.jpg");
        Image chessImage = new Image(chessFile.toURI().toString());
        //        chessImageView.setImage(chessImage);
    }

    public void loginButtonOnAction(ActionEvent event) {
        if (!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()) {
        } else {
            loginMessageLabel.setText("Please enter username and password.");
        }
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /*
       public void createAccountForm(){
           try{

               FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register_ui.fxml"));
               Scene scene = new Scene(fxmlLoader.load(), 520, 400);
               Stage registerStage = new Stage();
               registerStage.initStyle(StageStyle.UNDECORATED);
               registerStage.setScene(scene);
               registerStage.show();
           } catch(Exception e){
               e.printStackTrace();
               e.getCause();
           }

       }
    */

}
