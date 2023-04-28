package com.example.mokit_r31;

import java.sql.*;

public class Tietokanta {
    private static final String TIETOKANTAN_NIMI = "tietokannan_nimi";
    private static final String KAYTTAJA = "kayttajatunnus";
    private static final String SALASANA = "salasana";

    public static Connection getYhteys() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/" + TIETOKANTAN_NIMI, KAYTTAJA, SALASANA);
    }

    public static void sulje(Connection yhteys) {
        if (yhteys != null) {
            try {
                yhteys.close();
            } catch (SQLException ex) {
                // Ohitetaan poikkeus
            }
        }
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

        sulje(kysely);
    }
}