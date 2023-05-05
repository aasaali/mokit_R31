package com.example.mokit_r31;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.w3c.dom.events.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class VarausController {
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
    private ListView<Varaus> varauksetLw;

    @FXML
    private Button btHae;
    @FXML
    private Button btPoista;


    @FXML
    private void tallennaVarausBt() {
        int asiakas = Integer.parseInt(asiakasTf.getText());
        int mokki = Integer.parseInt(mokkiTf.getText());
        LocalDateTime alkupaiva = alkupaivaDP.getValue().atStartOfDay();
        LocalDateTime loppupaiva = loppupaivaDP.getValue().atStartOfDay();
        Varaus varaus = new Varaus(asiakas,mokki,alkupaiva,loppupaiva);
        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            hallinta.lisaaVaraus(varaus);
            varauksetLw.getItems().setAll(hallinta.getVaraukset());

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btPoista(ActionEvent event) {
        Varaus varaus = varauksetLw.getSelectionModel().getSelectedItem();
        if (varaus != null) {
            try {
                VaraustenHallinta hallinta = new VaraustenHallinta();
                hallinta.poistaVaraus(varaus.getVarausId());
                varauksetLw.getItems().remove(varaus);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btHae(ActionEvent event) {
        VaraustenHallinta hallinta = new VaraustenHallinta();
        try {
            varauksetLw.getItems().setAll(hallinta.getVaraukset());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }



    public void initialize() {
        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            varauksetLw.getItems().setAll(hallinta.getVaraukset());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



