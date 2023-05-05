package com.example.mokit_r31;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class Raportit extends Application {

    private Connection connection;

    public Raportit(Connection connection) {
        this.connection = connection;
    }




    public void naytaMajoittumisraportti(String x, String y) {

        Stage stage = new Stage();
        VBox root = new VBox();
        root.getChildren().addAll(new Label("Anna ajanjakso (pp.kk.vvvv - pp.kk.vvvv):"), new TextField(), new Label("Anna postinumero: "), new TextField());
        stage.setScene(new Scene(root));
        stage.show();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Anna ajanjakso (pp.kk.vvvv - pp.kk.vvvv): ");
        String syotettyAjanjakso = scanner.nextLine();

        String[] ajankohdat = syotettyAjanjakso.split("-");
        if (ajankohdat.length != 2) {
            System.out.println("Virheellinen ajanjakso!");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date ensimmainenPaiva;
        Date viimeinenPaiva;
        try {
            ensimmainenPaiva = sdf.parse(ajankohdat[0].trim());
            viimeinenPaiva = sdf.parse(ajankohdat[1].trim());
        } catch (Exception e) {
            System.out.println("Virheellinen päivämäärä!");
            return;
        }

        System.out.print("Anna postinumero: ");
        String postinumero = scanner.nextLine();

        // Tarkistetaan, että postinumero löytyy tietokannasta
        String query = "SELECT * FROM alueet WHERE postinumero = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, postinumero);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                System.out.println("Postinumeroa ei löytynyt tietokannasta!");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Virhe tietokantakyselyssä: " + e.getMessage());
            return;
        }

        // Haetaan majoitustiedot halutulta ajanjaksolta ja postinumerolta
        query = "SELECT mokki_id, varattu_alkupvm, varattu_loppupvm FROM varaukset WHERE varattu_alkupvm >= ? AND varattu_loppupvm <= ? AND mokki_id IN (SELECT mokki_id FROM mokit WHERE postinumero = ?)";
        List<String[]> majoitustiedot = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, new java.sql.Date(ensimmainenPaiva.getTime()));
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String[] tiedot = new String[3];
                tiedot[0] = result.getString("mokki_id");
                tiedot[1] = result.getDate("varattu_alkupvm").toString();
                tiedot[2] = result.getDate("varattu_loppupvm").toString();
                majoitustiedot.add(tiedot);
            }
        } catch (SQLException e) {
            System.out.println("Virhe tietokantakyselyssä: " + e.getMessage());
            return;
        }

        // Luodaan pdf-tiedosto majoittumisraporttia varten
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("majoittumisraportti.pdf"));

            document.open();
            document.add(new Paragraph("Majoittumisraportti"));
            document.add(new Paragraph("Ajanjakso: " + sdf.format(ensimmainenPaiva) + " - " + sdf.format(viimeinenPaiva)));
            document.add(new Paragraph("Postinumero: " + postinumero));
            document.add(new Paragraph("\n"));

            for (String[] tiedot : majoitustiedot) {
                document.add(new Paragraph("Mökki ID: " + tiedot[0]));
                document.add(new Paragraph("Varattu alkupäivämäärä: " + tiedot[1]));
                document.add(new Paragraph("Varattu loppupäivämäärä: " + tiedot[2]));
                document.add(new Paragraph("\n"));
            }

            document.close();
            System.out.println("Majoittumisraportti luotu!");
        } catch (DocumentException | FileNotFoundException e) {
            System.out.println("Virhe tiedoston luomisessa: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

            // Luodaan tekstikentät, joissa voidaan syöttää tiedot
            TextField ajanjaksoTextField = new TextField();
            TextField postinumeroTextField = new TextField();

            // Luodaan napit, joilla voidaan suorittaa raportin luonti ja tyhjentää kentät
            Button luoRaporttiButton = new Button("Luo raportti");
            Button tyhjennaButton = new Button("Tyhjennä kentät");

            // Luodaan tapahtumankuuntelijat nappeihin
            luoRaporttiButton.setOnAction(e -> {
                String syotettyAjanjakso = ajanjaksoTextField.getText();
                String postinumero = postinumeroTextField.getText();
                naytaMajoittumisraportti(syotettyAjanjakso, postinumero);
            });

            tyhjennaButton.setOnAction(e -> {
                ajanjaksoTextField.setText("");
                postinumeroTextField.setText("");
            });

            // Luodaan VBox, johon lisätään kaikki komponentit
            VBox vbox = new VBox(10);
            vbox.getChildren().addAll(new Label("Anna ajanjakso (pp.kk.vvvv - pp.kk.vvvv):"), ajanjaksoTextField, new Label("Anna postinumero:"), postinumeroTextField, luoRaporttiButton, tyhjennaButton);

            Scene scene = new Scene(vbox, 300, 200);

            // Näytetään käyttöliittymä
            stage.setTitle("Majoittumisraportti");
            stage.setScene(scene);
            stage.show();


            }}