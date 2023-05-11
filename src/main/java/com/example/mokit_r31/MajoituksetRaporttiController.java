package com.example.mokit_r31;

import com.itextpdf.text.pdf.TextField;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MajoituksetRaporttiController {

    @FXML
    private DatePicker alkupaivaDP;

    @FXML
    private ComboBox<String> aluecbox;

    @FXML
    private Button haeButton;

    @FXML
    private Button haeButton1;

    @FXML
    private DatePicker loppupaivaDP;

    @FXML
    private ListView<String> palvelutLw;
    @FXML private Button btPaivitaAlueet;

    public void paivitaAlueCombobox() {
        aluecbox.getItems().clear();
        MajoitusraporttiHallinta.lataaValinnatComboBoxiin(aluecbox);
    }

    public void initialize() {

        haeButton1.setOnAction(e -> {

        });

        btPaivitaAlueet.setOnAction( e -> paivitaAlueCombobox());




        // Ladataan alueet comboboxiin
        MajoitusraporttiHallinta.lataaValinnatComboBoxiin(aluecbox);

        // Lisätään tapahtumankuuntelija "Hae" -napille
        haeButton.setOnAction(e -> {
            // Haetaan valitut arvot
            String alue = aluecbox.getValue();
            String alkupaiva = alkupaivaDP.getValue().toString();
            String loppupaiva = loppupaivaDP.getValue().toString();
            if (alkupaivaDP.getValue() == null || loppupaivaDP.getValue() == null) {
                System.out.println("Valitse päivämäärät.");
                return;
            }

            // Haetaan raportti palveluista
            MajoitusraporttiHallinta.haeVaraus(alue, alkupaiva, loppupaiva);

            // Haetaan raportti palveluista
            ArrayList<String> varaukset = MajoitusraporttiHallinta.haeVaraus(alue, alkupaiva, loppupaiva);
            palvelutLw.getItems().clear();
            palvelutLw.getItems().addAll(varaukset);



        });
    }


}
