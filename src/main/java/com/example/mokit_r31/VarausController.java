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
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VarausController {
    @FXML
    private Button tallennaVarausBt;
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
    private Button btMuokkaa;
    @FXML
    private TextArea varausTa;
    @FXML
    private ComboBox<Asiakas> asiakasCb;
    private Tietokanta tietokanta;

    @FXML
    private ComboBox<Mokki> mokkiCb;
    @FXML
    private ListView<Palvelu> palvelutLv;



    private void lataaAsiakkaat() {
        AsiakasHallinta hallinta = new AsiakasHallinta(tietokanta);
        ObservableList<Asiakas> asiakkaat = FXCollections.observableArrayList(hallinta.haeKaikkiAsiakkaat());
        asiakasCb.setItems(asiakkaat);
    }



    private void lataaMokit() {
        MokkienHallinta hallinta = new MokkienHallinta(tietokanta);
        ObservableList<Mokki> mokit = FXCollections.observableArrayList(hallinta.haeMokit(""));
        mokkiCb.setItems(mokit);
        }


    @FXML
    private void tallennaVarausBt() {
        Asiakas valittuAsiakas = asiakasCb.getSelectionModel().getSelectedItem();
        Mokki valittuMokki = mokkiCb.getSelectionModel().getSelectedItem();

        if (valittuAsiakas != null && valittuMokki != null && alkupaivaDP.getValue() != null && loppupaivaDP.getValue() != null) {
            LocalDateTime alkupaiva = alkupaivaDP.getValue().atStartOfDay();
            LocalDateTime loppupaiva = loppupaivaDP.getValue().atStartOfDay();
            if (loppupaiva.isBefore(alkupaiva)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Loppupäivämäärä ei voi olla ennen alkupäivämäärää.");
                alert.showAndWait();
                return;
            }
            int asiakasId = valittuAsiakas.getAsiakasId();
            int mokkiId = valittuMokki.getMokkiId();
            Varaus varaus = new Varaus(asiakasId, mokkiId, alkupaiva, loppupaiva);

            try {
                VaraustenHallinta hallinta = new VaraustenHallinta();
                hallinta.lisaaVaraus(varaus);
                varauksetLw.getItems().setAll(hallinta.getVaraukset());

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Varauksen tallentaminen epäonnistui. Tarkista syötteet ja yritä uudelleen.");
                alert.showAndWait();
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Virhe");
            alert.setHeaderText(null);
            alert.setContentText("Täytä kaikki kentät ennen varauksen tallentamista.");
            alert.showAndWait();
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
    private void btMuokkaa(ActionEvent event) {
        Varaus varaus = varauksetLw.getSelectionModel().getSelectedItem();
        if (varaus != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MuokkaaVaraustaIkkuna.fxml"));
                Parent parent = fxmlLoader.load();
                MuokkaaVaraustaController controller = fxmlLoader.getController();
                controller.setVaraus(varaus);
                Scene scene = new Scene(parent, 800, 400);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void handleVarausSelection() {
        Varaus varaus = varauksetLw.getSelectionModel().getSelectedItem();
        if (varaus != null) {
            try {
                List<Palvelu> varauksenPalvelut = VaraustenHallinta.getVarauksenPalvelut(varaus.getVarausId());
                Map<String, Integer> palveluidenLukumaarat = new HashMap<>();
                for (Palvelu palvelu : varauksenPalvelut) {
                    String palvelunNimi = palvelu.getNimi();
                    palveluidenLukumaarat.put(palvelunNimi, palveluidenLukumaarat.getOrDefault(palvelunNimi, 0) + 1);
                }

                StringBuilder palvelutString = new StringBuilder();
                for (Map.Entry<String, Integer> entry : palveluidenLukumaarat.entrySet()) {
                    palvelutString.append(entry.getKey()).append(" (").append(entry.getValue()).append(" kpl)").append(", ");
                }
                if (palvelutString.length() > 0) {
                    palvelutString.setLength(palvelutString.length() - 2); // poistaa viimeisen pilkun ja välilyönnin
                } else {
                    palvelutString.append("-");
                }

                // Muotoillaan päivämäärät muotoon pp.kk.vvvv
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String alkupaivaMuotoiltu = varaus.getVarattuAlkupvm().format(formatter);
                String loppupaivaMuotoiltu = varaus.getVarattuLoppupvm().format(formatter);

                String varausTiedot = "Varaus ID: " + varaus.getVarausId() + "\n" +
                        "Asiakas ID: " + varaus.getAsiakasId() + "\n" +
                        "Mökki ID: " + varaus.getMokkiId() + "\n" +
                        "Alkupäivä: " + alkupaivaMuotoiltu + "\n" +
                        "Loppupäivä: " + loppupaivaMuotoiltu + "\n" +
                        "Varauksen palvelut: " + palvelutString.toString(); // Lisätään palvelut tekstin loppuun
                varausTa.setText(varausTiedot);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            varausTa.setText("");
        }
    }

    @FXML
    private void initialize() {
        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            varauksetLw.getItems().setAll(hallinta.getVaraukset());
            varauksetLw.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleVarausSelection());
            lataaAsiakkaat();
            lataaMokit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        asiakasCb.setOnMouseClicked(event -> lataaAsiakkaat());
        mokkiCb.setOnMouseClicked(event -> lataaMokit());
    }

}



