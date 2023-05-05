package com.example.mokit_r31;
import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;


public class LaskuController {
    @FXML
    private ListView<Varaus> laskutaVarausLw;
    @FXML
    private ListView<Lasku> laskuLw;
    @FXML
    private Button btLahetaLasku;
    @FXML
    private Button btTallennaPDF;
    @FXML
    private Button buttonPaivita;
    @FXML
    private Button buttonLuoUusi;
    @FXML
    private Button buttonPoista;
    @FXML
    private TextField tfTiedostonimi;
    Tietokanta tietokanta = new Tietokanta();

    private LaskunHallinta laskunHallinta = new LaskunHallinta(tietokanta);
    @FXML
    private void btLahetaLasku(ActionEvent event) {

    }

    @FXML
    private void btTallennaPDF(ActionEvent event) {

    }

    @FXML
    private void btPaivitaLaskutus(ActionEvent event) {

    }

    @FXML
    private void buttonLuoUusi(ActionEvent event) throws SQLException, DocumentException, FileNotFoundException {
        Varaus varaus = laskutaVarausLw.getSelectionModel().getSelectedItem();
        Lasku uusiLasku = new Lasku();
        int varauksenid = varaus.getVarausId();
        laskunHallinta.luoLasku(varauksenid);

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