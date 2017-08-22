package com.ErMedia.user.resources.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by root on 1/11/17.
 */
public class RegisterController implements Initializable{
    @FXML
    private JFXTextField usernameTF;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.setRegisterController(this);
    }



    public void usernameExist(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Wrong username");
                alert.setHeaderText(null);
                alert.setContentText("This username is already exist!+_+");
                alert.showAndWait();
            }
        });
    }

    public void registrationIsDone(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Welcome");
                alert.setHeaderText(null);
                alert.setContentText("Registration is successfully done!");
                alert.showAndWait();
                MainController.getRegisterStage().close();
            }
        });


    }

    @FXML
    private void registerHandler(ActionEvent actionEvent) {
        if(!usernameTF.getText().isEmpty())
            MainController.getClient().getClientconnection().sendMessage("register " + usernameTF.getText());
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty Warning");
            alert.setHeaderText(null);
            alert.setContentText("its Empty!!! ?? ?? ?? -______-");
            alert.showAndWait();
        }
    }

}
