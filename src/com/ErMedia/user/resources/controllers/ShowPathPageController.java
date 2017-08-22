package com.ErMedia.user.resources.controllers;

import com.ErMedia.LogicModules.DataStructure.ArrayListI;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by root on 1/12/17.
 */
public class ShowPathPageController implements Initializable{

    @FXML
    private JFXTextArea pathsTA;

    @FXML
    private JFXTextField usernameTF ;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.getClient().setShowPathPageController(this);
    }

    @FXML
    private void pathFinderButtonHandler(ActionEvent actionEvent) {
        pathsTA.setText("");
        if (!usernameTF.getText().equals("")) {
            MainController.getClient().getClientconnection().sendMessage("pathFinder " + usernameTF.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty Warning");
            alert.setHeaderText(null);
            alert.setContentText("its Empty!!! ?? ?? ?? -______-");
            alert.showAndWait();
        }
        usernameTF.setText("");
    }

    public void showPath(ArrayListI paths) {
        for (int i = 0; i < paths.size(); i++) {
            pathsTA.appendText((String) paths.get(i) + "\n");
        }
    }
}
