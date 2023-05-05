package com.example.mokit_r31;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;

public class MokitController {


        @FXML
        private TextField TfMokki;
        @FXML
        private Button BtHaeMokki;
        @FXML
        private TextField TfAlue;
        @FXML
        private Button BtHaeAlue;
        @FXML
        private Button BtLisaaMokki;
        @FXML
        private Button BtLisaaAlue;
        @FXML
        private ListView<Mokki> MokitJaAlueetLista;

        public void initialize(){
        BtLisaaMokki.setOnAction(event ->{
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LisaaMokki.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        BtLisaaAlue.setOnAction(event ->{
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LisaaAlue.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            }

    }

