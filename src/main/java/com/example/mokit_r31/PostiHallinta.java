package com.example.mokit_r31;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Posti-luokkaa ja tietokannan taulua hallinnoiva luokka.
 * Toiminnot: tallenna, poista, muokkaa, hae, hae kaikki, hae postinumerot.
 */
@SuppressWarnings("unused")
public class PostiHallinta {
    public PostiHallinta(Tietokanta tietokanta) {
    }

    public void tallennaPosti(Posti posti) throws SQLException {
        Connection yhteys = Tietokanta.getYhteys();
        String sql = "INSERT INTO posti (postinro, toimipaikka) VALUES (?, ?)";
        try (PreparedStatement stmt = yhteys.prepareStatement(sql)) {
            stmt.setString(1, posti.getPostinro());
            stmt.setString(2, posti.getToimipaikka());
            stmt.executeUpdate();
        }
    }

    public void poistaPosti(Posti posti) throws SQLException {
        Connection yhteys = Tietokanta.getYhteys();
        String sql = "DELETE FROM posti WHERE postinro=?";
        try (PreparedStatement stmt = yhteys.prepareStatement(sql)) {
            stmt.setString(1, posti.getPostinro());
            stmt.executeUpdate();
        }
    }

    public void muokkaaPostia(Posti vanhaPosti, Posti uusiPosti) throws SQLException {
        Connection yhteys = Tietokanta.getYhteys();
        String sql = "UPDATE posti SET postinro=?, toimipaikka=? WHERE postinro=?";
        try (PreparedStatement stmt = yhteys.prepareStatement(sql)) {
            stmt.setString(1, uusiPosti.getPostinro());
            stmt.setString(2, uusiPosti.getToimipaikka());
            stmt.setString(3, vanhaPosti.getPostinro());
            stmt.executeUpdate();
        }
    }

    public Posti haePosti(String postinro) throws SQLException {
        Connection yhteys = Tietokanta.getYhteys();
        String sql = "SELECT * FROM posti WHERE postinro=?";
        try (PreparedStatement stmt = yhteys.prepareStatement(sql)) {
            stmt.setString(1, postinro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Posti(rs.getString("postinro"), rs.getString("toimipaikka"));
                } else {
                    return null;
                }
            }
        }
    }

    public List<Posti> haeKaikkiPostit() throws SQLException {
        Connection yhteys = Tietokanta.getYhteys();
        String sql = "SELECT * FROM posti";
        try (PreparedStatement stmt = yhteys.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Posti> postit = new ArrayList<>();
            while (rs.next()) {
                Posti posti = new Posti(rs.getString("postinro"), rs.getString("toimipaikka"));
                postit.add(posti);
            }
            return postit;
        }
    }

    public List<String> haePostinumerot() {
        List<String> postinumerot = new ArrayList<>();

        try {
            Connection conn = Tietokanta.getYhteys();
            String sql = "SELECT postinro FROM posti";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String postinro = rs.getString("postinro");
                postinumerot.add(postinro);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return postinumerot;
    }
}
