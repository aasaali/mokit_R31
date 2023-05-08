package com.example.mokit_r31;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/** Mökkivarausjärjestelmä by R 31
 * UEF TKT OHJELMISTOTUOTANTO 2023
 * Authors: Aatu Saali, Amanda Rizvanov, Juuli Ahonen, Taija Miettinen
 */
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainIkkuna.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mökkivarausjärjestelmä by R31");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}