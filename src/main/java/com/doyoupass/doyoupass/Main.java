package com.doyoupass.doyoupass;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.*;
import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root, 829, 473);
        scene.setFill(Color.web("#3c81bc"));
        LoginController controller = new LoginController();
        controller.setMainApp(this);
        this.primaryStage.setTitle("Login");
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(false);
        this.primaryStage.show();

        this.primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(0);
        });

    }

    public void mainScreen(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(root, 1124, 787);
        scene.setFill(Color.web("#3c81bc"));
        MainScreenController controller = new MainScreenController();
        controller.setMainApp(this);
        this.primaryStage.setTitle("DoYouPass");
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(false);
        this.primaryStage.show();

        this.primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(0);
        });

    }





    public static void main(String[] args) {
        launch();
    }
}