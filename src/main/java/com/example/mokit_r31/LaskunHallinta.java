package com.example.mokit_r31;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
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

public class LaskunHallinta {

    private LocalDateTime luomispvm;
    private int laskunnumero;
    private LocalDateTime erapaiva;

    Tietokanta tietokanta = new Tietokanta();

    public LaskunHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;
    }
    public void luoPDF(int varausId) {



    }
    public void luoLasku(int varausId) throws SQLException, FileNotFoundException, DocumentException {
        // Yhdistetään tietokantaan
        Connection conn = null;
        try {
            try {
                conn = tietokanta.getYhteys();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Suoritetaan SQL-kysely
            String sql = "SELECT varaus.varaus_id, asiakas.etunimi, asiakas.sukunimi, mokki.mokkinimi, palvelu.nimi, palvelu.hinta " +
                    "FROM varaus " +
                    "JOIN asiakas ON varaus.asiakas_id = asiakas.asiakas_id " +
                    "JOIN mokki ON varaus.mokki_mokki_id = mokki.mokki_id " +
                    "JOIN varauksen_palvelut ON varaus.varaus_id = varauksen_palvelut.varaus_id " +
                    "JOIN palvelu ON varauksen_palvelut.palvelu_id = palvelu.palvelu_id " +
                    "WHERE varaus.varaus_id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, varausId);
            ResultSet rs = stmt.executeQuery();

// Luodaan PDF-lasku iText-kirjastolla
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("lasku.pdf"));
            document.open();

// Lisätään laskun tiedot
            document.add(new Paragraph("Lasku"));
            document.add(new Paragraph("Varausnumero: " + varausId));
            document.add(new Paragraph("Asiakas: " + rs.getString("etunimi") + " " + rs.getString("sukunimi")));
            document.add(new Paragraph("Mökki: " + rs.getString("mokkinimi")));

// Lisätään palvelut
            double summa = 0;
            while (rs.next()) {
                document.add(new Paragraph("Palvelu: " + rs.getString("nimi") + " - hinta: " + rs.getDouble("hinta")));
                summa += rs.getDouble("hinta");
            }
            document.add(new Paragraph("Summa: " + summa));

// Tallennetaan lasku tietokantaan
            String insertSql = "INSERT INTO lasku (varaus_id, summa, alv) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, varausId);
            insertStmt.setDouble(2, summa);
            insertStmt.setDouble(3, summa * 0.24); // Oletetaan, että alv on 24%
            insertStmt.executeUpdate();

// Suljetaan yhteys tietokantaan
            conn.close();

// Suljetaan

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
/*
    public void luoLasku(Mokki mokki, Palvelu palvelu) {
        Lasku uusiLasku = new Lasku();
        uusiLasku.setVarausId(laskutaVarausLw.getSelectionModel().getSelectedItem());
        uusiLasku.laskeSummaJaAlv(mokki.getHinta(), palvelu.getHinta(), palvelu.getAlv());
        // Tallenna uusi lasku tietokantaan tai tee mitä tarvitset
    }*/

    public void lisaaLasku(Lasku lasku) throws SQLException {
        Connection yhteys = tietokanta.getYhteys();
        PreparedStatement lisayslause = null;
        try {
            // Tallenna laskun tiedot tietokantaan
            String sql = "INSERT INTO lasku (lasku_id, varaus_id, summa, alv) VALUES (?, ?, ?, ?)";
            lisayslause = yhteys.prepareStatement(sql);
            lisayslause.setInt(1, lasku.getLaskuId());
            lisayslause.setInt(2, lasku.getVarausId());
            lisayslause.setDouble(3, lasku.getSumma());
            lisayslause.setDouble(4, lasku.getAlv());
            lisayslause.executeUpdate();
        } finally {
            Tietokanta.sulje(lisayslause, yhteys);
        }
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

    public VarausAsiakas haeVarausAsiakas(int varausId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        VarausAsiakas varausAsiakas = null;

        try {
            conn = Tietokanta.getYhteys();
            stmt = conn.prepareStatement("SELECT varaus.varaus_id, asiakas.etunimi, asiakas.sukunimi, asiakas.osoite, asiakas.postinro " +
                    "FROM varaus " +
                    "JOIN asiakas ON varaus.asiakas_id = asiakas.asiakas_id " +
                    "WHERE varaus.varaus_id = ?");
            stmt.setInt(1, varausId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Varaus varaus = new Varaus();
                varaus.setVarausId(rs.getInt("varaus_id"));
                varaus.setAsiakasId(rs.getInt("asiakas_id"));

                Asiakas asiakas = new Asiakas();
                asiakas.setEtunimi(rs.getString("etunimi"));
                asiakas.setSukunimi(rs.getString("sukunimi"));
                asiakas.setLahiosoite(rs.getString("osoite"));
                asiakas.setPostinro(rs.getString("postinumero"));

                varausAsiakas = new VarausAsiakas(varaus, asiakas);
            }

        } finally {
            Tietokanta.sulje(rs, stmt, conn);
        }

        return varausAsiakas;
    }

/*
    public void luoLaskuPdf(Lasku lasku) {
        // Luodaan uusi PDF-dokumentti
        Document document = new Document(PageSize.A4);

        luomispvm = LocalDateTime.now();
        erapaiva = luomispvm.plus(14, ChronoUnit.DAYS);
        String tiedostonimi;
        LocalDateTime now = LocalDateTime.now();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        tiedostonimi = "laskuPDF_" + formatter.format(now) + ".pdf";

        try {
            // Alustetaan PDF-tiedoston tallennus
            PdfWriter.getInstance(document, new FileOutputStream(tiedostonimi));

            // Avataan dokumentti kirjoitusta varten
            document.open();

            // Luodaan otsikko ja muotoillaan sitä
            Paragraph otsikko = new Paragraph("LASKU");
            otsikko.setAlignment(Paragraph.ALIGN_CENTER);
            otsikko.setSpacingAfter(20);
            document.add(otsikko);

            // Lisätään asiakkaan tiedot laskuun
            Paragraph asiakkaanTiedot = new Paragraph("Asiakkaan tiedot:\n" + nimi + "\n" + osoite + "\n" + puhelinnumero);
            asiakkaanTiedot.setSpacingAfter(20);
            document.add(asiakkaanTiedot);

            // Lisätään varauksen tiedot laskuun
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Paragraph varauksenTiedot = new Paragraph("Varauksen tiedot:\n" + "Aloituspvm: " + dateFormat.format(aloituspvm) + "\n" +
                    "Lopetuspvm: " + dateFormat.format(lopetuspvm) + "\n" + "Kohde: " + kohde + "\n" +
                    "Lisäpalvelut: " + lisapalvelut + "\n" + "Hintaerittely: " + hinta);
            varauksenTiedot.setSpacingAfter(20);
            document.add(varauksenTiedot);

            // Lisätään laskutus tiedot laskuun
            Paragraph laskutusTiedot = new Paragraph("Laskutus:\n" + "Summa: " + summa + "\n" + "ALV: " + alv + "\n" +
                    "Laskun luomisen pvm: " + dateFormat.format(luomispvm) + "\n" + "Laskun numero: " + laskunnumero + "\n" +
                    "Eräpäivä: " + dateFormat.format(erapaiva));
            laskutusTiedot.setSpacingAfter(20);
            document.add(laskutusTiedot);

            // Suljetaan dokumentti
            document.close();

// Lisää tähän kutsu ilmoitus-ikkunalle
            System.out.println("Lasku tallennettu tiedostoon " + tiedostonimi);
        } catch (Exception e) {
            System.err.println("Virhe tallennettaessa laskua PDF-tiedostoksi: " + e.getMessage());
        }
    }
    */
}