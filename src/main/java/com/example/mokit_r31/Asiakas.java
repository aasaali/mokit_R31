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
    Tietokanta tietokanta = new Tietokanta();

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

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
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
/*
    public Asiakas haeVarausAsiakas(int varausId, int asiakasId) throws SQLException {
        Connection conn = tietokanta.getYhteys();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Asiakas asiakas = null;

        try {
            stmt = conn.prepareStatement("SELECT asiakas_id, postinro, etunimi, sukunimi, lahiosoite, email, puhelinnro " +
                    "FROM asiakas JOIN varaus ON asiakas.asiakas_id = varaus.asiakas_id " +
                    "WHERE varaus.varaus_id = ? AND asiakas.asiakas_id = ?");
            stmt.setInt(1, varausId);
            stmt.setInt(2, asiakasId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                asiakas = new Asiakas();
                asiakas.setAsiakasId(rs.getInt("asiakas_id"));
                asiakas.setPostinro(rs.getString("postinro"));
                asiakas.setEtunimi(rs.getString("etunimi"));
                asiakas.setSukunimi(rs.getString("sukunimi"));
                asiakas.setLahiosoite(rs.getString("lahiosoite"));
                asiakas.setEmail(rs.getString("email"));
                asiakas.setPuhelinnro(rs.getString("puhelinnro"));
            }
        } finally {
            tietokanta.sulje(rs, stmt, conn);
        }

        return asiakas;
    }

*/
}

