package com.example.mokit_r31;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

// Muistiinpano: muokkausikkunaan vielä asiakas-id näkyville

public class MuokkaaAsiakastaController {

    @FXML
    private TextField idTextField;
    @FXML
    private TextField postinroTextField;
    @FXML
    private TextField etunimiTextField;
    @FXML
    private TextField sukunimiTextField;
    @FXML
    private TextField lahiosoiteTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField puhelinnroTextField;
    @FXML
    private Button tallennaButton;

    private AsiakasHallinta asiakasHallinta;

    private Asiakas asiakas;
    Tietokanta tietokanta = new Tietokanta();
    public void setAsiakas(Asiakas asiakas) {
        this.asiakas = asiakas;
        naytaAsiakkaanTiedot();
    }

    public void initialize() {

        if (asiakas == null) {
            return;
        }

        // Tässä on jokin virhe, eikä tähän koodin osaan koskaan päästä :P

        idTextField.setText(String.valueOf(asiakas.getAsiakasId()));
        postinroTextField.setText(asiakas.getPostinro());
        etunimiTextField.setText(asiakas.getEtunimi());
        sukunimiTextField.setText(asiakas.getSukunimi());
        lahiosoiteTextField.setText(asiakas.getLahiosoite());
        emailTextField.setText(asiakas.getEmail());
        puhelinnroTextField.setText(asiakas.getPuhelinnro());

        tallennaButton.setOnAction(event -> {
            try {
                // Tallenna käyttäjän tekemät muutokset tietokantaan
                asiakasHallinta.paivitaAsiakas(asiakas);

                // Sulje ikkuna, jos tallennus onnistui
                Stage stage = (Stage) tallennaButton.getScene().getWindow();
                stage.close();
            } catch (SQLException ex) {
                // Näytä virheilmoitus dialogilla, jos tallennus epäonnistui
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Virhe");
                alert.setHeaderText("Asiakkaan tallentaminen epäonnistui");
                alert.setContentText("Tapahtui virhe tallennettaessa muutoksia tietokantaan:\n" + ex.getMessage());
                alert.showAndWait();
            }
        });

    }

    public void setAsiakasHallinta(AsiakasHallinta asiakasHallinta) {
        this.asiakasHallinta = asiakasHallinta;
    }
    @FXML
    private void naytaAsiakkaanTiedot() {
        etunimiTextField.setText(asiakas.getEtunimi());
        sukunimiTextField.setText(asiakas.getSukunimi());
        lahiosoiteTextField.setText(asiakas.getLahiosoite());
        postinroTextField.setText(asiakas.getPostinro());
        emailTextField.setText(asiakas.getEmail());
        puhelinnroTextField.setText(asiakas.getPuhelinnro());
    }
}
