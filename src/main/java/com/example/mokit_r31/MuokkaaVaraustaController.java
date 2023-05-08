package com.example.mokit_r31;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @FXML
    private ListView varauksenPalvelutLv;
    private Varaus varaus;
    @FXML
    private ListView palvelutLv;
    @FXML
    private Button btPoistaPalvelu;
    @FXML
    private Button btLisaaPalvelu;

    Tietokanta tietokanta = new Tietokanta();

    private List<Palvelu> kaikkiPalvelut = new ArrayList<>();


    public void setVaraus(Varaus varaus) {
        this.varaus = varaus;
        // Asetetaan varauksen tiedot tekstikenttiin
        tfVarausId.setText(String.valueOf(varaus.getVarausId()));
        tfAsiakasId.setText(String.valueOf(varaus.getAsiakasId()));
        tfMokkiId.setText(String.valueOf(varaus.getMokkiId()));
        dpAlkupvm.setValue(varaus.getVarattuAlkupvm().toLocalDate());
        dpLoppupvm.setValue(varaus.getVarattuLoppupvm().toLocalDate());
        lataaPalvelut(varaus.getMokkiId());
        lisaaVarauksenPalvelut();
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

        // Lisätään varauksen palvelut
        ObservableList<Palvelu> valitutPalvelut = palvelutLv.getSelectionModel().getSelectedItems();
        for (Palvelu palvelu : valitutPalvelut) {
            int palveluId = palvelu.getId();
            int lkm = 1; // oletuksena yksi kappale valittua palvelua
            try {
                VaraustenHallinta.lisaaPalveluVaraukselle(varausId, palveluId, lkm);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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


    private void lataaPalvelut(int mokkiId) {
        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            kaikkiPalvelut = hallinta.getMokinPalvelut(mokkiId);
            palvelutLv.getItems().setAll(kaikkiPalvelut);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void lisaaVarauksenPalvelut() {
        // Haetaan varauksen palvelut tietokannasta
        try {
            List<Palvelu> varauksenPalvelut = VaraustenHallinta.getVarauksenPalvelut(varaus.getVarausId());
            // Lisätään palvelut listviewiin
            varauksenPalvelutLv.getItems().setAll(varauksenPalvelut);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void poistaValittuPalvelu() {
        ObservableList<Palvelu> valitutPalvelut = varauksenPalvelutLv.getSelectionModel().getSelectedItems();
        for (Palvelu palvelu : valitutPalvelut) {
            try {
                VaraustenHallinta.poistaPalveluVaraukselta(varaus.getVarausId(), palvelu.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Päivitetään varauksen palvelut listviewiin
        lisaaVarauksenPalvelut();
    }

    @FXML
    public void lisaaValittuPalvelu() {
        ObservableList<Palvelu> valitutPalvelut = palvelutLv.getSelectionModel().getSelectedItems();
        for (Palvelu palvelu : valitutPalvelut) {
            int palveluId = palvelu.getId();
            int lkm = 1; // oletuksena yksi kappale valittua palvelua
            try {
                VaraustenHallinta.lisaaPalveluVaraukselle(varaus.getVarausId(), palveluId, lkm);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Päivitetään varauksen palvelut listviewiin
        lisaaVarauksenPalvelut();
    }
    @FXML
    public void initialize() {
        tallennaButton.setOnAction(e -> {
            tallennaButton();
        });
        palvelutLv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }
}