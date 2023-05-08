package com.example.mokit_r31;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AlueetController {

    @FXML
    private TextField TxAlueId;
    @FXML
    private Button BtTallenna;
    @FXML
    private TextField TxAlueNimi;

    Tietokanta tietokanta = new Tietokanta();
        @FXML
        public void BtTallenna (ActionEvent event){
            String nimi = TxAlueNimi.getText();
            int alueId = Integer.parseInt(TxAlueId.getText());

            Alue uusiAlue = new Alue(nimi, alueId);
            AlueidenHallinta hallinta = new AlueidenHallinta(tietokanta);
            hallinta.lisaaAlueenTiedot(uusiAlue);
            BtTallenna.setText("Alueen tiedot tallennettu!");
            System.out.println("tallennusOnnistui");

        }


}
