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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MokitController {

    @FXML private TextField TfMokki;
    @FXML private Button BtHaeMokki;
    @FXML private TextField TfAlue;
    @FXML private Button BtHaeAlue;
    @FXML private Button BtLisaaMokki;
    @FXML private Button BtLisaaAlue;
    @FXML private Button bTPoistaMokki;
    @FXML private Button btPoistaAlue;
    @FXML private ListView<Mokki> LwMokit;
    @FXML private ListView<Alue> LwAlueet;
    Tietokanta tietokanta = new Tietokanta();
    private MokkienHallinta mokkienHallinta = new MokkienHallinta(tietokanta);
    private AlueidenHallinta alueidenHallinta = new AlueidenHallinta(tietokanta);

    private void paivitaListat() {
        AlueidenHallinta alueidenhallinta = new AlueidenHallinta(tietokanta);
        try {
            LwAlueet.getItems().setAll(alueidenhallinta.haeKaikkiAlueet());
            LwMokit.getItems().setAll(mokkienHallinta.haeKaikkiMokit());
        } catch(SQLException e) { throw new RuntimeException(e);}
    }

    public void initialize(){
        paivitaListat();

        // Alustetaan ListView Mökkihaulle
            LwMokit.setCellFactory(lv -> new ListCell<Mokki>() {
                @Override
                protected void updateItem(Mokki mokki, boolean empty) {
                    super.updateItem(mokki, empty);
                    if (empty || mokki == null) {
                        setText(null);
                    } else {
                        setText(mokki.toString());
                    }
                }
            });

            // Alustetaan ListView Aluehaulle
            LwAlueet.setCellFactory(lv -> new ListCell<Alue>() {
                @Override
                protected void updateItem(Alue uusiAlue, boolean empty) {
                    super.updateItem(uusiAlue, empty);
                    if (empty || uusiAlue == null) {
                        setText(null);
                    } else {
                        setText(uusiAlue.toString());
                    }
                }
            });

            BtLisaaMokki.setOnAction(event ->{
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LisaaMokki.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    paivitaListat();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        BtLisaaAlue.setOnAction(event ->{
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LisaaAlue.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        BtHaeMokki.setOnAction(event ->{
            String hakusana = TfMokki.getText();
            // Kutsutaan haeMokit-metodia ja tallennetaan sen palauttamat tiedot listaan
            ObservableList<Mokki> hakutulokset = FXCollections.observableArrayList(mokkienHallinta.haeMokit(hakusana));

            // Asetetaan ListViewin Data näkyviin
            LwMokit.setItems(hakutulokset);

            LwMokit.setCellFactory(param -> new ListCell<Mokki>() {
                @Override
                protected void updateItem(Mokki mokki, boolean empty) {
                    super.updateItem(mokki, empty);
                    if (empty || mokki == null) {
                        setText(null);
                    } else {
                        setText("Mökki: " + mokki.getNimi() + "\n" +
                                "Hinta: " + mokki.getHinta() + " €\n" +
                                "Henkilömäärä: " + mokki.getHlo() + "\n" +
                                "Varustelu: " + mokki.getVarustelu() + "\n" +
                                "Osoite: " + mokki.getOsoite() + ", " + mokki.getPostiNro());
                    }
                }
            });
        });

            LwMokit.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Mokki selectedMokki = LwMokit.getSelectionModel().getSelectedItem();
                    if (selectedMokki != null) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MuokkaaMokkia.fxml"));
                            Parent root = fxmlLoader.load();
                            MuokkaMokkiaController controller = fxmlLoader.getController();
                            controller.setMokki(selectedMokki);
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            BtHaeAlue.setOnAction(event ->{
                String hakusana = TfAlue.getText();
                // Kutsutaan haeAlueet-metodia ja tallennetaan sen palauttamat tiedot listview:iin
                AlueidenHallinta alueidenhallinta = new AlueidenHallinta(tietokanta);
                try {
                    LwAlueet.getItems().setAll(alueidenhallinta.haeKaikkiAlueet());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // asetetaan ListView:n soluille oma solutehdas
                LwAlueet.setCellFactory(param -> new ListCell<Alue>() {
                    @Override
                    protected void updateItem(Alue alue, boolean empty) {
                        super.updateItem(alue, empty);

                        if (empty || alue == null) {
                            setText(null);
                        } else {
                            // muutetaan Alue-olion tiedot merkkijonoksi ja asetetaan se ListView:n solun tekstiksi
                            setText(alue.getNimi() + " - " + alue.getAlueId());
                        }
                    }
                });

            });

            LwAlueet.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Alue selectedAlue = LwAlueet.getSelectionModel().getSelectedItem();
                    if (selectedAlue != null) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MuokkaaAlue.fxml"));
                            Parent root = fxmlLoader.load();
                            MuokkaaAlueController controller = fxmlLoader.getController();
                            controller.setAlue(selectedAlue);
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            bTPoistaMokki.setOnAction(e -> {
                Mokki valittuMokki = LwMokit.getSelectionModel().getSelectedItem();
                if (valittuMokki != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Vahvista poisto");
                    alert.setHeaderText("Haluatko varmasti poistaa mökin " + valittuMokki.getNimi() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        MokkienHallinta.poistaMokki(valittuMokki.getMokkiId());
                        LwMokit.getItems().remove(valittuMokki);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Valitse mökki");
                    alert.setHeaderText("Valitse poistettava mökki listasta.");
                    alert.showAndWait();
                }
            });

            btPoistaAlue.setOnAction(e -> {
                Alue valittuAlue = LwAlueet.getSelectionModel().getSelectedItem();
                if (valittuAlue != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Vahvista poisto");
                    alert.setHeaderText("Haluatko varmasti poistaa alueen " + valittuAlue.getNimi() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        AlueidenHallinta.poistaAlueenTiedot(valittuAlue.getAlueId());
                        LwAlueet.getItems().remove(valittuAlue);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Valitse Alue");
                    alert.setHeaderText("Valitse poistettava Alue listasta.");
                    alert.showAndWait();
                }
            });
        }

}



