package com.ErMedia.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by root on 1/19/17.
 */
public class ServerPage extends Application {

    private  static Stage serverStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        serverStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/com/ErMedia/server/resources/fxmls/ServerScreen.fxml"));
        primaryStage.setTitle("ServerHandlers");
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
