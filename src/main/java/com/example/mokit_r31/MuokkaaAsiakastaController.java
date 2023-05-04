package com.example.mokit_r31;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

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

    public void initialize() {
        idTextField.setText(String.valueOf(asiakas.getAsiakasId()));
        postinroTextField.setText(asiakas.getPostinro());
        etunimiTextField.setText(asiakas.getEtunimi());
        sukunimiTextField.setText(asiakas.getSukunimi());
        lahiosoiteTextField.setText(asiakas.getLahiosoite());
        emailTextField.setText(asiakas.getEmail());
        puhelinnroTextField.setText(asiakas.getPuhelinnro());
    }

    public void setAsiakasHallinta(AsiakasHallinta asiakasHallinta) {
        this.asiakasHallinta = asiakasHallinta;
    }

    public void setAsiakas(Asiakas asiakas) {
        this.asiakas = asiakas;
    }

    @FXML
    private void tallenna() {
        try {
            // Tallenna käyttäjän tekemät muutokset tietokantaan
            asiakasHallinta.paivitaAsiakas(asiakas);

            // Sulje ikkuna, jos tallennus onnistui
            Stage stage = (Stage) tallennaButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            // Näytä virheilmoitus dialogilla, jos tallennus epäonnistui
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Virhe");
            alert.setHeaderText("Asiakkaan tallentaminen epäonnistui");
            alert.setContentText("Tapahtui virhe tallennettaessa muutoksia tietokantaan:\n" + e.getMessage());
            alert.showAndWait();
        }
    }

}
