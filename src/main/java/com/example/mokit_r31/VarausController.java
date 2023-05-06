package com.example.mokit_r31;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;


import java.io.IOException;
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


    @FXML
    private void handleVarausListDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Haetaan valittu varaus
            Varaus varaus = varauksetLw.getSelectionModel().getSelectedItem();

            // Jos varaus on valittu, avataan sen tiedot uuteen ikkunaan
            if (varaus != null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MuokkaaVaraustaIkkuna.fxml"));
                    Parent parent = fxmlLoader.load();
                    MuokkaaVaraustaController controller = fxmlLoader.getController();
                    controller.setVaraus(varaus);
                    Scene scene = new Scene(parent, 600, 400);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initialize() {
        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            varauksetLw.getItems().setAll(hallinta.getVaraukset());
            varauksetLw.setOnMouseClicked(event -> handleVarausListDoubleClick(event));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



