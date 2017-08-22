package com.ErMedia.user.resources.controllers;

import com.ErMedia.LogicModules.LogicComponents.Number;
import com.ErMedia.LogicModules.LogicComponents.User;
import com.ErMedia.user.ClientHandlers.Client;
import com.ErMedia.user.WelcomePage;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    private JFXTextField username;

    @FXML
    private AnchorPane loginScreen;

    private static Client client;
    private static User user;
    private static Number number;

    private static Stage registerStage;
    private static Stage userNumberListStage;


    private static RegisterController registerController;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MainController.user = user;
    }

    public static Stage getUserNumberListStage() {
        return userNumberListStage;
    }

    public static void setUserNumberListStage(Stage userNumberListStage) {
        MainController.userNumberListStage = userNumberListStage;
    }

    public static Number getNumber() {
        return number;
    }

    public static void setNumber(Number number) {
        MainController.number = number;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new Client(this);
        user = null;
    }

    @FXML
    private void loginHandler(ActionEvent event){
        if(!username.getText().isEmpty())
            client.getClientconnection().sendMessage("login " + username.getText());
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty Warning");
            alert.setHeaderText(null);
            alert.setContentText("its Empty!!! ?? ?? ?? -______-");
            alert.showAndWait();
        }
    }

    @FXML
    private void registerHandler(ActionEvent event){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    setRegisterStage(new Stage());
                    Parent root = FXMLLoader.load(getClass().getResource("/com/ErMedia/user/resources/fxmls/RegistrationScreen.fxml"));
                    getRegisterStage().setTitle("Registration Screen");
                    Scene scene = new Scene(root, 219, 134);
                    //        scene.getStylesheets().add(getClass().getResource("/sample/Css/style.css").toExternalForm());
                    getRegisterStage().setScene(scene);
                    getRegisterStage().show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //if user exist in server ,get user and create the numberView
    public void goToNumbersPage(User user){
        //TODO
        this.user = user;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    userNumberListStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/com/ErMedia/user/resources/fxmls/UserNumberScreen.fxml"));
                    userNumberListStage.setTitle("Numbers Screen");
                    Scene scene = new Scene(root, 245, 340);
                //  scene.getStylesheets().add(getClass().getResource("/sample/Css/style.css").toExternalForm());
                    userNumberListStage.setScene(scene);
                    userNumberListStage.show();
                    WelcomePage.getpS().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //if user not exist in server
    public void usernameNotExist(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User not Found");
                alert.setHeaderText(null);
                alert.setContentText("You can Register now.");
                alert.showAndWait();
            }
        });

    }


    public static Client getClient() {
        return client;
    }

    public static void setClient(Client c) {
        client = c;
    }

    public static Stage getRegisterStage() {
        return registerStage;
    }

    public static RegisterController getRegisterController() {
        return registerController;
    }

    public static void setRegisterController(RegisterController registerController) {
        MainController.registerController = registerController;
    }

    public static void setRegisterStage(Stage registerStage) {
        MainController.registerStage = registerStage;
    }
}
