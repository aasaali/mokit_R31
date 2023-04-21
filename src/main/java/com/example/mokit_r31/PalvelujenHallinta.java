package com.example.mokit_r31;
import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;



public class PalvelujenHallinta extends Application  {



    private static final String DB_URL = "jdbc:mysql://localhost:3306/vn";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "x";

    private static Connection conn;

    public static void main(String[] args) {

        try {
            // Yhdistä tietokantaan
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Luo tietokantaan tarvittavat taulut
            createTables();

            // Luo valikko
            Scanner scanner = new Scanner(System.in);
            int valinta;
            do {
                System.out.println("1. Lisää palvelu");
                System.out.println("2. Näytä palvelut");
                System.out.println("3. Poista palvelu");
                System.out.println("4. Muokkaa palvelun tietoja");
                System.out.println("0. Lopeta");
                System.out.print("Valitse toiminto: ");
                valinta = scanner.nextInt();
                scanner.nextLine(); // Tyhjennä rivinvaihtomerkki

                switch (valinta) {
                    case 1:
                        lisaaPalvelu(scanner);
                        break;
                    case 2:
                         naytaPalvelut();
                        break;
                    case 3:
                       poistaPalvelu(scanner);
                        break;
                    case 4:
                        muokkaaPalvelunTietoja(scanner);
                        break;
                    case 0:
                        System.out.println("Kiitos ohjelman käytöstä!");
                        break;
                    default:
                        System.out.println("Virheellinen valinta.");
                }
                System.out.println();
            } while (valinta != 0);

            // Sulje yhteys tietokantaan
            conn.close();

        } catch (SQLException e) {
            System.out.println("Tietokantavirhe: " + e.getMessage());
        }
    }

    private static void muokkaaPalvelunTietoja(Scanner scanner) {

            System.out.println("Anna muokattavan palvelun ID:");
            int id = scanner.nextInt();
            scanner.nextLine(); // Tyhjennetään rivinvaihto

         ArrayList<Palvelu> palvelut = new ArrayList<>();

            // Etsitään palvelu ID:n perusteella
            Palvelu muokattavaPalvelu = null;
            for (Palvelu palvelu : palvelut) {
                if (palvelu.getId() == id) {
                    muokattavaPalvelu = palvelu;
                    break;
                }
            }

            if (muokattavaPalvelu == null) {
                System.out.println("Palvelua ei löydy ID:n perusteella.");
                return;
            }

            System.out.println("Anna uusi nimi (max. 40 merkkiä):");
            String uusiNimi = scanner.nextLine();
            if (uusiNimi.length() > 40) {
                System.out.println("Nimi on liian pitkä.");
                return;
            }

            System.out.println("Anna uusi tyyppi (1-10):");
            int uusiTyyppi = scanner.nextInt();
            scanner.nextLine(); // Tyhjennetään rivinvaihto
            if (uusiTyyppi < 1 || uusiTyyppi > 10) {
                System.out.println("Tyyppi on virheellinen.");
                return;
            }

            System.out.println("Anna uusi kuvaus (max. 225 merkkiä):");
            String uusiKuvaus = scanner.nextLine();
            if (uusiKuvaus.length() > 225) {
                System.out.println("Kuvaus on liian pitkä.");
                return;
            }

            System.out.println("Anna uusi hinta (max. 8 numeron mittainen luku kahden desimaalin tarkkuudella):");
            double uusiHinta = scanner.nextDouble();
            if (uusiHinta <= 0 || uusiHinta > 99999999.99) {
                System.out.println("Hinta on virheellinen.");
                return;
            }

            // Lasketaan uusi arvonlisävero
            final double ALV_PROSENTTI = 24.0;
            double uusiALV = uusiHinta * ALV_PROSENTTI / 100.0;

            // Päivitetään palvelun tiedot
            muokattavaPalvelu.setNimi(uusiNimi);
            muokattavaPalvelu.setTyyppi(uusiTyyppi);
            muokattavaPalvelu.setKuvaus(uusiKuvaus);
            muokattavaPalvelu.setHinta(uusiHinta);
            muokattavaPalvelu.setAlv(uusiALV);

            System.out.println("Palvelun tiedot päivitetty.");
        }



    private static void poistaPalvelu(Scanner scanner) {{
            try {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM palvelut");

                System.out.println("Valitse poistettava palvelu (syötä id):");
                while (resultSet.next()) {
                    System.out.println(resultSet.getInt("id") + ": " + resultSet.getString("nimi"));
                }

                int id = scanner.nextInt();
                statement.executeUpdate("DELETE FROM palvelut WHERE id=" + id);

                System.out.println("Palvelu poistettu onnistuneesti.");

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void naytaPalvelut() {

        try {
            // Avaa tietokantayhteys
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Hae kaikki palvelut tietokannasta
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM palvelut");

            // Tulosta palvelut
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Nimi: " + rs.getString("nimi") + " | Tyyppi: " + rs.getInt("tyyppi") + " | Kuvaus: " + rs.getString("kuvaus") + " | Hinta: " + rs.getDouble("hinta") + " | ALV: " + rs.getDouble("alv"));
            }

            // Sulje tietokantayhteys
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Virhe: " + e.getMessage());
        }
    }


    private static void createTables() throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS alueet ("
                + "id INT(11) NOT NULL AUTO_INCREMENT,"
                + "nimi VARCHAR(40) NOT NULL,"
                + "PRIMARY KEY (id)"
                + ")";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS palvelut ("
                + "id INT(11) NOT NULL AUTO_INCREMENT,"
                + "alue_id INT(11) NOT NULL,"
                + "nimi VARCHAR(40) NOT NULL,"
                + "tyyppi INT(11) NOT NULL,"
                + "kuvaus VARCHAR(225) NOT NULL,"
                + "hinta DECIMAL(10,2) NOT NULL,"
                + "alv DECIMAL(10,2) NOT NULL,"
                + "PRIMARY KEY (id),"
                + "FOREIGN KEY (alue_id) REFERENCES alueet(id) ON DELETE CASCADE"
                + ")";
        stmt.executeUpdate(sql);

        stmt.close();
    }

        private static void lisaaPalvelu(Scanner scanner) throws SQLException {
            System.out.println("\nLisää uusi palvelu");
            System.out.print("Palvelun nimi (max 40 merkkiä): ");
            String nimi = scanner.nextLine();
            while (nimi.length() > 40) {
                System.out.print("Liian pitkä nimi. Anna uusi nimi (max 40 merkkiä): ");
                nimi = scanner.nextLine();
            }
            System.out.print("Palvelun tyyppi: ");
            int tyyppi = Integer.parseInt(scanner.nextLine());
            System.out.print("Palvelun kuvaus (max 225 merkkiä): ");
            String kuvaus = scanner.nextLine();
            while (kuvaus.length() > 225) {
                System.out.print("Liian pitkä kuvaus. Anna uusi kuvaus (max 225 merkkiä): ");
                kuvaus = scanner.nextLine();
            }
            System.out.print("Palvelun hinta (max 8 numeron mittainen luku kahden desimaalin tarkkuudella): ");
            double hinta = Double.parseDouble(scanner.nextLine());
            System.out.print("Palvelun alv-prosentti: ");
            double alvProsentti = Double.parseDouble(scanner.nextLine());

            String sql = "INSERT INTO palvelut (nimi, tyyppi, kuvaus, hinta, alv_prosentti) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nimi);





    }}

    @Override
    public void start(Stage stage) throws Exception {

    }
}

