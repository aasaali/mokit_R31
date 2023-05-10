package com.example.mokit_r31;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;


public class AlueetController {

    @FXML private Button BtTallenna;
    @FXML private TextField TxAlueNimi;
    @FXML private TextField tfPostinro;
    @FXML private TextField tfPostitoimipaikka;

    Tietokanta tietokanta = new Tietokanta();
        @FXML
        public void BtTallenna (ActionEvent event){
            String nimi = TxAlueNimi.getText();
            String postinro = tfPostinro.getText();
            String postitoimipk = tfPostitoimipaikka.getText();

            Alue uusiAlue = new Alue(nimi);
            Posti uusiPosti = new Posti(postinro, postitoimipk);
            AlueidenHallinta aluehallinta = new AlueidenHallinta(tietokanta);
            PostiHallinta postiHallinta = new PostiHallinta(tietokanta);
            try {
                aluehallinta.lisaaAlueenTiedot(uusiAlue);
                postiHallinta.tallennaPosti(uusiPosti);
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
