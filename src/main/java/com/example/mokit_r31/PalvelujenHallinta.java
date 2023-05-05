package com.example.mokit_r31;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;

public class PalvelujenHallinta extends Application {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/vn";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Kamilrakas91!";
    private static Connection conn;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("uudempiPalvelu.fxml"));
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Tietokantaan ei saatu yhteyttä: " + e.getMessage());
        }
    }

    // Metodi palvelun lisäämiseen tietokantaan
    public void lisaaPalvelu(int postinumero, String nimi, int tyyppi, String kuvaus, double hinta) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Palvelu (postinumero, nimi, tyyppi, kuvaus, hinta, alv) " +
                            "VALUES (?, ?, ?, ?, ?, ?)"
            );
            stmt.setInt(1, postinumero);
            stmt.setString(2, nimi);
            stmt.setInt(3, tyyppi);
            stmt.setString(4, kuvaus);
            stmt.setDouble(5, hinta);
            stmt.setDouble(6, hinta * 0.24); // Lasketaan arvonlisävero automaattisesti
            stmt.executeUpdate();
            System.out.println("Palvelu lisätty tietokantaan.");
        } catch (SQLException e) {
            System.out.println("Virhe lisättäessä palvelua: " + e.getMessage());
        }
    }

    // Metodi palveluiden hakemiseen tietokannasta ja tulostamiseen listana


    public List<Palvelu> getPalvelut() throws SQLException {
        List<Palvelu> palvelut = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM palvelu")) {

            while (rs.next()) {
                Palvelu palvelu = new Palvelu(
                        rs.getInt("palvelu_id"),
                        rs.getInt("alue_id"),
                        rs.getString("nimi"),
                        rs.getInt("tyyppi"),
                        rs.getString("kuvaus"),
                        rs.getDouble("hinta"),
                        rs.getDouble("alv")
                );
                palvelut.add(palvelu);
            }
        } catch (SQLException e) {
            System.out.println("Virhe haettaessa palveluita: " + e.getMessage());
        }

        return palvelut;
    }

    // Metodi, joka poistaa palvelun tietokannasta
    public void poistaPalvelu(int palveluId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM palvelu WHERE palvelu_id = ?")) {
            stmt.setInt(1, palveluId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodi, joka muokkaa palvelun tietoja tietokannassa
    public void muokkaaPalvelua(Palvelu palvelu) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE palvelu SET alue_id = ?, nimi = ?, tyyppi = ?, kuvaus = ?, hinta = ?, alv = ? WHERE palvelu_id = ?")) {
            stmt.setInt(1, palvelu.getAlueId());
            stmt.setString(2, palvelu.getNimi());
            stmt.setInt(3, palvelu.getTyyppi());
            stmt.setString(4, palvelu.getKuvaus());
            stmt.setDouble(5, palvelu.getHinta());
            stmt.setDouble(6, palvelu.getAlv());
            stmt.setInt(7, palvelu.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void naytaPalvelut() {
        List<Palvelu> palvelut;

        try {
            palvelut = getPalvelut();
            // Tee jotain palvelu-listalle, esim. tulosta se
            for (Palvelu palvelu : palvelut) {
                System.out.println(palvelu.toString());
            }
        } catch (SQLException e) {
            System.out.println("Virhe haettaessa palveluita: " + e.getMessage());
        }
    }


}
