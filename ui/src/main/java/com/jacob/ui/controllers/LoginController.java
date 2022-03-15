package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.net.URL;

public class LoginController implements Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView chessImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File chessFile = new File("src/images/chessLogo.jpg");
        Image chessImage = new Image(chessFile.toURI().toString());
        chessImageView.setImage(chessImage);
    }

    public void loginButtonOnAction(ActionEvent event){
        if (usernameTextField.getText().isBlank() == false && enterPasswordField.getText().isBlank() == false) {
            validateLogin();
        }
        else{
            loginMessageLabel.setText("Please enter username and password.");
        }
    }

    public void cancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM user_accounts WHERE username = '" + usernameTextField.getText() + "' AND password = '" + enterPasswordField.getText() + "'";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if (queryResult.getInt(1) == 1){
                    loginMessageLabel.setText("Congratulations!");
                    //createAccountForm();
                }
                else {
                    loginMessageLabel.setText("Invalid login. Please try again.");
                }
            }

        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
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