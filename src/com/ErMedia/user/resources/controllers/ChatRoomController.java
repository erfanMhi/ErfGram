package com.ErMedia.user.resources.controllers;

import com.ErMedia.LogicModules.LogicComponents.Message;
import com.ErMedia.LogicModules.LogicComponents.Number;
import com.ErMedia.LogicModules.LogicComponents.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by root on 1/11/17.
 */
public class ChatRoomController implements Initializable{

    private ObservableList<String> observableFriendList ;

    @FXML
    private JFXListView friendsLv ;

    @FXML
    private JFXTextArea messageTA;

    @FXML
    private TextField messageTF;

    @FXML
    private Label wellcomeLabel ;

    @FXML
    private VBox chatRoomScreen;

    @FXML
    private JFXButton propertyButton;

    private FileChooser fileChooser ;

    private Stage settingPageStage;

    private boolean isSettingPageOpen;
    private static User user ;
    private static Number number ;
    private static ArrayList<User> friends;

    private static String currentPvUser;
    private static Long currentPvNumber;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files" , "*.txt"),
                new FileChooser.ExtensionFilter("Image Files" , "*.png" , "*.jpg" , "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files" , "*.wav" , "*.mp3" , "*.aac"),
                new FileChooser.ExtensionFilter("All Files" ,"*.*")
        );
        currentPvUser = null;
        currentPvNumber = null;
        MainController.getClient().setChatRoomController(this);
        user = MainController.getUser();
        messageTA.setText("chat room : wellcome "+ user.getName());
        wellcomeLabel.setText("Wellcome " + user.getName() + "(" + number.getNumber() + ")");
        observableFriendList = FXCollections.observableArrayList();
        for (int i = 0; i < friends.size() ; i++) {
            for (int j = 0; j < friends.get(i).getNumbers().size() ; j++) {
                observableFriendList.add(friends.get(i).getName() + " " +friends.get(i).getNumbers().get(j).getNumber());
            }
        }
        friendsLv.setItems(observableFriendList);
        friendsLv.setOnMouseClicked(e -> {
            if(friendsLv.getSelectionModel().getSelectedItem() != null){
                String tmp = (String)friendsLv.getSelectionModel().getSelectedItem();
                currentPvUser = tmp.split(" ")[0];
                currentPvNumber = Long.parseLong(tmp.split(" ")[1]);
                for (int i = 0; i < number.getMessages().size() ; i++) {
                    if(number.getMessages().get(i).getDist() == currentPvNumber || number.getMessages().get(i).getSrc() == currentPvNumber){
                        messageTA.setText(number.getMessages().get(i).getMessage());
                        return;
                    }
                }
                messageTA.setText("No Message");
            }

        });

    }

    @FXML
    private void settingShower(ActionEvent actionEvent){
        try {
            if (!isSettingPageOpen) {
                settingPageStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/com/ErMedia/user/resources/fxmls/SettingPageScreen.fxml"));
                settingPageStage.setTitle("Setting");
                Scene scene = new Scene(root, 231, 361);
                // scene.getStylesheets().add(getClass().getResource("/sample/Css/style.css").toExternalForm());
                settingPageStage.setScene(scene);
                settingPageStage.show();
                isSettingPageOpen = true;
                settingPageStage.setOnCloseRequest(e -> {
                    isSettingPageOpen = false;
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void sendMessageHandler(ActionEvent actionEvent){
        System.out.println(messageTF.getText());
        if(currentPvNumber != null)
            if(!messageTF.getText().isEmpty()) {
//                Number tmpNumber = getNumber(currentPvNumber);
                Gson gson = new Gson();
                Type messageType = new TypeToken<Message>(){}.getType();
                if (!messageTA.getText().equals("No Message")) {
                    messageTA.appendText("\n" + user.getName() + " : " + messageTF.getText());
                    Message message = new Message(number.getNumber(), currentPvNumber, messageTA.getText());
                    //update user message
                    System.out.println("message created");
                    for (int i = 0; i < number.getMessages().size() ; i++) {
                        if(number.getMessages().get(i).getDist() == currentPvNumber || number.getMessages().get(i).getSrc() == currentPvNumber){
                            number.getMessages().set(i,message);
                            break;
                        }
                    }
                    System.out.println("message sent");
                    //update friends number
//                    int messageIndex = getIndexMessage(tmpNumber,number.getNumber());
//                    if(messageIndex != -1) {
//                        tmpNumber.getMessages().set(messageIndex,message);
//                    } else {
//                        System.out.println("Errrrrrrrore");
//                    }
                    MainController.getClient().getClientconnection().sendMessage("message " + gson.toJson(message,messageType));
                } else {
                    Message message = new Message(number.getNumber(), currentPvNumber, user.getName() + " : " + messageTF.getText());
                    messageTA.setText(user.getName() + " : " + messageTF.getText());
                    number.getMessages().add(message);
//                    if( tmpNumber != null){
//                        tmpNumber.getMessages().add(message);
//                    } else {
//                        System.out.println("Errrrrrrrore");
//                    }
                    MainController.getClient().getClientconnection().sendMessage("message " + gson.toJson(message,messageType));
                }
            }
        messageTF.setText("");

    }



    @FXML
    private void fileSenderButtonHandler(ActionEvent actionEvent) {
        File file;
        file = fileChooser.showOpenDialog(null);
        if(file != null) {
            try {
                MainController.getClient().getClientconnection().sendFile(file,currentPvUser,currentPvNumber);
                showNotification("sending file","file has successfully sent.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void messageRecieverHandler(Message message){
        //if user in this pv change TArea
        if(currentPvNumber !=null)
        if(currentPvNumber == message.getDist() || currentPvNumber == message.getSrc()){
            messageTA.setText(message.getMessage());
        }
        long distance ;
        if(message.getDist() == number.getNumber())
            distance = message.getSrc();
        else
            distance = message.getDist();
        //updating number messages
        boolean isMessageExist = false;
        for (int i = 0; i < number.getMessages().size(); i++) {
            if(number.getMessages().get(i).getDist() == distance ||
                    number.getMessages().get(i).getSrc() == distance){
                number.getMessages().set(i,message);
                isMessageExist = true;
                break;
            }
        }

        if(!isMessageExist)
            number.getMessages().add(message);

    }


    public Number getNumber(int number){
        for (int i = 0; i < friends.size() ; i++) {
            for (int j = 0; j < friends.get(i).getNumbers().size(); j++) {
                if(friends.get(i).getNumbers().get(j).getNumber() == number){
                    return friends.get(i).getNumbers().get(j);
                }
            }
        }
        return null;
    }

    public int getIndexMessage(Number dist,int src){
        for (int i = 0; i < dist.getMessages().size() ; i++) {
            if(dist.getMessages().get(i).getDist() == src || dist.getMessages().get(i).getSrc() == src){
                return  i;
            }
        }
        return -1;
    }
    public void showNotification(String title,String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(text);
                alert.showAndWait();
            }
        });
    }

    public void requestAcceptHandler(User user) {
        this.user.getFriends().add(user.getName());
        friends.add(user);
        for (int j = 0; j < user.getNumbers().size() ; j++) {
            observableFriendList.add(user.getName() + " " +user.getNumbers().get(j).getNumber());
        }

    }


    public Stage getsettingPageStage() {
        return settingPageStage;
    }

    public void setsettingPageStage(Stage settingPageStage) {
        this.settingPageStage = settingPageStage;
    }


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ChatRoomController.user = user;
    }

    public static Number getNumber() {
        return number;
    }

    public static void setNumber(Number number) {
        ChatRoomController.number = number;
    }

    public static ArrayList<User> getFriends() {
        return friends;
    }

    public static void setFriends(ArrayList<User> friends) {
        ChatRoomController.friends = friends;
    }

    public static String getCurrentPvUser() {
        return currentPvUser;
    }

    public static void setCurrentPvUser(String currentPvUser) {
        ChatRoomController.currentPvUser = currentPvUser;
    }

    public static long getCurrentPvNumber() {
        return currentPvNumber;
    }

    public static void setCurrentPvNumber(long currentPvNumber) {
        ChatRoomController.currentPvNumber = currentPvNumber;
    }


}
