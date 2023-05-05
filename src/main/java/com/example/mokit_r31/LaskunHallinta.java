package com.example.mokit_r31;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LaskunHallinta {

    Tietokanta tietokanta = new Tietokanta();
    public LaskunHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;
    }

    public void laskeJaTallennaLasku(int laskuId, int varausId, double alv) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mokkivuokraus", "kayttajatunnus", "salasana");
            conn.setAutoCommit(false);

            // Laske laskun summa varauksen perusteella
            double summa = laskeLaskunSumma(varausId, alv);

            // Tallenna laskun tiedot tietokantaan
            String sql = "INSERT INTO lasku (lasku_id, varaus_id, summa, alv) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, laskuId);
            stmt.setInt(2, varausId);
            stmt.setDouble(3, summa);
            stmt.setDouble(4, alv);
            stmt.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            // Käsittele virhe
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Sulje yhteydet
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public double laskeLaskunSumma(int varausId, double alv) {
        double summa = 0;

        // Toteuta laskennan logiikka, esimerkiksi hakemalla varauksen tiedot tietokannasta
        // ja laskemalla hinnat yhteen, lisäämällä ALV jne.

        return summa;
    }

}
