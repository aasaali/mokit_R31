/*package com.example.mokit_r31;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/** Laskun tietoja:
asiakkaan tiedot: nimi, osoite, puhelinnumero
Varauksen tiedot: pvm-pvm, kohde, palvelut, hintaerittely
laskutus: summa, pvm, laskun numero, eräpäivä pvm + 14 vrk
 */

/*
public class Lasku {

    private String nimi;
    private String osoite;
    private String puhelinnumero;
    private Date aloituspvm;
    private Date lopetuspvm;
    private String kohde;
    private String lisapalvelut;
    private double hinta;
    private double alv;
    private double summa;
    private LocalDateTime luomispvm;
    private int laskunnumero;
    private Date erapaiva;

    public void luoLaskuPdf(String tiedostonimi) {
        // Luodaan uusi PDF-dokumentti
        Document document = new Document(PageSize.A4);

        // Sen hetkinen päivämäärä?
        // luomispvm = java.time.LocalDate.now();

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

}
*/