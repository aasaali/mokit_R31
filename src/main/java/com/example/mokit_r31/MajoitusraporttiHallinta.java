package com.example.mokit_r31;

import java.sql.*;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

public class MajoitusraporttiHallinta {

    public static void lataaValinnatComboBoxiin(ComboBox<String> cbAlue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT alue_id FROM alue";


        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int alueId = rs.getInt("alue_id");
                cbAlue.getItems().addAll(Integer.toString(alueId));
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Tietokanta.sulje(rs, stmt, conn);
        }
    }



    public static ArrayList<String> haeVaraus(String valittuAlue, String alkupaiva, String loppupaiva) {
        ArrayList<String> varaukset = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT m.mokkinimi, v.varattu_alkupvm, v.varattu_loppupvm " +
                "FROM vn.mokki m " +
                "INNER JOIN vn.varaus v ON m.mokki_id = v.mokki_mokki_id " +
                "WHERE m.alue_id = ? AND v.varattu_loppupvm > ? AND v.varattu_alkupvm < ?;";

        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, valittuAlue);
            stmt.setString(2, alkupaiva);
            stmt.setString(3, loppupaiva);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String mokkiNimi = rs.getString("mokkinimi");
                Date varattuAlkupvm = rs.getDate("varattu_alkupvm");
                Date varattuLoppupvm = rs.getDate("varattu_loppupvm");
                String rivi = mokkiNimi + " - Varattu " + varattuAlkupvm.toString() + " - " + varattuLoppupvm.toString();
                varaukset.add(rivi);
            }

            if (varaukset.isEmpty()) {
                throw new SQLException();
            }

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ei varauksia annetuilla hakuehdoilla.");
            String virheviesti = "Tyhjät mökit seisovat rivissä\n" +
                    "Kesän tuulet ne puhaltavat kivissä\n" +
                    "Ei ääntä, ei valoa, ei eloa\n" +
                    "Vain hiljaisuus vallitsee yksinäisyydessään.";
            alert.setHeaderText(virheviesti);
            alert.showAndWait();
            ex.printStackTrace();
        } finally {
            Tietokanta.sulje(rs, stmt, conn);
        }

        return varaukset;
    }


}

