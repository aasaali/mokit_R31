package com.example.mokit_r31;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class MokitLisaaController {
    @FXML
    private Button BtTallenna;
    @FXML
    private TextField TfMokinNimi;
    @FXML
    private TextField TfHlo;
    @FXML
    private TextField TfMokinId;
    @FXML
    private TextField TfHinta;
    @FXML
    private TextField TfAlueId;
    @FXML
    private TextField TfKatuosoite;
    @FXML
    private TextField TfPostiNro;
    @FXML
    private TextArea TfMokinKuvaus;
    @FXML
    private TextArea TfMokinVarustelu;

    Tietokanta tietokanta = new Tietokanta();
    @FXML
        private void BtTallenna (ActionEvent event){
            String nimi = TfMokinNimi.getText();
            String kuvaus = TfMokinKuvaus.getText();
            String osoite = TfKatuosoite.getText();
            String varustelu = TfMokinVarustelu.getText();
            int hlo = Integer.parseInt(TfHlo.getText());
            String postinumero = TfPostiNro.getText();
            double hinta = Double.parseDouble(TfHinta.getText());
            int alueId = Integer.parseInt(TfAlueId.getText());

            Mokki uusiMokki = new Mokki(alueId, postinumero, nimi, osoite, hinta, kuvaus, hlo, varustelu);
            try {
                MokkienHallinta hallinta = new MokkienHallinta(tietokanta);
                hallinta.lisaaMokki(uusiMokki);
                BtTallenna.setText("Mökin tiedot tallennettu!");
                System.out.println("tallennusOnnistui");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

