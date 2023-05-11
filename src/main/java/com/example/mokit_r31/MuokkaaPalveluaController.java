package com.example.mokit_r31;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

/** Hallinnoi muokkaa asiakasta-ikkunaa ja sen toiminnallisuuksia. Ikkunassa käyttäjä voi muuttaa asiakkaan tietoja,
 * ja lähettää ne tietokantaan. Virheilmoitus, jos tiedot ovat virheellisiä.
 */
public class MuokkaaPalveluaController {

    @FXML private TextField palveluIDTf;

    @FXML private TextField alueIDTf;

    @FXML private TextField palveluAlvTf;

    @FXML
    private TextField palveluHintaTf;

    @FXML
    private TextField palveluKuvausTf;

    @FXML
    private TextField palveluNimiTf;

    @FXML
    private TextField palveluTyyppiTf;

    @FXML
    private Button tallennaPalveluBt;

    @FXML

    private Palvelu palvelu;
    Tietokanta tietokanta;
    PalvelujenHallinta palvelujenHallinta = new PalvelujenHallinta(tietokanta);
    public void setPalvelu(Palvelu palvelu) {
        this.palvelu = palvelu;
        naytaPalvelunTiedot();
    }
// Luokassa on kaksi tapaa toteuttaa buttonin onAction

    @FXML
    void tallennaMuokkausButton(ActionEvent event) {

    }
    @FXML
    private void initialize() {

        tallennaPalveluBt.setOnAction(e -> tallennaPalveluBt());
    }
    @FXML
    private void tallennaPalveluBt() {
        try {
            // Tallenna käyttäjän tekemät muutokset tietokantaan
            System.out.println(palvelu);
            paivitaPalveluntiedot();
            PalveluController.tallennaPalvelu(palvelu);

            // Sulje ikkuna, jos tallennus onnistui
            Stage stage = (Stage) tallennaPalveluBt.getScene().getWindow();
            stage.close();
            System.out.println("Muokattu palvelu tallennettu tietokantaan");
        } catch (SQLException ex) {
            // Näytä virheilmoitus dialogilla, jos tallennus epäonnistui
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Virhe");
            alert.setHeaderText("Palvelun tallentaminen epäonnistui");
            alert.setContentText("Tapahtui virhe tallennettaessa muutoksia tietokantaan:\n" + ex.getMessage());
            alert.showAndWait();
        }
    }
    private void paivitaPalveluntiedot() {
        palvelu.setAlueId(Integer.parseInt(alueIDTf.getText()));
        palvelu.setNimi(palveluNimiTf.getText());
        palvelu.setTyyppi(Integer.parseInt(palveluTyyppiTf.getText()));
        palvelu.setKuvaus(palveluKuvausTf.getText());
        palvelu.setHinta(Integer.parseInt(palveluHintaTf.getText()));
        palvelu.setAlv(Integer.parseInt(palveluAlvTf.getText()));
    }
    @FXML
    private void naytaPalvelunTiedot() {

        palveluIDTf.setText((String.valueOf(palvelu.getId())));
        alueIDTf.setText((String.valueOf(palvelu.getAlueId())));
        palveluNimiTf.setText((palvelu.getNimi()));
        palveluTyyppiTf.setText(String.valueOf(palvelu.getAlueId()));
        palveluKuvausTf.setText(palvelu.getKuvaus());
        palveluHintaTf.setText(String.valueOf(palvelu.getHinta()));
        palveluAlvTf.setText(String.valueOf(palvelu.getAlv()));

    }
}
