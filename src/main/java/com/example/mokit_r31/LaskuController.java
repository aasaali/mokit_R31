package com.example.mokit_r31;
import com.itextpdf.text.DocumentException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Optional;

/** Laskutusta käsittelevä controller.
 * Käsittelee laskuja ja varauksia listoina, jotka haetaan sql-tietokannasta.
 * Sisältää toiminnot: päivitä listat, luo uusi: tallentaa tietokantaan, poista tietokannasta,
 * luo ja tallenna PDF-tiedostoon, merkitse tietokantaan maksetuksi tai ei maksetuksi.
 * Laskun lähetys sähköpostilla voidaan lisätä, kun yrityksellä on sähköpostipalvelin.
 */

// Tämän luokan rakenne aiheuttaa tarpeettomia "is never used"-varoituksia, jonka vuoksi:
@SuppressWarnings(value = "unused")

public class LaskuController {
    @FXML private ListView<Varaus> laskutaVarausLw;
    @FXML private ListView<Lasku> laskuLw;
    @FXML private Button btLahetaLasku;
    @FXML private Button btTallennaPDF;
    @FXML private Button buttonPaivita;
    @FXML private Button buttonLuoUusi;
    @FXML private Button buttonPoista;
    @FXML private TextArea taLaskunTiedot;
    @FXML private TextField tfEmailosoite;
    @FXML private TextField tfTiedostonimi;
    @FXML private CheckBox checkMaksettu;
    @FXML private CheckBox checkEiMaksettu;
    Tietokanta tietokanta;
    LaskunHallinta laskunHallinta = new LaskunHallinta(tietokanta);
    @FXML
    private void btLahetaLasku(ActionEvent event) {
        String email = tfEmailosoite.getText();
        // Tähän voisi lisätä email-osoitteen lähettämisen suorittavan metodin kutsun.

        // Koska toiminto ei ole käytössä, generoidaan ilmoitus:
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Email-toiminto ei ole käytössä");
        alert.setHeaderText("""
                Sähköpostilähetys ei ole käytössä.
                 Se voidaan ottaa käyttöön, kun yrityksellä
                 on sähköpostipalvelin, jonka tiedot lisätään ohjelmaan.""");
        alert.showAndWait();
    }

    @FXML
    private void btTallennaPDF(ActionEvent event) {
        Lasku lasku = laskuLw.getSelectionModel().getSelectedItem();

        try {
            laskunHallinta.luoPDF(lasku);
            tfTiedostonimi.setText(lasku.getTiedostonimi());
        } catch (SQLException | FileNotFoundException | DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void btPaivitaLaskutus(ActionEvent event) throws SQLException {

        VaraustenHallinta hallinta = new VaraustenHallinta();
        laskutaVarausLw.getItems().setAll(hallinta.getVaraukset());

        LaskunHallinta laskunhallinta = new LaskunHallinta(tietokanta);
        laskuLw.getItems().setAll(laskunhallinta.haeKaikkiLaskut());
    }
    private void PaivitaLaskutus() throws SQLException {
        VaraustenHallinta hallinta = new VaraustenHallinta();
        laskutaVarausLw.getItems().setAll(hallinta.getVaraukset());
        LaskunHallinta laskunhallinta = new LaskunHallinta(tietokanta);
        laskuLw.getItems().setAll(laskunhallinta.haeKaikkiLaskut());
    }

    @FXML
    private void buttonLuoUusi(ActionEvent event) throws SQLException {

        Varaus varaus = laskutaVarausLw.getSelectionModel().getSelectedItem();
        LaskunHallinta laskunhallinta = new LaskunHallinta(tietokanta);
        int varauksenid = varaus.getVarausId();
        Lasku lasku = laskunhallinta.luoLasku(varaus);

        taLaskunTiedot.setText("Luotu uusi lasku tietokantaan.");

        PaivitaLaskutus();
    }

    @FXML
    private void naytaTiedot(MouseEvent event) {
        Lasku valinta = laskuLw.getSelectionModel().getSelectedItem();
        if (valinta != null) {
            int laskunid = valinta.getLaskuId();
            LaskunHallinta laskunhallinta = new LaskunHallinta(tietokanta);
            try {
                Lasku lasku = laskunhallinta.haeLasku(laskunid);
                boolean onkoMaksettu = lasku.isMaksettu();
                String laskunTiedot = "Varaus ID: " + lasku.getVarausId() + ".  Laskun ID: "
                        + lasku.getLaskuId()+"\n" + " Laskun summa: " + (lasku.getSumma()+lasku.getAlv())+"\n"+
                        "Vuokra-aika: " + laskunhallinta.laskePaivienMaara(valinta.getVarausId()) + " vrk" + "\n"
                        + "Lasku maksettu: " + (onkoMaksettu ? "kyllä" : "ei");
                taLaskunTiedot.setText(laskunTiedot);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {taLaskunTiedot.setText("");}
    }

    @FXML private void btPoista(ActionEvent event) throws SQLException {
        Lasku lasku = laskuLw.getSelectionModel().getSelectedItem();
        LaskunHallinta laskunhallinta = new LaskunHallinta(tietokanta);
        if (lasku != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Vahvista poisto");
            alert.setHeaderText("Haluatko varmasti poistaa laskun, jonka ID on: " + lasku.getLaskuId() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    laskunhallinta.poistaLasku(lasku);
                    laskuLw.getItems().remove(lasku);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Valitse asiakas");
            alert.setHeaderText("Valitse poistettava asiakas listasta.");
            alert.showAndWait();
        }
        PaivitaLaskutus();
    }

    @FXML private void merkitseMaksettu(ActionEvent event) throws SQLException {

        Lasku valittuLasku = laskuLw.getSelectionModel().getSelectedItem();
        LaskunHallinta lh = new LaskunHallinta(tietokanta);
            if (valittuLasku != null) {
                boolean onMaksettu = checkMaksettu.isSelected();
                valittuLasku.setMaksettu(onMaksettu);
                lh.merkitseMaksetuksi(valittuLasku);
            }
        PaivitaLaskutus();
        }

    @FXML private void merkitseEiMaksettu(ActionEvent event) throws SQLException {

        Lasku valittuLasku = laskuLw.getSelectionModel().getSelectedItem();
        LaskunHallinta lh = new LaskunHallinta(tietokanta);
        if (valittuLasku != null) {
            boolean eiMaksettu = checkEiMaksettu.isSelected();
            valittuLasku.setMaksettu(eiMaksettu);
            lh.merkitseEiMaksetuksi(valittuLasku);
        }
        PaivitaLaskutus();
    }

    @FXML
    private void initialize() {
        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            ObservableList<Varaus> varaukset = FXCollections.observableArrayList(hallinta.getVaraukset());

            laskutaVarausLw.getItems().setAll(varaukset);

            LaskunHallinta laskunhallinta = new LaskunHallinta(tietokanta);
            laskuLw.getItems().setAll(laskunhallinta.haeKaikkiLaskut());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}