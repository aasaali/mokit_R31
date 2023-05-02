package com.example.mokit_r31;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class AsiakasController {
    @FXML
    private Button tallennaVarausBt;
    @FXML
    private TextField asiakasTf;
    @FXML
    private TextField mokkiTf;
    @FXML
    private DatePicker alkupaivaDP;
    @FXML
    private DatePicker loppupaivaDP;

    @FXML
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

}