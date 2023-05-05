package com.example.mokit_r31;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MokkienHallinta {
    private Tietokanta tietokanta;
    public MokkienHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;}

// Mökin tietojen lisääminen tietokantaan SQL INSERT
public void lisaaMokki(Mokki newMokki) throws SQLException {
    // Tietokannan yhteysosoite

    try {
        Connection conn = tietokanta.getYhteys();
        {
            System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

            // Luodaan SQL-lause, jolla mökin tiedot lisätään tietokantaan
            String sql = "INSERT INTO mokki (mokki_id, alue_id, postinro, mokkinimi, katuosoite, hinta, kuvaus, henkilomaara, varustelu)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Asetetaan SQL-lauseelle arvot
            stmt.setInt(1, newMokki.getMokkiId());
            stmt.setInt(2, newMokki.getAlueId());
            stmt.setInt(3, newMokki.getPostiNro());
            stmt.setString(4, newMokki.getNimi());
            stmt.setString(5, newMokki.getOsoite());
            stmt.setDouble(6, newMokki.getHinta());
            stmt.setString(7, newMokki.getKuvaus());
            stmt.setInt(8, newMokki.getHlo());
            stmt.setString(9, newMokki.getVarustelu());


            // Suoritetaan SQL-lause
            stmt.executeUpdate();
            // Suljetaan tietokantayhteys ja vapautetaan resurssit
            stmt.close();
            conn.close();
        }
    } catch (SQLException e) {
        System.out.println("Yhteys tietokantaan epäonnistui!");
        e.printStackTrace();
    }

}

// Mökin tietojen hakeminen tietokannasta SQL SELECT>
public static List<Mokki> haeMokit(String hakusana) {
    // Tietokannan yhteysosoite
    String url = "jdbc:mysql://localhost:3306/vn";
    // Käyttäjän tunnus ja salasana
    String user = "root";
    String password = "Heleppohomma23?3";

    List<Mokki> mokit = new ArrayList<>();

    try {
        Connection conn = DriverManager.getConnection(url, user, password);
        {
            System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

            // Luodaan SQL-lause, jolla mökit haetaan tietokannasta
            String sql = "SELECT * FROM mokki WHERE mokkinimi LIKE ? OR hinta LIKE ? OR varustelu LIKE ? OR alue_id IN (SELECT alue_id FROM alue WHERE aluenimi LIKE ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Asetetaan hakusana SQL-lauseelle
            String hakusanaLike = "%" + hakusana + "%";
            stmt.setString(1, hakusanaLike);
            stmt.setString(2, hakusanaLike);
            stmt.setString(3, hakusanaLike);
            stmt.setString(4, hakusanaLike);

            // Suoritetaan SQL-lause ja käydään tulokset läpi
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int mokkiId = rs.getInt("mokki_id");
                int alueId = rs.getInt("alue_id");
                int postiNro = rs.getInt("postinro");
                String nimi = rs.getString("mokkinimi");
                String osoite = rs.getString("katuosoite");
                double hinta = rs.getDouble("hinta");
                String kuvaus = rs.getString("kuvaus");
                int hlo = rs.getInt("henkilomaara");
                String varustelu = rs.getString("varustelu");

                Mokki hakuMokki = new Mokki(mokkiId, alueId, postiNro, nimi, osoite, hinta, kuvaus, hlo, varustelu);
                mokit.add(hakuMokki);
            }

            // Suljetaan tietokantayhteys ja vapautetaan resurssit
            rs.close();
            stmt.close();
            conn.close();
        }
    } catch (SQLException e) {
        System.out.println("Yhteys tietokantaan epäonnistui!");
        e.printStackTrace();
    }

    return mokit;
}

// Mökin tietojen muokkaaminen tietokannassa SQL UPDATE
    public static void muokkaaMokkia(Mokki mokki) {
        // Tietokannan yhteysosoite
        String url = "jdbc:mysql://localhost:3306/vn";
        // Käyttäjän tunnus ja salasana
        String user = "root";
        String password = "Heleppohomma23?3";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            {
                System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

                // Luodaan SQL-lause, jolla päivitetään mökin tiedot tietokantaan
                String sql = "UPDATE mokki SET alue_id = ?, postinro = ?, mokkinimi = ?, katuosoite = ?, hinta = ?, kuvaus = ?, henkilomaara = ?, varustelu = ? WHERE mokki_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Asetetaan SQL-lauseelle arvot
                stmt.setInt(1, mokki.getAlueId());
                stmt.setInt(2, mokki.getPostiNro());
                stmt.setString(3, mokki.getNimi());
                stmt.setString(4, mokki.getOsoite());
                stmt.setDouble(5, mokki.getHinta());
                stmt.setString(6, mokki.getKuvaus());
                stmt.setInt(7, mokki.getHlo());
                stmt.setString(8, mokki.getVarustelu());
                stmt.setInt(9, mokki.getMokkiId());

                // Suoritetaan SQL-lause
                stmt.executeUpdate();

                // Suljetaan tietokantayhteys ja vapautetaan resurssit
                stmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Yhteys tietokantaan epäonnistui!");
            e.printStackTrace();
        }
    }

// Mökin tietojen poistaminen tietokannasta SQL DELETE
public static void poistaMokki(int mokkiId) {
    // Tietokannan yhteysosoite
    String url = "jdbc:mysql://localhost:3306/vn";
    // Käyttäjän tunnus ja salasana
    String user = "root";
    String password = "Heleppohomma23?3";

    try {
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

        // Luodaan SQL-lause, jolla poistetaan mökki tietokannasta
        String sql = "DELETE FROM mokki WHERE mokki_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, mokkiId);

        // Suoritetaan SQL-lause
        stmt.executeUpdate();

        // Suljetaan tietokantayhteys ja vapautetaan resurssit
        stmt.close();
        conn.close();

        System.out.println("Mökki poistettu tietokannasta.");
    } catch (SQLException e) {
        System.out.println("Mökin poistaminen tietokannasta epäonnistui!");
        e.printStackTrace();
    }
}
    public static List<Mokki> haeKaikkiMokit() {
        // Tietokannan yhteysosoite
        String url = "jdbc:mysql://localhost:3306/vn";
        // Käyttäjän tunnus ja salasana
        String user = "root";
        String password = "Heleppohomma23?3";

        List<Mokki> mokit = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            {
                System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

                // Luodaan SQL-lause, jolla mökit haetaan tietokannasta
                String sql = "SELECT * FROM mokki";
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Suoritetaan SQL-lause ja käydään tulokset läpi
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int mokkiId = rs.getInt("mokki_id");
                    int alueId = rs.getInt("alue_id");
                    int postiNro = rs.getInt("postinro");
                    String nimi = rs.getString("mokkinimi");
                    String osoite = rs.getString("katuosoite");
                    double hinta = rs.getDouble("hinta");
                    String kuvaus = rs.getString("kuvaus");
                    int hlo = rs.getInt("henkilomaara");
                    String varustelu = rs.getString("varustelu");

                    Mokki hakuMokki = new Mokki(mokkiId, alueId, postiNro, nimi, osoite, hinta, kuvaus, hlo, varustelu);
                    mokit.add(hakuMokki);
                }

                // Suljetaan tietokantayhteys ja vapautetaan resurssit
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Yhteys tietokantaan epäonnistui!");
            e.printStackTrace();
        }

        return mokit;
    }


    public static void main(String[] args) throws SQLException {
    Mokki testiMokki = new Mokki(345, 123, 70100, "Elämysmökki", "kotikatu", 500,"upea mökki keskellä kaupunkia", 5, "sauna" );

    }
}