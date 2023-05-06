package com.example.mokit_r31;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Laskun tietoja:
asiakkaan tiedot: nimi, osoite, puhelinnumero
Varauksen tiedot: pvm-pvm, kohde, palvelut, hintaerittely
laskutus: summa, pvm, laskun numero, eräpäivä pvm + 14 vrk
 */

public class Lasku {

    private double alv;
    private double summa;
    private int laskuId;
    private int varausId;

    private Asiakas asiakas;

    public Lasku(int varausId) {
        this.varausId=varausId;
    }


    public Lasku(){}

    public int getVarausId() {
        return varausId;}
    public void setVarausId(int varausId) {
        this.varausId = varausId;}
    public double getAlv() {
        return alv;}
    public void setAlv(double alv) {
        this.alv = alv;}
    public double getSumma() {
        return summa;}
    public void setSumma(double summa) {
        this.summa = summa;}
    public int getLaskuId() {
        return laskuId;}
    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;}

    public void laskeSummaJaAlv(double mokkiHinta, double palveluHinta, double palveluAlv) {
        double summa = mokkiHinta + palveluHinta;
        double alv = palveluAlv;
        this.summa = summa;
        this.alv = alv;
    }


/*
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

    // Metodit asettavat laskutuksen tiedot
    // ...
*/
}