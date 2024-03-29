package com.example.mokit_r31;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PalvelujenHallinta {
    //testi
    private static Tietokanta tietokanta;

    public PalvelujenHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;
    }

/*
    public void lisaaPalvelu(Palvelu palvelu) throws SQLException {
        Connection yhteys = tietokanta.getYhteys();
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO palvelu (alue_id, nimi, tyyppi, kuvaus, hinta, alv) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            pstmt = yhteys.prepareStatement(sql);
            pstmt.setInt(2, palvelu.getAlueId());
            pstmt.setString(3, palvelu.getNimi());
            pstmt.setInt(4, palvelu.getTyyppi());
            pstmt.setString(5, palvelu.getKuvaus());
            pstmt.setDouble(6, palvelu.getHinta());
            pstmt.setDouble(7, palvelu.getAlv());
            pstmt.executeUpdate();
        } finally {
            Tietokanta.sulje(pstmt, yhteys);
        }} */



    public List<Palvelu> naytaPalvelu() {
        Connection yhteys = null;
        Statement kysely = null;
        ResultSet tulokset = null;
        List<Palvelu> palvelut = new ArrayList<>();

        try {
            yhteys = Tietokanta.getYhteys();
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
            Tietokanta.sulje(tulokset, kysely, yhteys);
        }

        return palvelut;
    }

    public void muokkaaPalvelua(Palvelu palvelu) {
        try {
            Connection yhteys = Tietokanta.getYhteys();
            PreparedStatement paivitysLause = yhteys.prepareStatement(
                    "UPDATE palvelu SET alue_id=?, nimi=?, tyyppi=?, kuvaus=?, hinta=?, alv=? WHERE palvelu_id=?"
            );
            paivitysLause.setInt(1, palvelu.getAlueId());
            paivitysLause.setString(2, palvelu.getNimi());
            paivitysLause.setInt(3, palvelu.getTyyppi());
            paivitysLause.setString(4, palvelu.getKuvaus());
            paivitysLause.setDouble(5, palvelu.getHinta());
            paivitysLause.setDouble(6, palvelu.getAlv());
            paivitysLause.setInt(7, palvelu.getId());
            paivitysLause.executeUpdate();
            Tietokanta.sulje(yhteys);
        } catch (SQLException e) {
            System.err.println("Virhe tietokannan käsittelyssä: " + e.getMessage());
        }
    }



}









