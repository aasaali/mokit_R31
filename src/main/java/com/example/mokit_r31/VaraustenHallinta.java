package com.example.mokit_r31;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VaraustenHallinta {

    private Tietokanta tietokanta;


    // Metodi, joka lukee varaukset tietokannasta ja luo niistä listan.
    public List<Varaus> getVaraukset() throws SQLException {
        List<Varaus> varaukset = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement("SELECT * FROM varaus");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Varaus varaus = new Varaus();
                varaus.setVarausId(rs.getInt("varaus_id"));
                varaus.setAsiakasId(rs.getInt("asiakas_id"));
                varaus.setMokkiId(rs.getInt("mokki_mokki_id"));

                Timestamp vahvistusPvmTimestamp = rs.getTimestamp("vahvistus_pvm");
                if (vahvistusPvmTimestamp != null) {
                    varaus.setVahvistusPvm(vahvistusPvmTimestamp.toLocalDateTime());
                }

                Timestamp varattuAlkupvmTimestamp = rs.getTimestamp("varattu_alkupvm");
                if (varattuAlkupvmTimestamp != null) {
                    varaus.setVarattuAlkupvm(varattuAlkupvmTimestamp.toLocalDateTime());
                }

                Timestamp varattuLoppupvmTimestamp = rs.getTimestamp("varattu_loppupvm");
                if (varattuLoppupvmTimestamp != null) {
                    varaus.setVarattuLoppupvm(varattuLoppupvmTimestamp.toLocalDateTime());
                }

                varaukset.add(varaus);
            }
        } finally {
            Tietokanta.sulje(rs, stmt, conn);
        }

        return varaukset;
    }

    // Metodi, joka lisää uuden varauksen tietokantaan
    public void lisaaVaraus(Varaus varaus) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Tietokanta.getYhteys();
            String sql = "INSERT INTO varaus(asiakas_id, mokki_mokki_id, varattu_pvm, vahvistus_pvm, varattu_alkupvm, varattu_loppupvm) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, varaus.getAsiakasId());
            stmt.setInt(2, varaus.getMokkiId());
            stmt.setTimestamp(3, Timestamp.valueOf(varaus.getVahvistusPvm()));
            stmt.setTimestamp(4, Timestamp.valueOf(varaus.getVahvistusPvm()));
            stmt.setTimestamp(5, Timestamp.valueOf(varaus.getVarattuAlkupvm()));
            stmt.setTimestamp(6, Timestamp.valueOf(varaus.getVarattuLoppupvm()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Tietokanta.sulje(stmt, conn);
        }
    }

