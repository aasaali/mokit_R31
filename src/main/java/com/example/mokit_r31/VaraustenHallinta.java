package com.example.mokit_r31;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("VarausIkkuna.fxml"));
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    // Metodi, joka lukee varaukset tietokannasta ja luo niistä listan.
    public List<Varaus> getVaraukset() throws SQLException {
        List<Varaus> varaukset = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement("SELECT * FROM varaus");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Varaus varaus = new Varaus();
                varaus.setVarausId(rs.getInt("varaus_id"));
                varaus.setAsiakasId(rs.getInt("asiakas_id"));
                varaus.setMokkiId(rs.getInt("mokki_mokki_id"));

                Timestamp vahvistusPvmTimestamp = rs.getTimestamp("vahvistus_pvm");
                if (vahvistusPvmTimestamp != null) {
                    varaus.setVahvistusPvm(vahvistusPvmTimestamp.toLocalDateTime());
                }

                Timestamp varattuAlkupvmTimestamp = rs.getTimestamp("varattu_alkupvm");
                if (varattuAlkupvmTimestamp != null) {
                    varaus.setVarattuAlkupvm(varattuAlkupvmTimestamp.toLocalDateTime());
                }

                Timestamp varattuLoppupvmTimestamp = rs.getTimestamp("varattu_loppupvm");
                if (varattuLoppupvmTimestamp != null) {
                    varaus.setVarattuLoppupvm(varattuLoppupvmTimestamp.toLocalDateTime());
                }

                varaukset.add(varaus);
            }
        } finally {
            Tietokanta.sulje(rs, stmt, conn);
        }

        return varaukset;
    }

    // Metodi, joka lisää uuden varauksen tietokantaan
    public void lisaaVaraus(Varaus varaus) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Tietokanta.getYhteys();
            String sql = "INSERT INTO varaus(asiakas_id, mokki_mokki_id, varattu_pvm, vahvistus_pvm, varattu_alkupvm, varattu_loppupvm) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, varaus.getAsiakasId());
            stmt.setInt(2, varaus.getMokkiId());
            stmt.setTimestamp(3, Timestamp.valueOf(varaus.getVahvistusPvm()));
            stmt.setTimestamp(4, Timestamp.valueOf(varaus.getVahvistusPvm()));
            stmt.setTimestamp(5, Timestamp.valueOf(varaus.getVarattuAlkupvm()));
            stmt.setTimestamp(6, Timestamp.valueOf(varaus.getVarattuLoppupvm()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Tietokanta.sulje(stmt, conn);
        }
    }

// Metodi, joka poistaa varauksen tietokann

    public void poistaVaraus(int varausId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement("DELETE FROM varaus WHERE varaus_id = ?");
            stmt.setInt(1, varausId);
            stmt.executeUpdate();
        } finally {
            Tietokanta.sulje(stmt, conn);
        }
    }

    // Metodi, joka muokkaa varauksen tietokannassa
    public void muokkaaVarausta(Varaus varaus) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement("UPDATE varaus SET asiakas_id = ?, mokki_mokki_id = ?, varattu_alkupvm = ?, varattu_loppupvm = ? WHERE varaus_id = ?");
            stmt.setInt(1, varaus.getAsiakasId());
            stmt.setInt(2, varaus.getMokkiId());
            stmt.setTimestamp(3, Timestamp.valueOf(varaus.getVarattuAlkupvm()));
            stmt.setTimestamp(4, Timestamp.valueOf(varaus.getVarattuLoppupvm()));
            stmt.setInt(5, varaus.getVarausId());
            stmt.executeUpdate();
        } finally {
            Tietokanta.sulje(stmt, conn);
        }
    }

    public void lisaaPalveluVaraukselle(int varausId, int palveluId, int lkm) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Tietokanta.getYhteys();
            String sql = "INSERT INTO varauksen_palvelut(varaus_id, palvelu_id, lkm) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, varausId);
            stmt.setInt(2, palveluId);
            stmt.setInt(3, lkm);
            stmt.executeUpdate();
        } finally {
            Tietokanta.sulje(stmt, conn);
        }
    }



    public static void main(String[] args) {
        launch();
    }
}