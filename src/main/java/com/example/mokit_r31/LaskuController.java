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
import java.util.List;
import java.util.Optional;

public class LaskuController {
    @FXML private ListView<Varaus> laskutaVarausLw;
    @FXML private ListView<Lasku> laskuLw;
    @FXML private Button btLahetaLasku;
    @FXML private Button btTallennaPDF;
    @FXML private Button buttonPaivita;
    @FXML private Button buttonLuoUusi;
    @FXML private Button buttonPoista;
    @FXML private TextField tfTiedostonimi;
    @FXML
    private TextArea taLaskunTiedot;

    Tietokanta tietokanta = new Tietokanta();

    private LaskunHallinta laskunHallinta = new LaskunHallinta(tietokanta);
    @FXML
    private void btLahetaLasku(ActionEvent event) {

    }

    @FXML
    private void btTallennaPDF(ActionEvent event) {
        Varaus varaus = laskutaVarausLw.getSelectionModel().getSelectedItem();
        int varauksenid = varaus.getVarausId();
        try {
            laskunHallinta.luoPDF(varauksenid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
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

    @FXML
    private void buttonLuoUusi(ActionEvent event) throws
            SQLException, DocumentException, FileNotFoundException {
        Varaus varaus = laskutaVarausLw.getSelectionModel().getSelectedItem();
        int varauksenid = varaus.getVarausId();
        Lasku lasku = laskunHallinta.luoLasku(varaus);
        taLaskunTiedot.setText("Luotu lasku.\n" + "Varaus ID: " + varauksenid + " Laskun ID: " + lasku.getLaskuId()
                + "Laskun summa: " + lasku.getSumma());
    }

    @FXML
    private void naytaTiedot(MouseEvent event) {
        Lasku valinta = laskuLw.getSelectionModel().getSelectedItem();
        if (valinta != null) {
            int laskunid = valinta.getLaskuId();
            LaskunHallinta laskunhallinta = new LaskunHallinta(tietokanta);
            try {
                Lasku lasku = laskunhallinta.haeLasku(laskunid);
                String laskunTiedot = "Varaus ID: " + lasku.getVarausId() + ".  Laskun ID: "
                        + lasku.getLaskuId()+"\n" + " Laskun summa: " + lasku.getSumma();
                taLaskunTiedot.setText(laskunTiedot);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {taLaskunTiedot.setText("");}
    }

    @FXML private void btPoista(ActionEvent event) {
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
    }

    @FXML
    private void initialize() {

        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            laskutaVarausLw.getItems().setAll(hallinta.getVaraukset());
            // varauksetLw.setOnMouseClicked(event -> handleVarausListDoubleClick(event));

            LaskunHallinta laskunhallinta = new LaskunHallinta(tietokanta);
            laskuLw.getItems().setAll(laskunhallinta.haeKaikkiLaskut());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}