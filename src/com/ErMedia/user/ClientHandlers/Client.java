package com.ErMedia.user.ClientHandlers;

import com.ErMedia.user.resources.controllers.*;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by root on 1/10/17.
 */
public class Client extends Thread{

    private Socket socket ;
    private ClientConnection clientConnection;
    private MainController mainController;
    private UserNumberListController userNumberListController;
    private ChatRoomController chatRoomController ;
    private SettingPageController settingPageController;
    private LevelSearchPageController levelSearchPageController;
    private ShowPathPageController showPathPageController;

    public static void main(String[] args) {
        new Client(null);
    }

    public Client(MainController mainController){
        try {
            this.mainController = mainController;
            socket = new Socket("127.0.0.1" , 4200);
            clientConnection = new ClientConnection(socket,this);
            clientConnection.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientConnection.sendMessage("im here");
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ClientConnection getClientconnection() {
        return clientConnection;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public UserNumberListController getUserNumberListController() {
        return userNumberListController;
    }

    public void setUserNumberListController(UserNumberListController userNumberListController) {
        this.userNumberListController = userNumberListController;
    }

    public ChatRoomController getChatRoomController() {
        return chatRoomController;
    }

    public void setChatRoomController(ChatRoomController chatRoomController) {
        this.chatRoomController = chatRoomController;
    }

    public SettingPageController getSettingPageController() {
        return settingPageController;
    }

    public void setSettingPageController(SettingPageController settingPageController) {
        this.settingPageController = settingPageController;
    }

    public LevelSearchPageController getLevelSearchPageController() {
        return levelSearchPageController;
    }

    public void setLevelSearchPageController(LevelSearchPageController levelSearchPageController) {
        this.levelSearchPageController = levelSearchPageController;
    }

    public ShowPathPageController getShowPathPageController() {
        return showPathPageController;
    }

    public void setShowPathPageController(ShowPathPageController showPathPageController) {
        this.showPathPageController = showPathPageController;
    }
}
