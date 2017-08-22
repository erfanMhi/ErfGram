package com.ErMedia.user;
/**
 * Created by root on 1/9/17.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WelcomePage extends Application {

    private  static Stage pS;

    @Override
    public void start(Stage primaryStage) throws Exception{
        pS = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/com/ErMedia/user/resources/fxmls/WlcPageScreen.fxml"));
        primaryStage.setTitle("Login");
        Scene scene = new Scene(root, 278, 154);
        scene.getStylesheets().add(getClass().getResource("/com/ErMedia/user/resources/CSS/WlcPageStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getpS() {
        return pS;
    }

    public static void setpS(Stage primaryStage) {
        pS = primaryStage;
    }
}