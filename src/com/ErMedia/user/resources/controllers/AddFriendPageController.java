package com.ErMedia.user.resources.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * Created by root on 1/12/17.
 */
public class AddFriendPageController{

    @FXML
    private JFXTextField usernameTF;

    @FXML
    private void sendRequestButtonHandler(ActionEvent actionEvent){
        if(!usernameTF.getText().isEmpty()) {
            if (ChatRoomController.getUser().getName().equals(usernameTF.getText())) {
                itsYou();
                return;
            }
            MainController.getClient().getClientconnection().sendMessage("sendRequest " + usernameTF.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty Warning");
            alert.setHeaderText(null);
            alert.setContentText("its Empty!!! ?? ?? ?? -______-");
            alert.showAndWait();
        }
    }

    public void itsYou(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Request");
                alert.setHeaderText(null);
                alert.setContentText("its you.");
                alert.showAndWait();
            }
        });
    }

}
