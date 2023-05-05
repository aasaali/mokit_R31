package com.example.mokit_r31;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;

public class MokitController {

        @FXML
        private Button BtpalaaMain;
        @FXML
        private TextField TfMokki;
        @FXML
        private Button BtHaeMokki;
        @FXML
        private TextField TfAlue;
        @FXML
        private Button BtHaeAlue;
        @FXML
        private Button BtLisaaMokki;
        @FXML
        private Button BtLisaaAlue;
        @FXML
        private ListView<Mokki> MokitJaAlueetLista;

   /** Tietokanta tietokanta = new Tietokanta();
    private MokkienHallinta NewMokkienHallinta = new MokkienHallinta(tietokanta);

    @FXML
    private void initialize() {
        // Hae kaikki mokit tietokannasta ja luo observable list
        ObservableList<Mokki> mokit = FXCollections.observableArrayList(NewMokkienHallinta.haeKaikkiMokit());

        // Aseta observable list ListView-komponentin dataksi
        MokitJaAlueetLista.setItems(mokit);

        // Lisää tapahtumankäsittelijä jokaiselle listan elementille
        MokitJaAlueetLista.setCellFactory(param -> {
            ListCell<Mokki> cell = new ListCell<>() {
                @Override
                protected void updateItem(Mokki NewMokki, boolean empty) {
                    super.updateItem(NewMokki, empty);
                    if (empty || NewMokki == null) {
                        setText(null);
                    } else {
                        setText(NewMokki.getNimi() + " " + NewMokki.getHinta());
                    }
                }
            };

          /**  cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) {
                    Mokki NewMokki = cell.getItem();
                    // Avaa uusi ikkuna tietojen muokkausta varten
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MuokkaaAsiakasta.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                        MuokkaaAsiakastaController controller = loader.getController();
                        controller.setAsiakas(asiakas);
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            });*/

          /**  return cell;
        });
    }

    @FXML
    private void ButtonHae(ActionEvent event) {
        ObservableList<Mokki> mokit = FXCollections.observableArrayList(NewMokkienHallinta.haeKaikkiMokit());
        MokitJaAlueetLista.setItems(mokit);
    }*/

    }
