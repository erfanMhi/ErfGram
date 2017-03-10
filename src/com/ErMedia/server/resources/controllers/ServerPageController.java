package com.ErMedia.server.resources.controllers;

import com.ErMedia.server.ServerHandlers.Server;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Created by root on 1/19/17.
 */
public class ServerPageController {

    @FXML
    private JFXTextArea serverLogTA;

    @FXML
    private JFXButton connectionButton;

    private Server server ;

    @FXML
    private void runServer(ActionEvent actionEvent){
        serverLogTA.setText("ServerHandlers is Started");
        connectionButton.setText("Disconnect");
        connectionButton.setOnAction(e -> {
            stopServer(e);
        });
        server = new Server(4200,serverLogTA);
        server.start();
    }

    private void stopServer(ActionEvent actionEvent){
        connectionButton.setText("Connect");
        connectionButton.setOnAction(e -> {
            runServer(e);
        });
        server.stopServer();
        try {
            server.getServerSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverLogTA.setText("no conncetion");

    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
