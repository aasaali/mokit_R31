package com.example.mokit_r31;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/** Asiakas- ja AsiakasHallinta -luokkia hallinnoiva controller.
 * Toiminnot: hakee asiakkaat tietokannasta ja listaa ne käyttöliittymään,
 * näyttää listalta valitun asiakkaan tiedot käyttöliittymässä,
 * asiakkaan tietojen muokkaus tuplaklikkaamalla aukeavassa ikkunassa, jolloin muutokset tallennetaan tietokantaan,
 * sekä asiakkaan poistaminen tietokannasta.
 */
@SuppressWarnings(value = "unused")
public class AsiakasController {

    public Button btMuokkaa;
    @FXML private ListView<Asiakas> asiakasLista;
    @FXML private Button buttonHae;
    @FXML private Button buttonTallenna;
    @FXML private Button buttonPoista;
    @FXML private TextArea taAsiakkaanTiedot;
    @FXML private TextField tfPostinumero;
    @FXML private TextField tfEtunimi;
    @FXML private TextField tfSukunimi;
    @FXML private TextField tfLahiosoite;
    @FXML private TextField tfEmail;
    @FXML private TextField tfPuhnro;
    Tietokanta tietokanta;
    AsiakasHallinta asiakasHallinta = new AsiakasHallinta(tietokanta);
    @FXML
    private void naytaTiedot(MouseEvent event) throws SQLException {

        Asiakas valinta = asiakasLista.getSelectionModel().getSelectedItem();

        if (valinta != null) {
            int asiakasid = valinta.getAsiakasId();
            AsiakasHallinta ah = new AsiakasHallinta(tietokanta);
            Asiakas asiakas = ah.haeAsiakas(asiakasid);
            String asiakasTiedot = "Asiakas ID: " + asiakas.getAsiakasId() + "\n"
                    + "Nimi: " + asiakas.getEtunimi() + " " + asiakas.getSukunimi() + "\n"
                    + "Lähiosoite: " + asiakas.getLahiosoite() + "\n"
                    + "Postinumero: " + asiakas.getPostinro() +"\n"
                    + "Email: " + asiakas.getEmail() +"\n"
                    + "Puhelinnumero: " + asiakas.getPuhelinnro();
                taAsiakkaanTiedot.setText(asiakasTiedot);
        } else {taAsiakkaanTiedot.setText("");}
    }

    @FXML private void muokkaaAsiakasta(ActionEvent event) {
        Asiakas asiakas = asiakasLista.getSelectionModel().getSelectedItem();
        if (asiakas != null) {
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
        haeTietokannasta();
    }

    private void haeTietokannasta() {
        AsiakasHallinta ashallinta = new AsiakasHallinta(tietokanta);
        asiakasLista.getItems().setAll(ashallinta.haeKaikkiAsiakkaat());
    }

    @FXML
    private void initialize() {
        // Hae kaikki asiakkaat tietokannasta ja aseta listview:iin
        AsiakasHallinta ashallinta = new AsiakasHallinta(tietokanta);
        asiakasLista.getItems().setAll(ashallinta.haeKaikkiAsiakkaat());

        asiakasLista.setOnMouseClicked(event -> {
            try {
                naytaTiedot(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        buttonHae.setOnAction(e -> haeTietokannasta());

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

            haeTietokannasta();
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
                        ObservableList<Asiakas> asiakkaatTietokannasta =
                                FXCollections.observableArrayList(asiakasHallinta.haeKaikkiAsiakkaat());
                        asiakasLista.setItems(asiakkaatTietokannasta);
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

