package com.example.mokit_r31;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MokitLisaaController {
    @FXML private Button BtTallenna;
    @FXML private TextField TfMokinNimi;
    @FXML private TextField TfHlo;
    @FXML private TextField TfHinta;
    @FXML private TextField TfAlueId;
    @FXML private TextField TfKatuosoite;
    @FXML private TextArea TfMokinKuvaus;
    @FXML private TextArea TfMokinVarustelu;

    @FXML private ComboBox<String> cbPostinro;

    Tietokanta tietokanta = new Tietokanta();

    public void initialize() {
        try {
            cbPostinro();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void cbPostinro() throws SQLException {
        // Hae postinumerot
        PostiHallinta postiHallinta = new PostiHallinta(tietokanta);
        List<String> postinumerot = postiHallinta.haePostinumerot();

        // Lisää postinumerot comboboxiin
        cbPostinro.getItems().addAll(postinumerot);

    }
    @FXML
    private void BtTallenna (ActionEvent event){

        String postinumero = cbPostinro.getSelectionModel().getSelectedItem();

        String nimi = TfMokinNimi.getText();
            String kuvaus = TfMokinKuvaus.getText();
            String osoite = TfKatuosoite.getText();
            String varustelu = TfMokinVarustelu.getText();
            int hlo = Integer.parseInt(TfHlo.getText());
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

