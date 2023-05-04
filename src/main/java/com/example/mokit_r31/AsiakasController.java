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

        // Lisää tapahtumankäsittelijä jokaiselle listan elementille
        asiakasLista.setCellFactory(param -> {
            ListCell<Asiakas> cell = new ListCell<>() {
                @Override
                protected void updateItem(Asiakas asiakas, boolean empty) {
                    super.updateItem(asiakas, empty);
                    if (empty || asiakas == null) {
                            setText(null);
                        } else {
                            setText(asiakas.getEtunimi() + " " + asiakas.getSukunimi());
                        }
                    }
                };

                cell.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !cell.isEmpty()) {
                        Asiakas asiakas = cell.getItem();
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
                });

                return cell;
            });
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

