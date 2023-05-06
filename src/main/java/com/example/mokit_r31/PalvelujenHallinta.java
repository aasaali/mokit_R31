package com.example.mokit_r31;
import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;


public class PalvelujenHallinta {

    private static Tietokanta tietokanta;

    public PalvelujenHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;
    }

    public void lisaaPalvelu(Palvelu palvelu) throws SQLException {
        Connection yhteys = tietokanta.getYhteys();
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO palvelu (id, alue_id, nimi, tyyppi, kuvaus, hinta, alv) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            pstmt = yhteys.prepareStatement(sql);
            pstmt.setInt(1, palvelu.getId());
            pstmt.setInt(2, palvelu.getAlueId());
            pstmt.setString(3, palvelu.getNimi());
            pstmt.setInt(4, palvelu.getTyyppi());
            pstmt.setString(5, palvelu.getKuvaus());
            pstmt.setDouble(6, palvelu.getHinta());
            pstmt.setDouble(7, palvelu.getAlv());
            pstmt.executeUpdate();
        } finally {
            tietokanta.sulje(pstmt, yhteys);
        }}


    public void tallennaPalvelu(Palvelu palvelu) throws SQLException {
        Connection yhteys = tietokanta.getYhteys();
        PreparedStatement lisayslause = null;
        try {
            String sql = "INSERT INTO Palvelu (id, alue_id, nimi, tyyppi, kuvaus, hinta, alv) VALUES (?, ?, ?, ?, ?, ?, ?)";
            lisayslause = yhteys.prepareStatement(sql);
            lisayslause.setInt(1, palvelu.getId());
            lisayslause.setInt(2, palvelu.getAlueId());
            lisayslause.setString(3, palvelu.getNimi());
            lisayslause.setInt(4, palvelu.getTyyppi());
            lisayslause.setString(5, palvelu.getKuvaus());
            lisayslause.setDouble(6, palvelu.getHinta());
            lisayslause.setDouble(7, palvelu.getAlv());
            lisayslause.executeUpdate();
        } finally {
            if (lisayslause != null) {
                lisayslause.close();
            }
            if (yhteys != null) {
                yhteys.close();
            }
        }
    }


    public List<Palvelu> naytaPalvelu() {
        Connection yhteys = null;
        Statement kysely = null;
        ResultSet tulokset = null;
        List<Palvelu> palvelut = new ArrayList<>();

        try {
            yhteys = tietokanta.getYhteys();
            kysely = yhteys.createStatement();
            tulokset = kysely.executeQuery("SELECT * FROM palvelu");

            while (tulokset.next()) {
                int id = tulokset.getInt("palvelu_id");
                int alueId = tulokset.getInt("alue_id");
                String nimi = tulokset.getString("nimi");
                int tyyppi = tulokset.getInt("tyyppi");
                String kuvaus = tulokset.getString("kuvaus");
                double hinta = tulokset.getDouble("hinta");
                double alv = tulokset.getDouble("alv");

                Palvelu palvelu = new Palvelu(id, alueId, nimi, tyyppi, kuvaus, hinta, alv);
                palvelut.add(palvelu);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            tietokanta.sulje(tulokset, kysely, yhteys);
        }

        return palvelut;
    }

    }







