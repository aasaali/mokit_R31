package com.example.mokit_r31;

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
public class MuokkaaAsiakastaController {

    @FXML private TextField idTextField;
    @FXML private ComboBox<String> cbPostinumero;
    @FXML private TextField etunimiTextField;
    @FXML private TextField sukunimiTextField;
    @FXML private TextField lahiosoiteTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField puhelinnroTextField;
    @FXML private Button tallennaButton;

    private Asiakas asiakas;
    Tietokanta tietokanta;
    AsiakasHallinta asiakasHallinta = new AsiakasHallinta(tietokanta);
    public void setAsiakas(Asiakas asiakas) {
        this.asiakas = asiakas;
        naytaAsiakkaanTiedot();
    }

    @FXML
    private void initialize() {

        tallennaButton.setOnAction(e -> tallennaButton());
    }
    @FXML
    private void tallennaButton() {

        try {
            // Tallenna käyttäjän tekemät muutokset tietokantaan
            System.out.println(asiakas);
            paivitaAsiakkaantiedot();
            asiakasHallinta.paivitaAsiakas(asiakas);

            // Sulje ikkuna, jos tallennus onnistui
            Stage stage = (Stage) tallennaButton.getScene().getWindow();
            stage.close();
            System.out.println("Muokattu asiakas tallennettu tietokantaan");
        } catch (SQLException ex) {
            // Näytä virheilmoitus dialogilla, jos tallennus epäonnistui
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Virhe");
            alert.setHeaderText("Asiakkaan tallentaminen epäonnistui");
            alert.setContentText("Tapahtui virhe tallennettaessa muutoksia tietokantaan:\n" + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void paivitaAsiakkaantiedot() {
        asiakas.setPostinro(cbPostinumero.getSelectionModel().getSelectedItem());
        asiakas.setEtunimi(etunimiTextField.getText());
        asiakas.setSukunimi(sukunimiTextField.getText());
        asiakas.setLahiosoite(lahiosoiteTextField.getText());
        asiakas.setEmail(emailTextField.getText());
        asiakas.setPuhelinnro(puhelinnroTextField.getText());
    }
    @FXML
    private void naytaAsiakkaanTiedot() {
        List<String> postinumerot = asiakasHallinta.haePostinumerot();
        cbPostinumero.getItems().addAll(postinumerot);

        idTextField.setText(String.valueOf(asiakas.getAsiakasId()));
        etunimiTextField.setText(asiakas.getEtunimi());
        sukunimiTextField.setText(asiakas.getSukunimi());
        lahiosoiteTextField.setText(asiakas.getLahiosoite());
        emailTextField.setText(asiakas.getEmail());
        puhelinnroTextField.setText(asiakas.getPuhelinnro());
    }
}
