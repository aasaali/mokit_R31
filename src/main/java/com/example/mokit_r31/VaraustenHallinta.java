package com.example.mokit_r31;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VaraustenHallinta extends Application {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/vn";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "R31_mokki";
    private static Connection conn;
    @Override
    public void start(Stage stage) throws IOException {

    }

    //Metodi, joka lukee varaukset tietokannasta ja luo niistä listan.
    public List<Varaus> getVaraukset() throws SQLException {
        List<Varaus> varaukset = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM varaus")) {

            while (rs.next()) {
                Varaus varaus = new Varaus();
                varaus.setVarausId(rs.getInt("varaus_id"));
                varaus.setAsiakasId(rs.getInt("asiakas_id"));
                varaus.setMokkiId(rs.getInt("mokki_mokki_id"));
                varaus.setVarattuPvm(rs.getTimestamp("varattu_pvm").toLocalDateTime());
                varaus.setVahvistusPvm(rs.getTimestamp("vahvistus_pvm").toLocalDateTime());
                varaus.setVarattuAlkupvm(rs.getTimestamp("varattu_alkupvm").toLocalDateTime());
                varaus.setVarattuLoppupvm(rs.getTimestamp("varattu_loppupvm").toLocalDateTime());
                varaukset.add(varaus);
            }
        }

        return varaukset;
    }

    // Metodi, joka lisää uuden varauksen tietokantaan
    public void lisaaVaraus(Varaus varaus) throws SQLException {
        String sql = "INSERT INTO varaus(asiakas_id, mokki_mokki_id, varattu_pvm, vahvistus_pvm, varattu_alkupvm, varattu_loppupvm) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, varaus.getAsiakasId());
            stmt.setInt(2, varaus.getMokkiId());
            stmt.setTimestamp(3, Timestamp.valueOf(varaus.getVarattuPvm()));
            stmt.setTimestamp(4, Timestamp.valueOf(varaus.getVahvistusPvm()));
            stmt.setTimestamp(5, Timestamp.valueOf(varaus.getVarattuAlkupvm()));
            stmt.setTimestamp(6, Timestamp.valueOf(varaus.getVarattuLoppupvm()));
            stmt.executeUpdate();
        }
    }

    //Metodi, joka poistaa varauksen tietokannasta.
    public void poistaVaraus(int varausId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM varaus WHERE varaus_id = ?")) {
            stmt.setInt(1, varausId);
            stmt.executeUpdate();
        }
    }

    // Metodi, joka muokkaa varauksen tietokannassa
    public void muokkaaVarausta(Varaus varaus) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE varaus SET asiakas_id = ?, mokki_mokki_id = ?, varattu_pvm = ?, vahvistus_pvm = ?, varattu_alkupvm = ?, varattu_loppupvm = ? WHERE varaus_id = ?")) {
            stmt.setInt(1, varaus.getAsiakasId());
            stmt.setInt(2, varaus.getMokkiId());
            stmt.setTimestamp(3, Timestamp.valueOf(varaus.getVarattuPvm()));
            stmt.setTimestamp(4, Timestamp.valueOf(varaus.getVahvistusPvm()));
            stmt.setTimestamp(5, Timestamp.valueOf(varaus.getVarattuAlkupvm()));
            stmt.setTimestamp(6, Timestamp.valueOf(varaus.getVarattuLoppupvm()));
            stmt.setInt(7, varaus.getVarausId());
            stmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

