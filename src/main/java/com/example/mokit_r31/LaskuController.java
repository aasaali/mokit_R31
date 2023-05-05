package com.example.mokit_r31;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.List;


public class LaskuController {
    @FXML
    private ListView varausLista;
    @FXML
    private Button buttonHae;
    @FXML
    private Button buttonPaivita;
    @FXML
    private Button buttonLuoUusi;
    @FXML
    private Button buttonPoista;
    Tietokanta tietokanta = new Tietokanta();
    /*
    private LaskunHallinta laskunHallinta = new LaskunHallinta(tietokanta);

    @FXML
    private void initialize() {

        try {
            List<Varaus> varaukset = new LaskunHallinta().getVaraukset();
            // Luo uusi LaskunHallinta-olio ja kutsuu sen getVaraukset()-metodia, joka palauttaa listan Varaus-olioita
            ObservableList<Varaus> varausData = FXCollections.observableArrayList(varaukset);
            // Luo uuden ObservableList-olion, joka sisältää Varaus-oliot
            varausLista.setItems(varausData); // Aseta ObservableList-olio ListView-komponenttiin
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            List<Varaus> varaukset = new LaskunHallinta().getVaraukset(); // Luo uusi LaskunHallinta-olio ja kutsuu sen getVaraukset()-metodia, joka palauttaa listan Varaus-olioita
            ObservableList<Varaus> varausData = FXCollections.observableArrayList(varaukset); // Luo uuden ObservableList-olion, joka sisältää Varaus-oliot
            varausLista.setItems(varausData); // Aseta ObservableList-olio ListView-komponenttiin
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void buttonLuoUusi(ActionEvent event) {
        Varaus varaus = varausLista.getSelectionModel().getSelectedItem();
        //LocalDateTime nyt
        Lasku uusiLasku = new Lasku(varaus);
        try {
            LaskunHallinta hallinta = new LaskunHallinta();
            hallinta.lisaaVaraus(varaus);
            tallennaVarausBt.setText("Vahvistettu");
        }
        catch (SQLException e) {
            // Virheen käsittely
        }
    } */
}