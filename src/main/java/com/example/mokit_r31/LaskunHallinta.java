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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private int varauksenkesto;

    Tietokanta tietokanta = new Tietokanta();

    public LaskunHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;
    }

    public int laskePaivienMaara(int varausId) {
        varauksenkesto = 0;
        try {
            // Muodostetaan SQL-kysely päivien lukumäärän laskemiseksi
            Connection conn = tietokanta.getYhteys();
            String sql = "SELECT DATEDIFF(varattu_loppupvm, varattu_alkupvm) AS paivat FROM varaus WHERE varaus_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, varausId);

            // Suoritetaan kysely ja haetaan päivien lukumäärä tuloksista
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                varauksenkesto = rs.getInt("paivat");
            }
        } catch (SQLException e) {
            // Tässä käsitellään poikkeus, jos kyselyn suoritus epäonnistuu
            throw new RuntimeException(e);
        }
        return varauksenkesto;
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
// Lasketaan vuokrauksen kesto päivinä
        varauksenkesto = laskePaivienMaara(varaus.getVarausId());

// Lasketaan yhteishinta ja alv
        double yhteishinta = (mokinHinta * varauksenkesto) + palveluidenSumma;
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

    public void luoPDF(Lasku lasku) throws SQLException, FileNotFoundException, DocumentException {
        // Yhdistetään tietokantaan
        Connection conn = null;
        PreparedStatement haku = null;
        try {
            conn = tietokanta.getYhteys();

            // TÄMÄ SKRIPTI EI OLE OIKEIN. Rs jää tyhjäksi.
            /*
            // Suoritetaan SQL-kysely
            String sql = "SELECT varaus.varaus_id, asiakas.etunimi, asiakas.sukunimi, mokki.mokkinimi, palvelu.nimi, " +
                    "palvelu.hinta, palvelu.alv, lasku.lasku_id " +
                    "FROM varaus " +
                    "JOIN asiakas ON varaus.asiakas_id = asiakas.asiakas_id " +
                    "JOIN mokki ON varaus.mokki_mokki_id = mokki.mokki_id " +
                    "LEFT JOIN varauksen_palvelut ON varaus.varaus_id = varauksen_palvelut.varaus_id " +
                    "LEFT JOIN palvelu ON varauksen_palvelut.palvelu_id = palvelu.palvelu_id " +
                    "LEFT JOIN lasku ON varaus.varaus_id = lasku.varaus_id " +
                    "WHERE lasku.lasku_id = ?";

            haku = conn.prepareStatement(sql);
            haku.setInt(1, lasku.getLaskuId());
            ResultSet rs = haku.executeQuery();
            */

            // Kokeillaan käyttäen haeLasku-metodia
            LaskunHallinta laskunHallinta = new LaskunHallinta(tietokanta);
            Lasku luoPDFLasku = haeLasku(lasku.getLaskuId());
            System.out.println("Tarkistus1");
            System.out.println(luoPDFLasku);
            //System.out.println(rs.getString("lasku_id"));

// Laskun päiväys ja päiväyksen avulla generoitu PDF-tiedoston nimi

            LocalDateTime luomispvm = LocalDateTime.now();

            LocalDateTime erapaiva = luomispvm.plus(14, ChronoUnit.DAYS);


            DateTimeFormatter pvmformat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            String luomisDate = luomispvm.format(pvmformat);
            String erapaivaDate = erapaiva.format(pvmformat);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter tiedostopvm = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
            String tiedostonimi = "laskuPDF_" + now.format(tiedostopvm) + ".pdf";

// Luodaan PDF-lasku iText-kirjastolla
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(tiedostonimi));
            document.open();

            // Testataan haeLasku-metodin tulosten lisäämistä tiedostoon
            Paragraph otsikko = new Paragraph("LASKU");
            otsikko.setAlignment(Paragraph.ALIGN_CENTER);
            otsikko.setSpacingAfter(100);
            document.add(otsikko);

            Paragraph yritys = new Paragraph("Village Newbies OY\n" + "Mökkivuokraus, Kuopio\n" + "UEF OT R31");
            otsikko.setAlignment(Paragraph.ALIGN_CENTER);
            yritys.setSpacingAfter(100);
            document.add(yritys);

            String laskuntiedot = luoPDFLasku.toString();
            Paragraph laskulause = new Paragraph(laskuntiedot);
            laskulause.setSpacingAfter(100);
            document.add(laskulause);

            Paragraph laskutus = new Paragraph("Maksutiedot: \n" +
                    "Tilinumero: FI11 1111 2222 3333 44\n" +
                    "BIC: VNOYHH\n" +
                    "Laskun päiväys: " + luomisDate + ". Eräpäivä: " + erapaivaDate );
            document.add(laskutus);

/* rs on tyhjä, joten laskun tietoja ei saada generoitua
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
            System.out.println("Tarkistus");
            document.add(new Paragraph("Varausnumero: " + lasku.getVarausId()));
            document.add(new Paragraph("Asiakas: " + rs.getString("etunimi") + " " +
                    rs.getString("sukunimi")));
            document.add(new Paragraph("Mökki: " + rs.getString("mokkinimi")));

// Lisätään palvelut
            double summa = 0;
            double palvelutAlv = 0;
            if (rs.next()) {
                do {
                    document.add(new Paragraph("Palvelu: " + rs.getString("nimi") + " - hinta: " +
                            rs.getDouble("hinta")));
                    summa += rs.getDouble("hinta");
                    palvelutAlv += rs.getDouble("alv");
                } while (rs.next());
            } else {
                System.out.println("ResultSet on tyhjä");
            }

            double kokonaishinta = summa + palvelutAlv;
            document.add(new Paragraph("Veroton hinta: " + summa));
            document.add(new Paragraph("Hinta yhteensä: " + kokonaishinta));

            Paragraph laskutus = new Paragraph("Maksutiedot: \n" + "Tilinumero: FI11 1111 2222 3333 44\n" +
                    "BIC: VNOYHH\n" + "Laskun päiväys:" + luomisDate +
                    "Eräpäivä: " + erapaivaDate );
            otsikko.setAlignment(Paragraph.ALIGN_CENTER);
            otsikko.setSpacingAfter(20);
            document.add(laskutus);
*/
            document.close();

            System.out.println("Lasku tallennettu tiedostoon " + tiedostonimi);

// Suljetaan yhteys tietokantaan
            tietokanta.sulje(conn);

// Suljetaan

        } catch (Exception e) {
            System.err.println("Virhe tallennettaessa laskua PDF-tiedostoksi: " + e.getMessage());
        }
    }
}