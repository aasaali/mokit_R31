package com.example.mokit_r31;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/*
public class LaskunHallinta {

    private LocalDateTime luomispvm = LocalDateTime.now();
    private int laskunnumero;
    private LocalDateTime erapaiva = luomispvm.plus(14, ChronoUnit.DAYS);

    Tietokanta tietokanta = new Tietokanta();
    public LaskunHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;
    }

    public void luoLasku(Mokki mokki, Palvelu palvelu) {
        Lasku uusiLasku = new Lasku();
        uusiLasku.laskeSummaJaAlv(mokki.getHinta(), palvelu.getHinta(), palvelu.getAlv());
        // Tallenna uusi lasku tietokantaan tai tee mitä tarvitset
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

    public void luoLaskuPdf(String tiedostonimi) {
        // Luodaan uusi PDF-dokumentti
        Document document = new Document(PageSize.A4);

        // Sen hetkinen päivämäärä?
        luomispvm = LocalDateTime.now();

        //erapaiva = luomispvm + 14
        //https://www.javatpoint.com/java-date-add-days

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

} */