package com.example.mokit_r31;
import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class LaskuController {
    @FXML private ListView<Varaus> laskutaVarausLw;
    @FXML private ListView<Lasku> laskuLw;
    @FXML private Button btLahetaLasku;
    @FXML private Button btTallennaPDF;
    @FXML private Button buttonPaivita;
    @FXML private Button buttonLuoUusi;
    @FXML private Button buttonPoista;
    @FXML private TextField tfTiedostonimi;
    @FXML private TextArea taLaskunTiedot;

    Tietokanta tietokanta = new Tietokanta();

    private LaskunHallinta laskunHallinta = new LaskunHallinta(tietokanta);
    @FXML
    private void btLahetaLasku(ActionEvent event) {

    }

    @FXML
    private void btTallennaPDF(ActionEvent event) {

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
    private void initialize() {

        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            laskutaVarausLw.getItems().setAll(hallinta.getVaraukset());
            // varauksetLw.setOnMouseClicked(event -> handleVarausListDoubleClick(event));

            LaskunHallinta laskunhallinta = new LaskunHallinta(tietokanta);
            laskuLw.getItems().setAll(laskunhallinta.haeKaikkiLaskut());

            Lasku valinta = laskuLw.getSelectionModel().getSelectedItem();
            if (valinta != null) {
                taLaskunTiedot.setText("Luotu lasku.\n" + "Varaus ID: " + valinta.getVarausId() + " Laskun ID: "
                        + valinta.getLaskuId() + "Laskun summa: " + valinta.getSumma());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}