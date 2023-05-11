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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class PalveluController {
    //testi
    @FXML
    private ComboBox<Alue> cbAlueID;

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

    public Button muokkaaPalveluaBt;

    static Tietokanta tietokanta;
    private final PalvelujenHallinta palveluHallinta = new PalvelujenHallinta(tietokanta);

    AlueidenHallinta alueidenHallinta = new AlueidenHallinta(tietokanta);


    private void listaaAlueetComboboxiin() throws SQLException {
        ObservableList<Alue> alueet = FXCollections.observableArrayList(alueidenHallinta.haeKaikkiAlueet());
        cbAlueID.setItems(alueet);

    }

    @FXML
    private void initialize() {

        try {
            listaaAlueetComboboxiin();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //testi
        //naytaPalveluBt tapahtumakäsittelijä
        naytaPalveluBt.setOnAction(event -> naytaPalvelut());

        muokkaaPalveluaBt.setOnAction(event -> muokkaaPalvelua());



        luoPalveluBt.setOnAction(e -> {
            // Tarkista, onko jokin palvelu valittu
            if (palveluLista.getSelectionModel().getSelectedItem() != null) {
                // Tyhjennä tekstikentät
                palveluIDTf.setText("");
                cbAlueID.getSelectionModel().clearSelection();
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
                cbAlueID.getSelectionModel().clearSelection();
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
                try {
                    // Kutsu poistaPalvelu-metodia valitulle palvelulle
                    poistaPalvelu(valittuPalvelu);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });





        // lisätään kuuntelija palveluLista:lle
        palveluLista.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // täytetään tekstikentät valitun palvelun tiedoilla
                    if (newValue != null) {
                        cbAlueID.setValue(new Alue(newValue.getAlueId(), null));
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
            if (cbAlueID.getValue() == null || palveluNimiTf.getText().isEmpty() || palveluTyyppiTf.getText().isEmpty() || palveluKuvausTf.getText().isEmpty() || palveluHintaTf.getText().isEmpty() || palveluArvonlisaveroTf.getText().isEmpty()) {
                // Jos jokin kenttä on tyhjä, näytä virheilmoitusikkuna
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Tallennus ei onnistu");
                alert.setHeaderText("Täytä kaikki kentät ennen tallentamista");
                alert.showAndWait();
                return;
            }

            Alue valittu = cbAlueID.getSelectionModel().getSelectedItem();
            int alueId = valittu.getAlueId();
            String nimi = palveluNimiTf.getText();
            int tyyppi = Integer.parseInt(palveluTyyppiTf.getText());
            String kuvaus = palveluKuvausTf.getText();
            double hinta = Double.parseDouble(palveluHintaTf.getText());
            double alv = Double.parseDouble(palveluArvonlisaveroTf.getText());

            // Luo uusi Palvelu-olio
            Palvelu uusiPalvelu = new Palvelu(alueId, nimi, tyyppi, kuvaus, hinta, alv);

            palveluLista.getItems().add(uusiPalvelu);

            try {
                tallennaPalvelu(uusiPalvelu);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML private void muokkaaPalvelua() {
        {
            Palvelu palvelu = palveluLista.getSelectionModel().getSelectedItem();
            if (palvelu != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MuokkaaPalvelua.fxml"));
                    Parent root = loader.load();
                    MuokkaaPalveluaController controller = loader.getController();
                    controller.setPalvelu(palvelu);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }}

    public void poistaPalvelu(Palvelu palvelu) throws SQLException {
        int valittuID = palvelu.getId();
        System.out.println(valittuID);
        if (palvelu != null) {
            // Avaa varmistusikkuna
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Vahvista poisto");
            alert.setHeaderText("Haluatko varmasti poistaa palvelun?");
            alert.setContentText("Palvelun poisto on peruuttamaton toimenpide.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                // Poista palvelu tietokannasta
                try {
                    // Luo tietokantayhteys
                    Connection yhteys = Tietokanta.getYhteys();

                    // Luo DELETE-kysely
                    PreparedStatement kysely = yhteys.prepareStatement("DELETE FROM palvelu WHERE palvelu_id = ?");
                    kysely.setInt(1, valittuID);
                    kysely.executeUpdate();//moi


                    // Sulje yhteys
                    yhteys.close();
                    palveluLista.getItems().remove(palvelu);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }}

    }

    public static void tallennaPalvelu(Palvelu palvelu) throws SQLException {
        Connection yhteys = null;
        try {
            yhteys = Tietokanta.getYhteys();
            PreparedStatement lisayslause = yhteys.prepareStatement("INSERT INTO palvelu (alue_id, nimi, tyyppi, kuvaus, hinta, alv) VALUES (?, ?, ?, ?, ?, ?)");

            yhteys.setAutoCommit(false);


            lisayslause.setInt(1, palvelu.getAlueId());
            lisayslause.setString(2, palvelu.getNimi());
            lisayslause.setInt(3, palvelu.getTyyppi());
            lisayslause.setString(4, palvelu.getKuvaus());
            lisayslause.setDouble(5, palvelu.getHinta());
            lisayslause.setDouble(6, palvelu.getAlv());
            lisayslause.executeUpdate();
            yhteys.commit();
            System.out.println("Onnistui");
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
        palveluData.addAll(palveluHallinta.naytaPalvelu());
        palveluLista.setItems(palveluData);
        palveluLista.setCellFactory(param -> new ListCell<>() {
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