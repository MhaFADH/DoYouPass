package com.doyoupass.doyoupass;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.*;
import java.sql.*;
import java.io.IOException;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root, 1200, 800);
        scene.setFill(Color.web("#3c81bc"));
        primaryStage.setTitle("Pepal Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(0);
        });

    }

    public void mainScreen(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(root, 1200, 800);
        scene.setFill(Color.web("#3c81bc"));
        primaryStage.setTitle("Pepal");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(0);
        });

    }





    public static void main(String[] args) {
        launch();
    }
}