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
import java.util.Optional;

public class AsiakasController {
    @FXML
    private ListView<Asiakas> asiakasLista;
    @FXML
    private Button buttonHae;
    @FXML
    private Button buttonTallenna;
    @FXML
    private Button buttonPoista;
    @FXML
    private TextField tfAsiakasId;
    @FXML
    private TextField tfPostinumero;
    @FXML
    private TextField tfEtunimi;
    @FXML
    private TextField tfSukunimi;
    @FXML
    private TextField tfLahiosoite;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfPuhnro;

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
                            setText(asiakas.getEtunimi() + " " + asiakas.getSukunimi() + " - asiakas ID: "
                                    + asiakas.getAsiakasId());
                        }
                    }
                };
            cell.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !cell.isEmpty()) {
                        Asiakas asiakas = cell.getItem();
                        System.out.println(cell.getItem());
                        // Avaa uusi ikkuna tietojen muokkausta varten
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("MuokkaaAsiakasta.fxml"));
                            Parent root = loader.load();
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

        buttonHae.setOnAction(e -> {
            ObservableList<Asiakas> asiakkaatTietokannasta = FXCollections.observableArrayList(asiakasHallinta.haeKaikkiAsiakkaat());
            asiakasLista.setItems(asiakkaatTietokannasta);
        });

        buttonTallenna.setOnAction(e -> {
            String postinro = tfPostinumero.getText();
            String etunimi = tfEtunimi.getText();
            String sukunimi = tfSukunimi.getText();
            String lahiosoite = tfLahiosoite.getText();
            String email = tfEmail.getText();
            String puhelinnro = tfPuhnro.getText();

            Asiakas uusiAsiakas = new Asiakas(postinro, etunimi, sukunimi, lahiosoite, email, puhelinnro);
            try {
                asiakasHallinta.lisaaAsiakas(uusiAsiakas);
                asiakasLista.getItems().add(uusiAsiakas);
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Tallennus epäonnistui");
                alert.setHeaderText("Tallennus epäonnistui. Tarkista syöttämäsi tiedot.");
                alert.showAndWait();
            }
            tfPostinumero.clear();
            tfEtunimi.clear();
            tfSukunimi.clear();
            tfLahiosoite.clear();
            tfEmail.clear();
            tfPuhnro.clear();
        });

        buttonPoista.setOnAction(e -> {
            Asiakas valittuAsiakas = asiakasLista.getSelectionModel().getSelectedItem();
            if (valittuAsiakas != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Vahvista poisto");
                alert.setHeaderText("Haluatko varmasti poistaa asiakkaan " + valittuAsiakas.getEtunimi()
                        + " " + valittuAsiakas.getSukunimi() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        AsiakasHallinta.poistaAsiakas(valittuAsiakas.getAsiakasId());
                        asiakasLista.getItems().remove(valittuAsiakas);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Valitse asiakas");
                alert.setHeaderText("Valitse poistettava asiakas listasta.");
                alert.showAndWait();
            }
        });
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

