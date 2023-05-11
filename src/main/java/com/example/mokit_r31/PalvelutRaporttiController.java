package com.example.mokit_r31;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.mokit_r31.PalveluController.tietokanta;


public class PalvelutRaporttiController {

    @FXML
    private ComboBox<String> aluecbox;

    @FXML
    private DatePicker alkupaivaDP;

    @FXML
    private DatePicker loppupaivaDP;

    @FXML
    private ListView<String> palvelutLw;

    @FXML
    private Button haeButton;

    @FXML
    private Button tallennaPDFButton;
    private void lataaAlueet() {
        AlueidenHallinta hallinta = new AlueidenHallinta(tietokanta);
        try {
            List<Alue> alueet = hallinta.haeKaikkiAlueet();
            ObservableList<String> alueidenNimet = FXCollections.observableArrayList();
            for (Alue alue : alueet) {
                alueidenNimet.add(alue.getNimi());
            }
            aluecbox.setItems(alueidenNimet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // Ladataan alueet comboboxiin
        aluecbox.setOnMouseClicked(event -> lataaAlueet());

        // Lisätään tapahtumankuuntelija "Hae" -napille
        haeButton.setOnAction(e -> {
            // Haetaan valitut arvot
            String alue = aluecbox.getValue();
            String alkupaiva = alkupaivaDP.getValue().toString();
            String loppupaiva = loppupaivaDP.getValue().toString();

            if (alue == null || alkupaiva == null || loppupaiva == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Täytä kaikki kentät ennen haun suorittamista.");
                alert.showAndWait();
                return;
            }

            // Haetaan raportti palveluista
            PalvelutRaportti.haePalveluRaportti(alue, alkupaiva, loppupaiva, palvelutLw);
        });

        tallennaPDFButton.setOnAction(e -> {
            String alue = aluecbox.getValue();
            String alkupaiva = alkupaivaDP.getValue().toString();
            String loppupaiva = loppupaivaDP.getValue().toString();

            // Tallennetaan raportti PDF-tiedostoksi
            tallennaPDF(alue, alkupaiva, loppupaiva, palvelutLw);
        });
    }


    public static void tallennaPDF(String alue, String alkupaiva, String loppupaiva, ListView<String> palvelutLw) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String raportinNimi = "Palveluraportti_" + alue + "_" + dateFormat.format(new Date()) + ".pdf";

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(raportinNimi));
            document.open();

            // Otsikko
            Paragraph otsikko = new Paragraph("Palveluraportti", new Font(Font.FontFamily.HELVETICA, 18));
            otsikko.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(otsikko);
            document.add(new Paragraph("\n"));

            // Alue
            Paragraph alueP = new Paragraph("Alue: " + alue);
            document.add(alueP);
            document.add(new Paragraph("\n"));

            // Ajanjakso
            Paragraph ajanjaksoP = new Paragraph("Ajanjakso: " + alkupaiva + " - " + loppupaiva);
            document.add(ajanjaksoP);
            document.add(new Paragraph("\n"));

            // Palvelut
            document.add(new Paragraph("Palvelut:"));
            document.add(new Paragraph("\n"));

            for (String palvelu : palvelutLw.getItems()) {
                document.add(new Paragraph(palvelu));
            }

            document.close();
            System.out.println("Raportti tallennettu tiedostoon: " + raportinNimi);
        } catch (DocumentException | IOException e) {
        }

    }
}
