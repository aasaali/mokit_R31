package com.example.mokit_r31;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

// Tähän tai Lasku:un pitää vielä päivittää varauksen päivien laskenta.
// Nyt lasku menee yhden päivän hinnalla.

public class LaskunHallinta {

    private LocalDateTime luomispvm;
    private int laskunnumero;
    private LocalDateTime erapaiva;

    Tietokanta tietokanta = new Tietokanta();

    public LaskunHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;
    }

    public Lasku haeLasku(int laskuId) throws SQLException {
        String sql = "SELECT * FROM lasku WHERE lasku_id = ?";
        try (Connection conn = tietokanta.getYhteys();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, laskuId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int varausId = rs.getInt("varaus_id");
                    double summa = rs.getDouble("summa");
                    double alv = rs.getDouble("alv");
                    return new Lasku(alv, summa, laskuId, varausId);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Virhe haettaessa laskua tietokannasta: " + ex.getMessage());
            throw ex;
        }
        return null;
    }


    public Lasku luoLasku(Varaus varaus) throws SQLException {

        Lasku luodaanLasku = new Lasku();
        Connection yhteys = tietokanta.getYhteys();

        // Haetaan mokin hinta
        double mokinHinta = 0.0;
        String mokinHintaQuery = "SELECT hinta FROM mokki WHERE mokki_id = ?";
        try (PreparedStatement mokinHintaStmt = yhteys.prepareStatement(mokinHintaQuery)) {
            mokinHintaStmt.setInt(1, varaus.getMokkiId());
            ResultSet mokinHintaResult = mokinHintaStmt.executeQuery();
            if (mokinHintaResult.next()) {
                mokinHinta = mokinHintaResult.getDouble("hinta");
            }
        }

// Haetaan varauksen palveluiden hinnat ja alvit
        double palveluidenSumma = 0.0;
        double palveluidenAlvSumma = 0.0;
        String palveluidenQuery = "SELECT p.hinta, p.alv FROM palvelu p JOIN varauksen_palvelut vp ON " +
                "p.palvelu_id = vp.palvelu_id WHERE vp.varaus_id = ?";
        try (PreparedStatement palveluidenStmt = yhteys.prepareStatement(palveluidenQuery)) {
            palveluidenStmt.setInt(1, varaus.getVarausId());
            ResultSet palveluidenResult = palveluidenStmt.executeQuery();
            while (palveluidenResult.next()) {
                palveluidenSumma += palveluidenResult.getDouble("hinta");
                palveluidenAlvSumma += palveluidenResult.getDouble("alv") *
                        palveluidenResult.getDouble("hinta");
            }
        }

// Lasketaan yhteishinta ja alv
        double yhteishinta = mokinHinta + palveluidenSumma;
        double yhteisalv = palveluidenAlvSumma;

// Tallennetaan lasku tietokantaan
        String tallennaLaskuQuery = "INSERT INTO lasku (varaus_id, summa, alv) VALUES (?, ?, ?)";
        try (PreparedStatement tallennaLaskuStmt = yhteys.prepareStatement(tallennaLaskuQuery)) {
            tallennaLaskuStmt.setInt(1, varaus.getVarausId());
            tallennaLaskuStmt.setDouble(2, yhteishinta);
            tallennaLaskuStmt.setDouble(3, yhteisalv);
            tallennaLaskuStmt.executeUpdate();
        }

        return luodaanLasku;
    }

    public List<Lasku> haeKaikkiLaskut() throws SQLException {
        List<Lasku> laskut = new ArrayList<>();
        Connection yhteys = null;
        PreparedStatement haku = null;
        ResultSet tulosjoukko = null;
        try {
            yhteys = tietokanta.getYhteys();
            String sql = "SELECT * FROM lasku";
            haku = yhteys.prepareStatement(sql);
            tulosjoukko = haku.executeQuery();
            while (tulosjoukko.next()) {
                Lasku lasku = new Lasku();
                lasku.setLaskuId(tulosjoukko.getInt("lasku_id"));
                lasku.setVarausId(tulosjoukko.getInt("varaus_id"));
                lasku.setSumma(tulosjoukko.getDouble("summa"));
                lasku.setAlv(tulosjoukko.getDouble("alv"));
                laskut.add(lasku);
            }
        } finally {
            tietokanta.sulje(tulosjoukko, haku, yhteys);
        }
        return laskut;
    }

    public void poistaLasku(Lasku lasku) throws SQLException {
        Connection yhteys = null;
        PreparedStatement stmt = null;

        try {
            // Avataan yhteys tietokantaan
            yhteys = tietokanta.getYhteys();

            // Luodaan SQL-kysely laskun poistamiseksi
            String sql = "DELETE FROM lasku WHERE lasku_id = ?";
            stmt = yhteys.prepareStatement(sql);
            stmt.setInt(1, lasku.getLaskuId());

            // Suoritetaan SQL-kysely
            int tulos = stmt.executeUpdate();
            if (tulos == 0) {
                System.out.println("Laskun poistaminen epäonnistui.");
            } else {
                System.out.println("Lasku poistettiin onnistuneesti tietokannasta.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Suljetaan resurssit
            if (stmt != null) {
                stmt.close();
            }
            if (yhteys != null) {
                yhteys.close();
            }
        }
    }

    public void luoPDF(int varausId) throws SQLException, FileNotFoundException, DocumentException {
        // Yhdistetään tietokantaan
        Connection conn = null;
        try {
            try {
                conn = tietokanta.getYhteys();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Suoritetaan SQL-kysely
            String sql = "SELECT varaus.varaus_id, asiakas.etunimi, asiakas.sukunimi, mokki.mokkinimi, palvelu.nimi, " +
                    "palvelu.hinta, palvelu.alv, lasku.lasku_id " +
                    "FROM varaus " +
                    "JOIN asiakas ON varaus.asiakas_id = asiakas.asiakas_id " +
                    "JOIN mokki ON varaus.mokki_mokki_id = mokki.mokki_id " +
                    "JOIN varauksen_palvelut ON varaus.varaus_id = varauksen_palvelut.varaus_id " +
                    "JOIN palvelu ON varauksen_palvelut.palvelu_id = palvelu.palvelu_id " +
                    "JOIN lasku ON varaus.varaus_id = lasku.varaus_id " +
                    "WHERE varaus.varaus_id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, varausId);
            ResultSet rs = stmt.executeQuery();

// Laskun päiväys ja päiväyksen avulla generoitu PDF-tiedoston nimi

            luomispvm = LocalDateTime.now();
            erapaiva = luomispvm.plus(14, ChronoUnit.DAYS);

            String tiedostonimi;
            LocalDateTime now = LocalDateTime.now();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            tiedostonimi = "laskuPDF_" + formatter.format(now) + ".pdf";

// Luodaan PDF-lasku iText-kirjastolla
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(tiedostonimi));
            document.open();

// Lisätään laskun tiedot
            Paragraph otsikko = new Paragraph("LASKU");
            otsikko.setAlignment(Paragraph.ALIGN_CENTER);
            otsikko.setSpacingAfter(10);
            document.add(otsikko);

            Paragraph yritys = new Paragraph("Village Newbies OY");
            otsikko.setAlignment(Paragraph.ALIGN_CENTER);
            otsikko.setSpacingAfter(20);
            document.add(yritys);

            document.add(new Paragraph("Laskun ID: " + rs.getString("lasku_id")));
            document.add(new Paragraph("Varausnumero: " + varausId));
            document.add(new Paragraph("Asiakas: " + rs.getString("etunimi") + " " +
                    rs.getString("sukunimi")));
            document.add(new Paragraph("Mökki: " + rs.getString("mokkinimi")));

// Lisätään palvelut
            double summa = 0;
            double palvelutAlv = 0;
            while (rs.next()) {
                document.add(new Paragraph("Palvelu: " + rs.getString("nimi") + " - hinta: " +
                        rs.getDouble("hinta")));
                summa += rs.getDouble("hinta");
                palvelutAlv += rs.getDouble("alv");
            }
            double kokonaishinta = summa + palvelutAlv;
            document.add(new Paragraph("Veroton hinta: " + summa));
            document.add(new Paragraph("Hinta yhteensä: " + kokonaishinta));

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            Paragraph laskutus = new Paragraph("Maksutiedot: \n" + "Tilinumero: FI11 1111 2222 3333 44\n" +
                    "BIC: VNOYHH\n" + "Laskun päiväys:" + dateFormat.format(luomispvm) +
                    "Eräpäivä: " + dateFormat.format(erapaiva) );
            otsikko.setAlignment(Paragraph.ALIGN_CENTER);
            otsikko.setSpacingAfter(20);
            document.add(laskutus);

            document.close();

            System.out.println("Lasku tallennettu tiedostoon " + tiedostonimi);

// Suljetaan yhteys tietokantaan
            conn.close();

// Suljetaan

        } catch (Exception e) {
            System.err.println("Virhe tallennettaessa laskua PDF-tiedostoksi: " + e.getMessage());
        }
    }


}