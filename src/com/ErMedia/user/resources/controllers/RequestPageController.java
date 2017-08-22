package com.ErMedia.user.resources.controllers;

import com.jfoenix.controls.JFXListView;
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
public class RequestPageController implements Initializable{

    @FXML
    private JFXListView requestLv;

    private ObservableList<String> observableRequestList;

    private String currentRequest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        observableRequestList = FXCollections.observableArrayList();
        for (int i = 0; i < ChatRoomController.getUser().getRequests().size(); i++) {
            if(ChatRoomController.getUser().getRequests().get(i).getDist().equals(ChatRoomController.getUser().getName()))
                observableRequestList.add(ChatRoomController.getUser().getRequests().get(i).getSrc());
        }
        requestLv.setItems(observableRequestList);
        requestLv.setOnMouseClicked(e -> {
            currentRequest = (String)requestLv.getSelectionModel().getSelectedItem();
            System.out.println(currentRequest);
        });
    }

    @FXML
    private void rejectButtonHandler(ActionEvent actionEvent){
        if(currentRequest == null){
            return;
        }
        observableRequestList.remove(currentRequest);
        for (int i = 0; i < ChatRoomController.getUser().getRequests().size() ; i++) {
            if(ChatRoomController.getUser().getRequests().get(i).getSrc().equals(currentRequest)){
                ChatRoomController.getUser().getRequests().remove(i);
            }
        }
        MainController.getClient().getClientconnection().sendMessage("rejectRequest " + currentRequest);
        currentRequest = null;
    }

    @FXML
    private void acceptButtonHandler(ActionEvent actionEvent){
        if(currentRequest == null){
            return;
        }
        observableRequestList.remove(currentRequest);
        for (int i = 0; i < ChatRoomController.getUser().getRequests().size() ; i++) {
            if(ChatRoomController.getUser().getRequests().get(i).getSrc().equals(currentRequest)){
                ChatRoomController.getUser().getRequests().remove(i);
            }
        }
        MainController.getClient().getClientconnection().sendMessage("acceptRequest " + currentRequest);
        currentRequest = null;
    }


}
