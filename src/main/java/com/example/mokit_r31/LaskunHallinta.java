package com.example.mokit_r31;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/** Laskua käsittelevä hallintaluokka.
 * Sisältää Lasku- ja LaskuController-luokkien tarvitsemia metodeja.
 * Kentät: laskun luomisen päivämäärä, laskun eräpäivä, varauksen kesto (vrk), laskun numero.
 * Metodeja: laske varauksen kesto, hae lasku tietokannasta, luo uusi lasku ja tallenna tietokantaan,
 * hae kaikki laskut tietokannasta, poista lasku tietokannasta, merkitse lasku maksetuksi tietokantaan,
 * merkitse lasku maksamattomaksi, tallenna laskusta PDF-tiedosto.
 */

public class LaskunHallinta {
    private int varauksenkesto;
    Tietokanta tietokanta;

    public LaskunHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;
    }

    public int laskePaivienMaara(int varausId) {
        varauksenkesto = 0;
        try {
            // Muodostetaan SQL-kysely päivien lukumäärän laskemiseksi
            Connection conn = Tietokanta.getYhteys();
            String sql = "SELECT DATEDIFF(varattu_loppupvm, varattu_alkupvm) AS paivat FROM varaus WHERE varaus_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, varausId);

            // Suoritetaan kysely ja haetaan päivien lukumäärä tuloksista
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                varauksenkesto = 1 + rs.getInt("paivat");
            }
        } catch (SQLException e) {
            // Tässä käsitellään poikkeus, jos kyselyn suoritus epäonnistuu
            throw new RuntimeException(e);
        }
        return varauksenkesto;
    }

    public Lasku haeLasku(int laskuId) throws SQLException {
        String sql = "SELECT * FROM lasku WHERE lasku_id = ?";
        try (Connection conn = Tietokanta.getYhteys();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, laskuId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int varausId = rs.getInt("varaus_id");
                    double summa = rs.getDouble("summa");
                    double alv = rs.getDouble("alv");
                    boolean maksettu = rs.getBoolean("maksettu");
                    return new Lasku(alv, summa, laskuId, varausId, maksettu);
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
        Connection yhteys = Tietokanta.getYhteys();

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
            yhteys = Tietokanta.getYhteys();
            String sql = "SELECT * FROM lasku";
            haku = yhteys.prepareStatement(sql);
            tulosjoukko = haku.executeQuery();
            while (tulosjoukko.next()) {
                Lasku lasku = new Lasku();
                lasku.setLaskuId(tulosjoukko.getInt("lasku_id"));
                lasku.setVarausId(tulosjoukko.getInt("varaus_id"));
                lasku.setSumma(tulosjoukko.getDouble("summa"));
                lasku.setAlv(tulosjoukko.getDouble("alv"));
                lasku.setMaksettu(tulosjoukko.getBoolean("maksettu"));
                laskut.add(lasku);
            }
        } finally {
            Tietokanta.sulje(tulosjoukko, haku, yhteys);
        }
        return laskut;
    }

    public void poistaLasku(Lasku lasku) throws SQLException {
        Connection yhteys = null;
        PreparedStatement stmt = null;

        try {
            // Avataan yhteys tietokantaan
            yhteys = Tietokanta.getYhteys();

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
                yhteys.close();}
        }
        }

    public void merkitseMaksetuksi(Lasku lasku) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Tietokanta.getYhteys();
            String sql = "UPDATE lasku SET maksettu = ? WHERE lasku_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, true);
            stmt.setInt(2, lasku.getLaskuId());
            stmt.executeUpdate();
            lasku.setMaksettu(true);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void merkitseEiMaksetuksi(Lasku lasku) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Tietokanta.getYhteys();
            String sql = "UPDATE lasku SET maksettu = ? WHERE lasku_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, false);
            stmt.setInt(2, lasku.getLaskuId());
            stmt.executeUpdate();
            lasku.setMaksettu(false);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void luoPDF(Lasku lasku) throws SQLException, FileNotFoundException, DocumentException {
        // Yhdistetään tietokantaan
        Connection conn;
        try {
            conn = Tietokanta.getYhteys();

            Lasku luoPDFLasku = haeLasku(lasku.getLaskuId());

// Laskun päiväys ja päiväyksen avulla generoitu PDF-tiedoston nimi

            LocalDateTime luomispvm = LocalDateTime.now();
            LocalDateTime erapaiva = luomispvm.plus(14, ChronoUnit.DAYS);

            DateTimeFormatter pvmformat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String luomisDate = luomispvm.format(pvmformat);
            String erapaivaDate = erapaiva.format(pvmformat);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter tiedostopvm = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
            String tiedostonimi = "laskuPDF_" + now.format(tiedostopvm) + ".pdf";
            lasku.setTiedostonimi(tiedostonimi);

// Luodaan PDF-lasku iText-kirjastolla
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(tiedostonimi));
            document.open();

            // Testataan haeLasku-metodin tulosten lisäämistä tiedostoon
            Paragraph otsikko = new Paragraph("LASKU");
            otsikko.setAlignment(Paragraph.ALIGN_CENTER);
            otsikko.setSpacingAfter(100);
            document.add(otsikko);

            Paragraph yritys = new Paragraph("""
                    Village Newbies OY
                    Mökkivuokraus
                    UEF TKT KUOPIO - Ohjelmistotuotanto - R31 harjoitustyö
                    """);
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

            document.close();

            // Tässä voisi avata PDF-tiedoston esikatselun. Metodi ei ole tässä ohjelmassa käytössä.
            // Kutsutaan ikkunaa, joka avaa luodun PDF-tiedoston esikatselun:
            //pdfPreview(tiedostonimi);

            System.out.println("Lasku tallennettu tiedostoon " + tiedostonimi);

// Suljetaan yhteys tietokantaan
            Tietokanta.sulje(conn);

        } catch (Exception e) {
            System.err.println("Virhe tallennettaessa laskua PDF-tiedostoksi: " + e.getMessage());
        }
    }

    // Metodi, joka avaa pdf-tiedoston esikatselun uudessa ikkunassa.
    // Tämä ei toimi, ilmeisesti siksi että JavaFX kirjaston versio on vanhentunut.
    /*
    public void pdfPreview(String pdfTiedostonNimi) {
        // Luodaan Stage-objekti, joka edustaa ikkunaa
        Stage stage = new Stage();

        // Luodaan WebView-objekti, johon ladataan pdf-tiedosto
        WebView webView = new WebView();

        // Jos ohjelma ei löydä tiedostoa, tähän voidaan lisätä:
        // String tiedostopolku = polku + pdfTiedostonNimi

        webView.getEngine().load(pdfTiedostonNimi);

        // Luodaan Scene-objekti, johon lisätään WebView
        Scene scene = new Scene(webView);
        stage.setTitle("PDF-tiedoston esikatselu");

        // Asetetaan ikkunan koko
        stage.setWidth(800);
        stage.setHeight(600);

        // Asetetaan Scene ikkunaan ja näytetään se käyttäjälle
        stage.setScene(scene);
        stage.show();
    } */
}