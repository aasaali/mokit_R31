package com.example.mokit_r31;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class AsiakasController {
    @FXML
    private ListView<Asiakas> asiakasLista;
    @FXML
    private Button buttonHae;
    @FXML
    private Button buttonPaivita;
    @FXML
    private Button buttonLuoUusi;
    @FXML
    private Button buttonPoista;

    Tietokanta tietokanta = new Tietokanta();
    private AsiakasHallinta asiakasHallinta = new AsiakasHallinta(tietokanta);

    @FXML
    private void initialize() {
        // Hae kaikki asiakkaat tietokannasta ja luo observable list
        ObservableList<Asiakas> asiakkaat = FXCollections.observableArrayList(asiakasHallinta.haeKaikkiAsiakkaat());

        // Aseta observable list ListView-komponentin dataksi
        asiakasLista.setItems(asiakkaat);
        System.out.println(asiakkaat);
    }

    @FXML
    private void ButtonHae(ActionEvent event) {
        ObservableList<Asiakas> asiakkaat = FXCollections.observableArrayList(asiakasHallinta.haeKaikkiAsiakkaat());
        asiakasLista.setItems(asiakkaat);
    }
}

   /** @FXML
    private void tallennaVarausButton(ActionEvent event) {
        int asiakas = Integer.parseInt(asiakasTf.getText());
        int mokki = Integer.parseInt(mokkiTf.getText());
        LocalDateTime alkupaiva = alkupaivaDP.getValue().atStartOfDay();
        LocalDateTime loppupaiva = loppupaivaDP.getValue().atStartOfDay();
        Varaus varaus = new Varaus(asiakas,mokki,alkupaiva,loppupaiva);
        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            hallinta.lisaaVaraus(varaus);
            tallennaVarausBt.setText("Vahvistettu");
        }
        catch (SQLException e) {
            // Virheen käsittely
        }
    }
   */