// Metodi, joka poistaa varauksen tietokannasta

    public void poistaVaraus(int varausId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement("DELETE FROM varaus WHERE varaus_id = ?");
            stmt.setInt(1, varausId);
            stmt.executeUpdate();
        } finally {
            Tietokanta.sulje(stmt, conn);
        }
    }

    // Metodi, joka muokkaa varauksen tietokannassa
    public void muokkaaVarausta(Varaus varaus) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement("UPDATE varaus SET asiakas_id = ?, mokki_mokki_id = ?, varattu_alkupvm = ?, varattu_loppupvm = ? WHERE varaus_id = ?");
            stmt.setInt(1, varaus.getAsiakasId());
            stmt.setInt(2, varaus.getMokkiId());
            stmt.setTimestamp(3, Timestamp.valueOf(varaus.getVarattuAlkupvm()));
            stmt.setTimestamp(4, Timestamp.valueOf(varaus.getVarattuLoppupvm()));
            stmt.setInt(5, varaus.getVarausId());
            stmt.executeUpdate();
        } finally {
            Tietokanta.sulje(stmt, conn);
        }
    }


    public int getMokinAlueId(int mokkiId) throws SQLException {
        int alueId = -1;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = tietokanta.getYhteys();
            stmt = conn.prepareStatement("SELECT alue_id FROM mokki WHERE mokki_id = ?");
            stmt.setInt(1, mokkiId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                alueId = rs.getInt("alue_id");

            }
        } finally {
            tietokanta.sulje(rs, stmt, conn);
        }

        return alueId;
    }

    // Metodi, joka hakee tietokannasta kyseisen mökin alueen palvelut
    public List<Palvelu> getMokinPalvelut(int mokkiId) throws SQLException {
        List<Palvelu> palvelut = new ArrayList<>();

        int alueId = getMokinAlueId(mokkiId);
        if (alueId == -1) {
            return palvelut;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = tietokanta.getYhteys();
            stmt = conn.prepareStatement("SELECT * FROM palvelu WHERE alue_id = ?");
            stmt.setInt(1, alueId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Palvelu palvelu = new Palvelu();
                palvelu.setId(rs.getInt("palvelu_id"));
                palvelu.setAlueId(rs.getInt("alue_id"));
                palvelu.setNimi(rs.getString("nimi"));
                palvelu.setTyyppi(rs.getInt("tyyppi"));
                palvelu.setKuvaus(rs.getString("kuvaus"));
                palvelu.setHinta(rs.getDouble("hinta"));
                palvelu.setAlv(rs.getDouble("alv"));
                palvelut.add(palvelu);
            }
        } finally {
            tietokanta.sulje(rs, stmt, conn);
        }

        return palvelut;
    }

    public static void lisaaPalveluVaraukselle(int varausId, int palveluId, int lkm) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Tietokanta.getYhteys();
            String sql = "SELECT lkm FROM varauksen_palvelut WHERE varaus_id = ? AND palvelu_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, varausId);
            stmt.setInt(2, palveluId);
            rs = stmt.executeQuery();

            if (rs.next()) { // Palvelu on jo varauksessa, päivitetään lkm
                int nykyinenLkm = rs.getInt("lkm");
                sql = "UPDATE varauksen_palvelut SET lkm = ? WHERE varaus_id = ? AND palvelu_id = ?";
                stmt.close();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, nykyinenLkm + lkm);
                stmt.setInt(2, varausId);
                stmt.setInt(3, palveluId);
                stmt.executeUpdate();
            } else { // Palvelua ei ole vielä varauksessa, lisätään uusi rivi
                sql = "INSERT INTO varauksen_palvelut(varaus_id, palvelu_id, lkm) VALUES (?, ?, ?)";
                stmt.close();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, varausId);
                stmt.setInt(2, palveluId);
                stmt.setInt(3, lkm);
                stmt.executeUpdate();
            }
        } finally {
            Tietokanta.sulje(rs, stmt, conn);
        }
    }

    public static List<Palvelu> getVarauksenPalvelut(int varausId) throws SQLException {
        List<Palvelu> palvelut = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement("SELECT palvelu.*, varauksen_palvelut.lkm " +
                    "FROM palvelu " +
                    "JOIN varauksen_palvelut ON palvelu.palvelu_id = varauksen_palvelut.palvelu_id " +
                    "WHERE varauksen_palvelut.varaus_id = ?");
            stmt.setInt(1, varausId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Palvelu palvelu = new Palvelu();
                palvelu.setId(rs.getInt("palvelu_id"));
                palvelu.setNimi(rs.getString("nimi"));
                palvelu.setKuvaus(rs.getString("kuvaus"));
                palvelu.setHinta(rs.getDouble("hinta"));
                palvelu.setAlueId(rs.getInt("alue_id"));

                int lkm = rs.getInt("lkm");
                for (int i = 0; i < lkm; i++) {
                    palvelut.add(palvelu);
                }
            }
        } finally {
            Tietokanta.sulje(rs, stmt, conn);
        }
        return palvelut;
    }

    public static void poistaPalveluVaraukselta(int varausId, int palveluId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = Tietokanta.getYhteys();

            // Tarkista, kuinka monta samanlaista palvelua on varauksella
            stmt = conn.prepareStatement("SELECT lkm FROM varauksen_palvelut WHERE varaus_id = ? AND palvelu_id = ?");
            stmt.setInt(1, varausId);
            stmt.setInt(2, palveluId);
            rs = stmt.executeQuery();

            if(rs.next()) {
                int lkm = rs.getInt("lkm");
                if(lkm > 1) {
                    // Jos palveluiden lukumäärä on suurempi kuin 1, vähennä lukumäärää yhdellä
                    stmt = conn.prepareStatement("UPDATE varauksen_palvelut SET lkm = lkm - 1 WHERE varaus_id = ? AND palvelu_id = ?");
                    stmt.setInt(1, varausId);
                    stmt.setInt(2, palveluId);
                    stmt.executeUpdate();
                } else {
                    // Jos palveluiden lukumäärä on 1, poista rivi taulusta
                    stmt = conn.prepareStatement("DELETE FROM varauksen_palvelut WHERE varaus_id = ? AND palvelu_id = ?");
                    stmt.setInt(1, varausId);
                    stmt.setInt(2, palveluId);
                    stmt.executeUpdate();
                }
            }
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignored */ }
            }
            Tietokanta.sulje(stmt, conn);
        }
    }


}