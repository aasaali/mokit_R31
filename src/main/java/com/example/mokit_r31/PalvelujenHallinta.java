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
            String sql = "INSERT INTO palvelu (alue_id, nimi, tyyppi, kuvaus, hinta, alv) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            pstmt = yhteys.prepareStatement(sql);
            pstmt.setInt(1, palvelu.getAlueId());
            pstmt.setString(2, palvelu.getNimi());
            pstmt.setInt(3, palvelu.getTyyppi());
            pstmt.setString(4, palvelu.getKuvaus());
            pstmt.setDouble(5, palvelu.getHinta());
            pstmt.setDouble(6, palvelu.getAlv());
            pstmt.executeUpdate();
        } finally {
            tietokanta.sulje(pstmt, yhteys);
        }}



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







