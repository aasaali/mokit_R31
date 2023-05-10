package com.example.mokit_r31;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class MuokkaMokkiaController {

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

    @FXML
    private Button BtTallenna;
    private Mokki mokki;
    public void setMokki(Mokki mokki) {
        this.mokki = mokki;
        naytaMokinTiedot();

    }
    Tietokanta tietokanta = new Tietokanta();
    MokkienHallinta mokkienHallinta = new MokkienHallinta(tietokanta);
    public void alustaMokkienhallinta(MokkienHallinta mokkienHallinta){
        this.mokkienHallinta = mokkienHallinta;
    }
    @FXML
    private void initialize() {

        BtTallenna.setOnAction(e -> {
            BtTallenna();
        });
    }
    @FXML
    private void BtTallenna() {
        System.out.println("Testi");
        // Tallenna käyttäjän tekemät muutokset tietokantaan
        paivitaMokintiedot();
        mokkienHallinta.muokkaaMokkia(mokki);

        // Sulje ikkuna, jos tallennus onnistui
        Stage stage = (Stage) BtTallenna.getScene().getWindow();
        stage.close();
    }

    private void paivitaMokintiedot() {
        mokki.setNimi(TfMokinNimi.getText());
        mokki.setHlo(Integer.parseInt(TfHlo.getText()));
        mokki.setMokkiId(Integer.parseInt(TfMokinId.getText()));
        mokki.setHinta(Double.parseDouble(TfHinta.getText()));
        mokki.setAlueId(Integer.parseInt(TfAlueId.getText()));
        mokki.setOsoite(TfKatuosoite.getText());
        mokki.setPostiNro(TfPostiNro.getText());
        mokki.setKuvaus(TfMokinKuvaus.getText());
        mokki.setVarustelu(TfMokinVarustelu.getText());
    }


    public void setMokkienHallinta(MokkienHallinta mokkienHallinta) {

        this.mokkienHallinta = mokkienHallinta;
    }
    @FXML
    private void naytaMokinTiedot() {
        TfMokinNimi.setText(String.valueOf(mokki.getNimi()));
        TfHlo.setText(String.valueOf(mokki.getHlo()));
        TfMokinId.setText(String.valueOf(mokki.getMokkiId()));
        TfHinta.setText(String.valueOf(mokki.getHinta()));
        TfAlueId.setText(String.valueOf(mokki.getAlueId()));
        TfKatuosoite.setText(mokki.getOsoite());
        TfPostiNro.setText(String.valueOf(mokki.getPostiNro()));
        TfMokinKuvaus.setText(mokki.getKuvaus());
        TfMokinVarustelu.setText(mokki.getVarustelu());
    }
}


