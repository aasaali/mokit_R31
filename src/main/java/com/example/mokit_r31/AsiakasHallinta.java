package com.example.mokit_r31;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Asiakas-luokkaa hallinnoiva luokka. Tarjoaa metodeja AsiakasController-luokalle.
 * Käyttää MySQL-tietokantaa Tietokanta-luokan avulla.
 * Metodeja: lisää & hae & päivitä & poista asiakas, hae kaikki asiakkaat.
 */
public class AsiakasHallinta {
    Tietokanta tietokanta;
    public AsiakasHallinta(Tietokanta tietokanta) {this.tietokanta = tietokanta;
    }

    public List<Asiakas> haeAsiakkaat(String hakusana) {
        List<Asiakas> asiakkaat = new ArrayList<>();

        try {
            Connection conn = Tietokanta.getYhteys();
            {
                System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

                // Luodaan SQL-lause, jolla haetaan asiakkaat tietokannasta
                String sql = "SELECT * FROM asiakas WHERE postinro LIKE ? OR etunimi LIKE ? OR sukunimi LIKE ? OR lahiosoite LIKE ? OR email LIKE ? OR puhelinnro LIKE ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Asetetaan SQL-lauseelle arvot
                String hakusanaLike = "%" + hakusana + "%";
                stmt.setString(1, hakusanaLike);
                stmt.setString(2, hakusanaLike);
                stmt.setString(3, hakusanaLike);
                stmt.setString(4, hakusanaLike);
                stmt.setString(5, hakusanaLike);
                stmt.setString(6, hakusanaLike);

                // Suoritetaan SQL-lause ja käsitellään tulokset
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Asiakas asiakas = new Asiakas(
                            rs.getInt("asiakas_id"),
                            rs.getString("postinro"),
                            rs.getString("etunimi"),
                            rs.getString("sukunimi"),
                            rs.getString("lahiosoite"),
                            rs.getString("email"),
                            rs.getString("puhelinnro")
                    );
                    asiakkaat.add(asiakas);
                }

                // Suljetaan tietokantayhteys ja vapautetaan resurssit
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Asiakkaiden haku epäonnistui!");
            e.printStackTrace();
        }

        return asiakkaat;
    }

    public void lisaaAsiakas(Asiakas asiakas) throws SQLException {
        Connection yhteys = Tietokanta.getYhteys();
        PreparedStatement lisayslause = null;
        try {
            String sql = "INSERT INTO Asiakas (postinro, etunimi, sukunimi, lahiosoite, email, puhelinnro) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            lisayslause = yhteys.prepareStatement(sql);
            lisayslause.setString(1, asiakas.getPostinro());
            lisayslause.setString(2, asiakas.getEtunimi());
            lisayslause.setString(3, asiakas.getSukunimi());
            lisayslause.setString(4, asiakas.getLahiosoite());
            lisayslause.setString(5, asiakas.getEmail());
            lisayslause.setString(6, asiakas.getPuhelinnro());
            lisayslause.executeUpdate();
        } finally {
            Tietokanta.sulje(lisayslause, yhteys);
        }
    }
    public Asiakas haeAsiakas(int asiakasId) throws SQLException {
        Connection yhteys = null;
        PreparedStatement kysely = null;
        ResultSet tulokset = null;

        try {
            yhteys = Tietokanta.getYhteys();
            kysely = yhteys.prepareStatement("SELECT postinro, etunimi, sukunimi, lahiosoite, email, puhelinnro " +
                    "FROM Asiakas WHERE asiakas_id = ?");
            kysely.setInt(1, asiakasId);
            tulokset = kysely.executeQuery();

            if (tulokset.next()) {
                return new Asiakas(asiakasId, tulokset.getString("postinro"),
                        tulokset.getString("etunimi"), tulokset.getString("sukunimi"),
                        tulokset.getString("lahiosoite"), tulokset.getString("email"),
                        tulokset.getString("puhelinnro"));
            } else {
                return null;
            }
        } finally {
            Tietokanta.sulje(tulokset, kysely, yhteys);
        }
    }
    public void paivitaAsiakas(Asiakas asiakas) throws SQLException {
        Connection yhteys = Tietokanta.getYhteys();
        PreparedStatement paivityslause = null;
        try {
            String sql = "UPDATE Asiakas SET postinro = ?, etunimi = ?, sukunimi = ?, lahiosoite = ?, email = ?, puhelinnro = ? WHERE asiakas_id = ?";
            paivityslause = yhteys.prepareStatement(sql);
            paivityslause.setString(1, asiakas.getPostinro());
            paivityslause.setString(2, asiakas.getEtunimi());
            paivityslause.setString(3, asiakas.getSukunimi());
            paivityslause.setString(4, asiakas.getLahiosoite());
            paivityslause.setString(5, asiakas.getEmail());
            paivityslause.setString(6, asiakas.getPuhelinnro());
            paivityslause.setInt(7, asiakas.getAsiakasId());
            paivityslause.executeUpdate();
        } finally {
            Tietokanta.sulje(paivityslause, yhteys);
        }
    }

    public static void poistaAsiakas(int asiakasId) throws SQLException {
        Connection yhteys = Tietokanta.getYhteys();
        PreparedStatement poistolause = null;
        try {
            String sql = "DELETE FROM Asiakas WHERE asiakas_id = ?";
            poistolause = yhteys.prepareStatement(sql);
            poistolause.setInt(1, asiakasId);
            poistolause.executeUpdate();
        } finally {
            Tietokanta.sulje(poistolause, yhteys);
        }
    }

    public List<Asiakas> haeKaikkiAsiakkaat() {
        Connection yhteys = null;
        Statement kysely = null;
        ResultSet tulokset = null;
        List<Asiakas> asiakkaat = new ArrayList<>();

        try {
            yhteys = Tietokanta.getYhteys();
            kysely = yhteys.createStatement();
            tulokset = kysely.executeQuery("SELECT * FROM asiakas");

            while (tulokset.next()) {
                int id = tulokset.getInt("asiakas_id");
                String postinro = tulokset.getString("postinro");
                String etunimi = tulokset.getString("etunimi");
                String sukunimi = tulokset.getString("sukunimi");
                String lahiosoite = tulokset.getString("lahiosoite");
                String email = tulokset.getString("email");
                String puhelinnro = tulokset.getString("puhelinnro");

                Asiakas asiakas = new Asiakas(id, postinro, etunimi, sukunimi, lahiosoite, email, puhelinnro);
                asiakkaat.add(asiakas);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            Tietokanta.sulje(tulokset, kysely, yhteys);
        }

        return asiakkaat;
    }

}
