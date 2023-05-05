package com.example.mokit_r31;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;




public class PalveluController extends PalvelujenHallinta {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/vn";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "x!";
    private static Connection conn;



    PalvelujenHallinta hallinta = new PalvelujenHallinta();

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
        private ListView<?> palveluLista;

        @FXML
        private TextField palveluTyyppiTf;

        @FXML
        private TextField palveluNimiTf;

        @FXML
        private Button poistaPalveluBt;

        @FXML
        private Button tallennaPalveluBt;



    @FXML
    private void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("uudempiPalvelu.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    // luoPalveluBt tapahtumankäsittelijä
        luoPalveluBt.setOnAction(e -> {
        int id = Integer.parseInt(palveluIDTf.getText());
        int alueId = Integer.parseInt(alueenPostinumeroTf.getText());
        String nimi = palveluNimiTf.getText();
        int tyyppi = Integer.parseInt(palveluTyyppiTf.getText());
        String kuvaus = palveluKuvausTf.getText();
        double hinta = Double.parseDouble(palveluHintaTf.getText());
        double alv = Double.parseDouble(palveluArvonlisaveroTf.getText());

        // Luo uusi Palvelu-olio
        Palvelu uusiPalvelu = new Palvelu(id, alueId, nimi, tyyppi, kuvaus, hinta, alv);

            // Tarkista, että uusiPalvelu-olio on luotu
            if (uusiPalvelu != null) {
                // Tallenna uusiPalvelu-olion tiedot tietokantaan
                lisaaPalvelu(uusiPalvelu.getAlueId(), uusiPalvelu.getNimi(), uusiPalvelu.getTyyppi(), uusiPalvelu.getKuvaus(), uusiPalvelu.getHinta());
                System.out.println("Palvelu lisätty tietokantaan.");
            } else {
                System.out.println("Uutta palvelua ei ole vielä luotu.");
            }
        });





                naytaPalveluBt.setOnAction(e -> {
                    // Kutsu metodia "naytaPalvelut" tässä
                   hallinta.naytaPalvelut();
                });

               poistaPalveluBt.setOnAction(e -> {
                    // Kutsu metodia "poistaPalvelu" tässä
                  // hallinta.poistaPalvelu(1);
                });

               //tallennaPalveluBt.setOnAction(e -> {
                  // {
                   //    int alueId = Integer.parseInt(alueenPostinumeroTf.getText());
                       String nimi = palveluNimiTf.getText();
                       int tyyppi = Integer.parseInt(palveluIDTf.getText());
                       String kuvaus = palveluKuvausTf.getText();
                       double hinta = Double.parseDouble(palveluHintaTf.getText());
                       double alv = Double.parseDouble(palveluArvonlisaveroTf.getText());}}

                       //  Palvelu palvelu = new Palvelu(alueId, nimi, tyyppi, kuvaus, hinta, alv);
                       //   try {
                       //   PalvelujenHallinta hallinta = new PalvelujenHallinta();
                       //    hallinta.lisaaPalvelu(palvelu);
                       //  tallennaPalveluBt.setText("Vahvistettu");
                       //     } catch (SQLException e) };
                       //  }}}

// Kutsu metodia "muokkaaPalvelua" tässä
// hallinta.muokkaaPalvelua(1, 2, "palvelun nimi", 3, "kuvaus", 3.5);


