package com.example.mokit_r31;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;


public class PalvelutRaporttiController {

    @FXML
    private ComboBox<String> aluecbox;

    @FXML
    private DatePicker alkupaivaDP;

    @FXML
    private DatePicker loppupaivaDP;

    @FXML
    private ListView<String> palvelutLw;

    @FXML
    private Button haeButton;

    public void initialize() {
        // Ladataan alueet comboboxiin
        PalvelutRaportti.lataaAlueetComboBoxiin(aluecbox);

        // Lisätään tapahtumankuuntelija "Hae" -napille
        haeButton.setOnAction(e -> {
            // Haetaan valitut arvot
            String alue = aluecbox.getValue();
            String alkupaiva = alkupaivaDP.getValue().toString();
            String loppupaiva = loppupaivaDP.getValue().toString();

            // Haetaan raportti palveluista
            PalvelutRaportti.haePalveluRaportti(alue, alkupaiva, loppupaiva, palvelutLw);
        });
    }

}