package com.ErMedia.user.resources.controllers;

import com.ErMedia.LogicModules.DataStructure.ArrayListI;
import com.ErMedia.LogicModules.LogicComponents.Number;
import com.ErMedia.LogicModules.LogicComponents.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by root on 1/11/17.
 */
public class UserNumberListController implements Initializable {

    @FXML
    private JFXListView numbersLview;

    @FXML
    private JFXTextField addNumbetTF;

    @FXML
    private BorderPane numberShowerScreen;

    @FXML
    private JFXButton addNumberButton;
    private ObservableList<Long> listViewNumbers ;

    private static User user ;

    private Stage chatRoomStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.getClient().setUserNumberListController(this);
        user = MainController.getUser();
        Object[] nums = user.getNumbers().toArray();
        Long[] numbers = ArrayListI.toIntegerArray(nums);
        listViewNumbers = FXCollections.observableArrayList();
        listViewNumbers.addAll(numbers);
        numbersLview.setItems(listViewNumbers);
        numbersLview.setOnMouseClicked(e -> {
            if(numbersLview.getSelectionModel().getSelectedItem() != null) {
                long clickedNumber = (long) numbersLview.getSelectionModel().getSelectedItem();
                System.out.println(clickedNumber);
                for (int i = 0; i < user.getNumbers().size(); i++) {
                    if(user.getNumbers().get(i).getNumber() == clickedNumber) {
                        ChatRoomController.setNumber(user.getNumbers().get(i));
                        break;
                    }
                }

                MainController.getClient().getClientconnection().sendMessage("numberLogin " + clickedNumber);
            }
        });
    }

    //turn my arrayObject to Integer Array


    @FXML
    private void addNumberHandler(ActionEvent actionEvent) {
        if(!addNumbetTF.getText().isEmpty()) {
            try {
                //check if its a integer
                Long.parseLong(addNumbetTF.getText());
                //tell server to update user numbers
                MainController.getClient().getClientconnection().sendMessage("addNumber " + addNumbetTF.getText());

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Wrong Value");
                alert.setHeaderText(null);
                alert.setContentText("its not a number!!! ?? ?? ?? -______-");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty Warning");
            alert.setHeaderText(null);
            alert.setContentText("its Empty!!! ?? ?? ?? -______-");
            alert.showAndWait();
        }
    }


    public void duplicateNumberWarner() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Duplicated Number");
                alert.setHeaderText(null);
                alert.setContentText("this number is duplicated plz enter another number.");
                alert.showAndWait();
            }
        });
    }

    public void updateListview(String post) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Number tmpNumber = new Number(Long.parseLong(post),user.getName());
                //change user nubmber in every class
                user.addNumber(tmpNumber);
                MainController.setUser(user);
                MainController.getClient().getClientconnection().setUser(user);

                //add changes too listview
                listViewNumbers.add(tmpNumber.getNumber());
            }
        });

    }

    public void chatRoomCreator(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    chatRoomStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/com/ErMedia/user/resources/fxmls/ChattingRoomScreen.fxml"));
                    chatRoomStage.setTitle("Chat Room");
                    Scene scene = new Scene(root, 600, 400);
                    // scene.getStylesheets().add(getClass().getResource("/sample/Css/style.css").toExternalForm());
                    chatRoomStage.setScene(scene);
                    chatRoomStage.show();
                    MainController.getUserNumberListStage().close();
                    chatRoomStage.setOnCloseRequest(e -> {
                        MainController.getClient().getClientconnection().terminate();
                    });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public Stage getChatRoomStage() {
        return chatRoomStage;
    }

    public void setChatRoomStage(Stage chatRoomStage) {
        this.chatRoomStage = chatRoomStage;
    }
}
