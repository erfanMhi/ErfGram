package com.ErMedia.user.resources.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by root on 1/12/17.
 */
public class SettingPageController implements Initializable{

    private Stage addFriendPageStage;
    private Stage requestPageStage;
    private Stage levelSearchPageStage;
    private Stage showPathPageStage;
    private boolean levelSearchPageIsCreated ;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainController.getClient().setSettingPageController(this);
        levelSearchPageIsCreated = false;
    }


    @FXML
    private void requestButtonHandler(ActionEvent actionEvent){
        MainController.getClient().getClientconnection().sendMessage("getRequest nothing");

    }

    @FXML
    private void addFriendButtonHandler(ActionEvent actionEvent){
        try {
            addFriendPageStage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/com/ErMedia/user/resources/fxmls/AddFriendsPageScreen.fxml"));
            addFriendPageStage.setTitle("Add Friend Screen");
            Scene scene = new Scene(root, 286, 135);
            // scene.getStylesheets().add(getClass().getResource("/sample/Css/style.css").toExternalForm());
            addFriendPageStage.setScene(scene);
            addFriendPageStage.show();
        }
         catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void levelSearchButtonHandler(ActionEvent actionEvent){
        levelSearchPageCreator();
        MainController.getClient().getClientconnection().sendMessage("levelSearch 2");
    }

    @FXML
    private void showPathButtonHandler(ActionEvent actionEvent){
        try {
            showPathPageStage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/com/ErMedia/user/resources/fxmls/ShowPathPageScreen.fxml"));
            showPathPageStage.setTitle("Show Path Page");
            Scene scene = new Scene(root, 462, 346);
            // scene.getStylesheets().add(getClass().getResource("/sample/Css/style.css").toExternalForm());
            showPathPageStage.setScene(scene);
            showPathPageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addGroupButtonHandler(ActionEvent actionEvent){

    }


    private void levelSearchPageCreator(){

        try {
            levelSearchPageStage = new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/com/ErMedia/user/resources/fxmls/LevelSearchPageScreen.fxml"));
            levelSearchPageStage.setTitle("Level Search Page");
            Scene scene = new Scene(root, 313, 400);
            // scene.getStylesheets().add(getClass().getResource("/sample/Css/style.css").toExternalForm());
            levelSearchPageStage.setScene(scene);
            levelSearchPageStage.show();
            levelSearchPageIsCreated = true;
            levelSearchPageStage.setOnCloseRequest(e -> {
                levelSearchPageIsCreated = false;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createRequestPage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    requestPageStage = new Stage();
                    Parent root = null;
                    root = FXMLLoader.load(getClass().getResource("/com/ErMedia/user/resources/fxmls/RequestPageScreen.fxml"));
                    requestPageStage.setTitle("Add Friend Screen");
                    Scene scene = new Scene(root, 317, 400);
                    // scene.getStylesheets().add(getClass().getResource("/sample/Css/style.css").toExternalForm());
                    requestPageStage.setScene(scene);
                    requestPageStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public Stage getRequestPageStage() {
        return requestPageStage;
    }

    public void setRequestPageStage(Stage requestPageStage) {
        this.requestPageStage = requestPageStage;
    }



    public boolean isLevelSearchPageIsCreated() {
        return levelSearchPageIsCreated;
    }

    public void setLevelSearchPageIsCreated(boolean levelSearchPageIsCreated) {
        this.levelSearchPageIsCreated = levelSearchPageIsCreated;
    }

    public Stage getShowPathPageStage() {
        return showPathPageStage;
    }

    public void setShowPathPageStage(Stage showPathPageStage) {
        this.showPathPageStage = showPathPageStage;
    }
}
