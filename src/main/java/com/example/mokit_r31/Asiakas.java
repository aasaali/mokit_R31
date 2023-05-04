package com.example.mokit_r31;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Asiakkaita hallinnoiva luokka
 * kentät: asiakasId, postinro, etunimi, sukunimi, lähiosoite, email, puhelinnumero
 * metodit:
 */
public class Asiakas {

    private int asiakasId; private String postinro; private String etunimi; private String sukunimi;
    private String lahiosoite; private String email; private String puhelinnro;

    public Asiakas(int asiakasId, String postinro, String etunimi, String sukunimi, String lahiosoite,
                   String email, String puhelinnro) {
        this.asiakasId = asiakasId; this.postinro = postinro; this.etunimi = etunimi;
        this.sukunimi = sukunimi; this.lahiosoite = lahiosoite; this.email = email; this.puhelinnro = puhelinnro;
    }
    public Asiakas(String postinro, String etunimi, String sukunimi, String lahiosoite,
                   String email, String puhelinnro) {
        this.postinro = postinro; this.etunimi = etunimi;
        this.sukunimi = sukunimi; this.lahiosoite = lahiosoite; this.email = email; this.puhelinnro = puhelinnro;
    }
    public Asiakas(){
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public String getPostinro() {
        return postinro;
    }

    public void setPostinro(String postinro) { this.postinro = postinro; }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getLahiosoite() {
        return lahiosoite;
    }

    public void setLahiosoite(String lahiosoite) {
        this.lahiosoite = lahiosoite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPuhelinnro() {
        return puhelinnro;
    }

    public void setPuhelinnro(String puhelinnro) {
        this.puhelinnro = puhelinnro;
    }

    @Override
    public String toString() {
        return "Asiakkaan tiedot: " +
                "asiakasId=" + asiakasId +
                ", postinro='" + postinro + '\'' +
                ", etunimi='" + etunimi + '\'' +
                ", sukunimi='" + sukunimi + '\'' +
                ", lahiosoite='" + lahiosoite + '\'' +
                ", email='" + email + '\'' +
                ", puhelinnro='" + puhelinnro + '\'';
    }

    public static List<Asiakas> haeKaikki() throws SQLException {
        Connection yhteys = null;
        PreparedStatement kysely = null;
        ResultSet tulokset = null;

        try {
            yhteys = Tietokanta.getYhteys();
            kysely = yhteys.prepareStatement(
        "SELECT asiakas_id, postinro, etunimi, sukunimi, lahiosoite, email, puhelinnro FROM Asiakas");
            tulokset = kysely.executeQuery();

            List<Asiakas> asiakkaat = new ArrayList<>();

            while (tulokset.next()) {
                asiakkaat.add(new Asiakas(tulokset.getInt("asiakas_id"),
                        tulokset.getString("postinro"), tulokset.getString("etunimi"),
                        tulokset.getString("sukunimi"), tulokset.getString("lahiosoite"),
                        tulokset.getString("email"), tulokset.getString("puhelinnro")));
            }

            return asiakkaat;
        } finally {
            Tietokanta.sulje(tulokset, kysely, yhteys);
        }
    }

    public void lisaa() throws SQLException {
        Connection yhteys = null;
        PreparedStatement kysely = null;
        ResultSet avain = null;

        try {
            yhteys = Tietokanta.getYhteys();
            kysely = yhteys.prepareStatement(
                    "INSERT INTO Asiakas (postinro, etunimi, sukunimi, lahiosoite, email, puhelinnro) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            kysely.setString(1, postinro);
            kysely.setString(2, etunimi);
            kysely.setString(3, sukunimi);
            kysely.setString(4, lahiosoite);
            kysely.setString(5, email);
            kysely.setString(6, puhelinnro);

            int rivit = kysely.executeUpdate();

            if (rivit == 0) {
                throw new SQLException("Asiakkaan lisääminen tietokantaan epäonnistui.");
            }

            avain = kysely.getGeneratedKeys();
            if (avain.next()) {
                asiakasId = avain.getInt(1);
            } else {
                throw new SQLException("Asiakkaan lisääminen tietokantaan epäonnistui, asiakas_id:tä ei saatu.");
            }
        } finally {
            Tietokanta.sulje(avain, kysely, yhteys);
        }
    }

    public void paivita() throws SQLException {
        Connection yhteys = null;
        PreparedStatement kysely = null;

        try {
            yhteys = Tietokanta.getYhteys();
            //kysely = yhteys.prepareStatement("UPDATE Asiakas SET postinro = ?, etunimi = ?, sukunimi = ?, lahiosoite = ?, email = ?, puhelinnro = ? " +
              //              "kysely.setString(1, postinro);
            kysely.setString(2, etunimi);
            kysely.setString(3, sukunimi);
            kysely.setString(4, lahiosoite);
            kysely.setString(5, email);
            kysely.setString(6, puhelinnro);
            kysely.setInt(7, asiakasId);

            int rivit = kysely.executeUpdate();

            if (rivit == 0) {
                throw new SQLException("Asiakkaan päivittäminen tietokantaan epäonnistui.");
            }
        } finally {
            Tietokanta.sulje(kysely, yhteys);
        }
    }


}

