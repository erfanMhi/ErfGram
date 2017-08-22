package com.ErMedia.user.resources.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by root on 1/12/17.
 */
public class LevelSearchPageController implements Initializable {

    @FXML
    private JFXComboBox levelCB;

    private ObservableList<Integer> cBoxValues;
    private ObservableList<String> requestLvObservable;

    @FXML
    private JFXListView requestLv;

    private String currentRequest;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.getClient().setLevelSearchPageController(this);
        cBoxValues = FXCollections.observableArrayList(2,3,4,5,6,7,8,9,10,11,12,13,14);
        levelCB.setItems(cBoxValues);
        requestLvObservable = FXCollections.observableArrayList();
        requestLv.setItems(requestLvObservable);
        requestLv.setOnMouseClicked(e -> {
            if(requestLv.getSelectionModel().getSelectedItem() != null){
                currentRequest = (String)requestLv.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void setRequestLvObservable(String levelUsers[]){
        System.out.println(levelUsers);
        System.out.println(requestLvObservable);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    requestLvObservable.clear();
                    requestLvObservable.addAll(levelUsers);
            }
        });

    }

    public void clearRequestObservable(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                requestLvObservable.clear();
            }
        });

    }



    @FXML
    private void levelSelectionHandler(ActionEvent actionEvent){
        if(levelCB.getValue() != null) {
            System.out.println(levelCB.getValue());
            MainController.getClient().getClientconnection().sendMessage("levelSearch " + levelCB.getValue());
        }
    }

    @FXML
    private void sendRequestButtonHandler(ActionEvent actionEvent){
        if(currentRequest != null) {
            requestLv.getSelectionModel().getSelectedItem();
            requestLvObservable.remove(currentRequest);
            MainController.getClient().getClientconnection().sendMessage("sendRequest " + currentRequest);
            currentRequest = null;
        }
    }

}
