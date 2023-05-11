package com.example.mokit_r31;

import java.sql.*;

import com.example.mokit_r31.Tietokanta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public class PalvelutRaportti {


    public static void lataaAlueetComboBoxiin(ComboBox<String> cbAlue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT nimi FROM alue";

        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String alueNimi = rs.getString("nimi");
                cbAlue.getItems().add(alueNimi);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Tietokanta.sulje(rs, stmt, conn);
        }
    }

    public static void haePalveluRaportti(String valittuAlue, String alkupaiva, String loppupaiva, ListView<String> palvelutLw) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT palvelu.nimi, COUNT(varauksen_palvelut.palvelu_id) AS lukumaara, SUM(palvelu.hinta) AS kokonaishinta " +
                "FROM palvelu " +
                "LEFT JOIN varauksen_palvelut ON palvelu.palvelu_id = varauksen_palvelut.palvelu_id " +
                "LEFT JOIN varaus ON varaus.varaus_id = varauksen_palvelut.varaus_id " +
                "LEFT JOIN mokki ON mokki.mokki_id = varaus.mokki_mokki_id " +
                "WHERE mokki.alue_id = (SELECT alue_id FROM alue WHERE nimi = ?) AND varaus.varattu_alkupvm BETWEEN ? AND ? " +
                "GROUP BY palvelu.nimi";

        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, valittuAlue);
            stmt.setString(2, alkupaiva);
            stmt.setString(3, loppupaiva);
            rs = stmt.executeQuery();

            ObservableList<String> palvelut = FXCollections.observableArrayList();

            while (rs.next()) {
                String nimi = rs.getString("nimi");
                int lukumaara = rs.getInt("lukumaara");
                double kokonaishinta = rs.getDouble("kokonaishinta");

                palvelut.add(nimi + " - " + lukumaara + " kpl - " + kokonaishinta + " €");
            }

            palvelutLw.setItems(palvelut);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Tietokanta.sulje(rs, stmt, conn);
        }
    }
}
