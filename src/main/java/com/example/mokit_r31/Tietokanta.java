package com.example.mokit_r31;

import java.sql.*;

/** Tietokantayhteyttä hallinnoiva luokka.
 * Muodostaa yhteyden MySQL-tietokantaan.
 * Sulkee yhteyden.
 */
public class Tietokanta {
    private static final String TIETOKANNAN_NIMI = "jdbc:mysql://localhost:3306/vn";
    private static final String KAYTTAJA = "root";
    private static final String SALASANA = "R31_mokki";

    //private static final String SALASANA = "Kamilrakas91!";



    // TAIJAN VERSIO, koska en saa muutettua tietokannan kirjautumistietoja
    //private static final String SALASANA = "admin";
    // kommentoi tämä rivi pois ja ota ylempi salasana käyttöön

    public static Connection getYhteys() throws SQLException {
        return DriverManager.getConnection(TIETOKANNAN_NIMI, KAYTTAJA, SALASANA);
    }

    public static void sulje(Connection yhteys) {
        if (yhteys != null) {
            try {
                yhteys.close();
            } catch (SQLException ex) {
                // Ohitetaan poikkeus
            } }
    }

    public static void sulje(Statement kysely, Connection yhteys) {
        if (kysely != null) {
            try {
                kysely.close();
            } catch (SQLException ex) {
                // Ohitetaan poikkeus
            }
        }
        sulje(yhteys);
    }

    public static void sulje(ResultSet tulokset, Statement kysely, Connection yhteys) {
        if (tulokset != null) {
            try {
                tulokset.close();
            } catch (SQLException ex) {
                // Ohitetaan poikkeus
            }
        }
        sulje(yhteys);
    }
}