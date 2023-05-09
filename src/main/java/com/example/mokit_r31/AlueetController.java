package com.example.mokit_r31;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

            Alue uusiAlue = new Alue(nimi);
            AlueidenHallinta hallinta = new AlueidenHallinta(tietokanta);
            try {
                hallinta.lisaaAlueenTiedot(uusiAlue);
                BtTallenna.setText("Alueen tiedot tallennettu!");
                System.out.println("tallennus onnistui");
                // Sulje ikkuna, jos tallennus onnistui
                Stage stage = (Stage) BtTallenna.getScene().getWindow();
                stage.close();
            }
            catch (Exception e) {
                System.out.println("Tallennus epäonnistui");
            }

        }

}
