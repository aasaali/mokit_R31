package com.example.mokit_r31;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MuokkaaVaraustaController {


    @FXML
    private TextField tfVarausId;
    @FXML
    private TextField tfAsiakasId;
    @FXML
    private TextField tfMokkiId;
    @FXML
    private DatePicker dpAlkupvm;
    @FXML
    private DatePicker dpLoppupvm;
    @FXML
    private Button tallennaButton;

    private Varaus varaus;

    Tietokanta tietokanta = new Tietokanta();


    public void setVaraus(Varaus varaus) {
        this.varaus = varaus;
        // Asetetaan varauksen tiedot tekstikenttiin
        tfVarausId.setText(String.valueOf(varaus.getVarausId()));
        tfAsiakasId.setText(String.valueOf(varaus.getAsiakasId()));
        tfMokkiId.setText(String.valueOf(varaus.getMokkiId()));
        dpAlkupvm.setValue(varaus.getVarattuAlkupvm().toLocalDate());
        dpLoppupvm.setValue(varaus.getVarattuLoppupvm().toLocalDate());
    }


    @FXML
    private void tallennaButton() {
        int varausId = Integer.parseInt(tfVarausId.getText());
        int asiakas = Integer.parseInt(tfAsiakasId.getText());
        int mokki = Integer.parseInt(tfMokkiId.getText());
        LocalDateTime alkupaiva = dpAlkupvm.getValue().atStartOfDay();
        LocalDateTime loppupaiva = dpLoppupvm.getValue().atStartOfDay();
        Varaus uusiVaraus = new Varaus(asiakas, mokki, alkupaiva, loppupaiva);

        // Asetetaan varauksen id ennen tietokantaan tallentamista
        uusiVaraus.setVarausId(varausId);

        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            hallinta.muokkaaVarausta(uusiVaraus);
            // Näytetään tallennusviesti
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Varauksen muokkaus");
            alert.setHeaderText(null);
            alert.setContentText("Varauksen muokkaus onnistui.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            // Näytetään virheilmoitus, jos tallentaminen epäonnistuu
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Varauksen muokkaus");
            alert.setHeaderText(null);
            alert.setContentText("Varauksen muokkaus epäonnistui.");
            alert.showAndWait();
        }
        // Suljetaan ikkuna tallennuksen jälkeen
        Stage stage = (Stage) tallennaButton.getScene().getWindow();
        stage.close();
    }

    private void initialize() {

        tallennaButton.setOnAction(e -> {
            tallennaButton();
        });
    }
}