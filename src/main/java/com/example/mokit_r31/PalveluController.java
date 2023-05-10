package com.example.mokit_r31;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class PalveluController {

    @FXML
    private TextField alueenPostinumeroTf;

    @FXML
    private Button luoPalveluBt;

    @FXML
    private Button naytaPalveluBt;

    @FXML
    private TextField palveluArvonlisaveroTf;

    @FXML
    private TextField palveluHintaTf;

    @FXML
    private TextField palveluIDTf;

    @FXML
    private TextField palveluKuvausTf;

    @FXML
    private ListView<Palvelu> palveluLista;

    @FXML
    private TextField palveluNimiTf;

    @FXML
    private TextField palveluTyyppiTf;

    @FXML
    private Button poistaPalveluBt;

    @FXML
    private Button tallennaPalveluBt;

    Tietokanta tietokanta = new Tietokanta();
    private PalvelujenHallinta PalvelujenHallinta = new PalvelujenHallinta(tietokanta);


    @FXML
    private void initialize() {


        //naytaPalveluBt tapahtumakäsittelijä
        naytaPalveluBt.setOnAction(event -> naytaPalvelut());

        luoPalveluBt.setOnAction(e -> {
            // Tarkista, onko jokin palvelu valittu
            if (palveluLista.getSelectionModel().getSelectedItem() != null) {
                // Tyhjennä tekstikentät
                palveluIDTf.setText("");
                alueenPostinumeroTf.setText("");
                palveluNimiTf.setText("");
                palveluTyyppiTf.setText("");
                palveluKuvausTf.setText("");
                palveluHintaTf.setText("");
                palveluArvonlisaveroTf.setText("");

                // Poista valinta palvelulistalta
                palveluLista.getSelectionModel().clearSelection();
            }

            else {
                // Tyhjennä tekstikentät, jos mitään palvelua ei ole valittu
                palveluIDTf.setText("");
                alueenPostinumeroTf.setText("");
                palveluNimiTf.setText("");
                palveluTyyppiTf.setText("");
                palveluKuvausTf.setText("");
                palveluHintaTf.setText("");
                palveluArvonlisaveroTf.setText("");
            }
        });

        poistaPalveluBt.setOnAction(event -> {
            // Tarkista, onko jokin palvelu valittu
            Palvelu valittuPalvelu = palveluLista.getSelectionModel().getSelectedItem();
            if (valittuPalvelu != null) {
                // Avaa varmistusikkuna
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Vahvista poisto");
                alert.setHeaderText("Haluatko varmasti poistaa palvelun?");
                alert.setContentText("Palvelun poisto on peruuttamaton toimenpide.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    // Poista palvelu listalta
                    palveluLista.getItems().remove(valittuPalvelu);
                    // Poista palvelu myös tietokannasta
                    try {
                        // Luo tietokantayhteys
                        Connection yhteys = tietokanta.getYhteys();

                        // Luo DELETE-kysely
                        PreparedStatement kysely = yhteys.prepareStatement("DELETE FROM palvelut WHERE id = ?");
                        kysely.setInt(1, valittuPalvelu.getId());

                        // Suorita kysely
                        kysely.executeUpdate();

                        // Sulje yhteys
                        yhteys.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }}
        });


        // lisätään kuuntelija palveluLista:lle
        palveluLista.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // täytetään tekstikentät valitun palvelun tiedoilla
                    if (newValue != null) {
                        palveluIDTf.setText(Integer.toString(newValue.getId()));
                        alueenPostinumeroTf.setText(Integer.toString(newValue.getAlueId()));
                        palveluNimiTf.setText(newValue.getNimi());
                        palveluTyyppiTf.setText(Integer.toString(newValue.getTyyppi()));
                        palveluKuvausTf.setText(newValue.getKuvaus());
                        palveluHintaTf.setText(Double.toString(newValue.getHinta()));
                        palveluArvonlisaveroTf.setText(Double.toString(newValue.getAlv()));
                    }
                });


        //tallennaPalveluBt tapahtumankäsittelijä
        tallennaPalveluBt.setOnAction(e -> {

            // Tarkista, onko kaikki kentät täytetty
            if (palveluIDTf.getText().isEmpty() || alueenPostinumeroTf.getText().isEmpty() || palveluNimiTf.getText().isEmpty() || palveluTyyppiTf.getText().isEmpty() || palveluKuvausTf.getText().isEmpty() || palveluHintaTf.getText().isEmpty() || palveluArvonlisaveroTf.getText().isEmpty()) {
                // Jos jokin kenttä on tyhjä, näytä virheilmoitusikkuna
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Tallennus ei onnistu");
                alert.setHeaderText("Täytä kaikki kentät ennen tallentamista");
                alert.showAndWait();
                return;
            }

            // Tarkista, että palveluIDTf-kenttään on syötetty sallittu arvo
            try {
                int palveluId = Integer.parseInt(palveluIDTf.getText());
                if (palveluId < 1 || palveluId > 5) {
                    // Näytä virheilmoitusikkuna, jos palveluIDTf-kenttään on syötetty muu luku kuin 1,2,3,4 tai 5
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Tallennus ei onnistu");
                    alert.setHeaderText("PalveluID:n tulee olla jokin seuraavista: 1, 2, 3, 4 tai 5");
                    alert.showAndWait();
                    return;
                }

                int alueenPostinumero = Integer.parseInt(alueenPostinumeroTf.getText());
                double palveluHinta = Double.parseDouble(palveluHintaTf.getText());
                double palveluArvonlisavero = Double.parseDouble(palveluArvonlisaveroTf.getText());
                int palveluTyyppi = Integer.parseInt(palveluTyyppiTf.getText());
            } catch (NumberFormatException ex) {
                // Näytä virheilmoitusikkuna, joka kertoo mihin kenttään on syötetty jotain muuta kuin numeron
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Tallennus ei onnistu");
                String virheviesti = "Tarkista seuraavat kentät: ";
                if (!palveluIDTf.getText().matches("[0-9]+")) {
                    virheviesti += "\n - PalveluID (tulee olla numero)";
                }
                if (!alueenPostinumeroTf.getText().matches("[0-9]+")) {
                    virheviesti += "\n - Alueen postinumero (tulee olla numero)";
                }
                if (!palveluHintaTf.getText().matches("[0-9.]+")) {
                    virheviesti += "\n - Palvelun hinta (tulee olla numero tai desimaaliluku)";
                }
                if (!palveluArvonlisaveroTf.getText().matches("[0-9.]+")) {
                    virheviesti += "\n - Palvelun arvonlisävero (tulee olla numero tai desimaaliluku)";
                }
                if (!palveluTyyppiTf.getText().matches("[0-9]+")) {
                    virheviesti += "\n - Palvelun tyyppi (tulee olla numero)";
                }
                alert.setHeaderText(virheviesti);
                alert.showAndWait();
                return;
            }





            int id = Integer.parseInt(palveluIDTf.getText());
            int alueId = Integer.parseInt(alueenPostinumeroTf.getText());
            String nimi = palveluNimiTf.getText();
            int tyyppi = Integer.parseInt(palveluTyyppiTf.getText());
            String kuvaus = palveluKuvausTf.getText();
            double hinta = Double.parseDouble(palveluHintaTf.getText());
            double alv = Double.parseDouble(palveluArvonlisaveroTf.getText());

            // Luo uusi Palvelu-olio
            Palvelu uusiPalvelu = new Palvelu(id, alueId, nimi, tyyppi, kuvaus, hinta, alv);
            palveluLista.getItems().add(uusiPalvelu);

            try {
                tallennaPalvelu(uusiPalvelu);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }


    public void tallennaPalvelu(Palvelu palvelu) throws SQLException {
        Connection yhteys = null;
        try {
            yhteys = tietokanta.getYhteys();
            PreparedStatement lisayslause = yhteys.prepareStatement("INSERT INTO Palvelu (palvelu_id, alue_id, nimi, tyyppi, kuvaus, hinta, alv) VALUES (?, ?, ?, ?, ?, ?, ?)");

            yhteys.setAutoCommit(false);

            lisayslause.setInt(1, palvelu.getId());
            lisayslause.setInt(2, palvelu.getAlueId());
            lisayslause.setString(3, palvelu.getNimi());
            lisayslause.setInt(4, palvelu.getTyyppi());
            lisayslause.setString(5, palvelu.getKuvaus());
            lisayslause.setDouble(6, palvelu.getHinta());
            lisayslause.setDouble(7, palvelu.getAlv());
            lisayslause.executeUpdate();

            yhteys.commit();
        } catch (SQLException e) {
            // jotain meni pieleen, peruuta transaktio
            if (yhteys != null) {
                yhteys.rollback();
            }
            throw e;
        } finally {
            if (yhteys != null) {
                yhteys.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tallennus onnistui");
                alert.setHeaderText("Palvelun tiedot tallennettu tietokantaan");
                alert.showAndWait();
            }
        }
    }





    public void naytaPalvelut() {
        ObservableList<Palvelu> palveluData = FXCollections.observableArrayList();
        PalvelujenHallinta palveluHallinta = new PalvelujenHallinta(new Tietokanta());
        palveluData.addAll(palveluHallinta.naytaPalvelu());
        palveluLista.setItems(palveluData);
        palveluLista.setCellFactory(param -> new ListCell<Palvelu>() {
            @Override
            protected void updateItem(Palvelu palvelu, boolean empty) {
                super.updateItem(palvelu, empty);
                if (empty || palvelu == null) {
                    setText(null);
                } else {
                    setText(palvelu.getId() + " - " + palvelu.getNimi());
                }
            }
        });


    }


    }




