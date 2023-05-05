package com.example.mokit_r31;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
public class AlueidenHallinta {
    private Tietokanta tietokanta;
    public AlueidenHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;}
// Alueen tietojen lisääminen tietokantaan SQL INSERT, jos aluetta ei ole lisätty samoilla tiedoilla
    public void lisaaAlueenTiedot(Alue newAlue) {

        try {
            Connection conn = tietokanta.getYhteys();
            {
                System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

                // Luodaan SQL-lause, jolla alueen tiedot lisätään tietokantaan
                String sql = "INSERT INTO alue (alue_id, nimi) SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM alue WHERE nimi = ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Asetetaan SQL-lauseelle arvot
                stmt.setInt(1, newAlue.getAlueId());
                stmt.setString(2, newAlue.getNimi());
                stmt.setString(3, newAlue.getNimi());

                // Suoritetaan SQL-lause
                stmt.executeUpdate();
                // Suljetaan tietokantayhteys ja vapautetaan resurssit
                stmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Yhteys tietokantaan epäonnistui!");
            e.printStackTrace();
        }

    }


// Alueen tietojen hakeminen tietokannasta SQL SELECT>
    public static void haeAlueenTiedot(String hakuNimi) throws SQLException{
        // Tietokannan yhteysosoite
        String url = "jdbc:mysql://localhost:3306/vn";
        // Käyttäjän tunnus ja salasana
        String user = "root";
        String password = "Heleppohomma23?3";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            {
                System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

                // Luodaan SQL-lause, jolla alueen tiedot lisätään tietokantaan
                String sql = "SELECT * FROM alue WHERE nimi LIKE ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Asetetaan SQL-lauseelle arvot
                stmt.setString(1, "%" + hakuNimi + "%");
                ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    System.out.println(resultSet.getInt("alue_id") + "\t" + resultSet.getString("nimi"));
                }


                // Suljetaan tietokantayhteys ja vapautetaan resurssit
                stmt.close();
                conn.close();

            }
        } catch (SQLException e) {
            System.out.println("Yhteys tietokantaan epäonnistui!");
            e.printStackTrace();
        }

    }

// Alueen tietojen muokkaaminen tietokannassa SQL UPDATE

    public static void paivitaAlueenTiedot(int alueId, String uusiNimi) {
        // Tietokannan yhteysosoite
        String url = "jdbc:mysql://localhost:3306/vn";
        // Käyttäjän tunnus ja salasana
        String user = "root";
        String password = "Heleppohomma23?3";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            {
                System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

                // Luodaan SQL-lause, jolla alueen tiedot päivitetään tietokantaan
                String sql = "UPDATE alue SET nimi = ? WHERE alue_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Asetetaan SQL-lauseelle arvot
                stmt.setString(1, uusiNimi);
                stmt.setInt(2, alueId);

                // Suoritetaan SQL-lause
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Alueen tiedot päivitettiin onnistuneesti.");
                } else {
                    System.out.println("Alueen tietojen päivitys epäonnistui - aluetta ei löydetty.");
                }

                // Suljetaan tietokantayhteys ja vapautetaan resurssit
                stmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Yhteys tietokantaan epäonnistui!");
            e.printStackTrace();
        }
    }



// Alueen tietojen poistaminen tietokannasta SQL DELETE
public static void poistaAlueenTiedot(int alueId) {
    // Tietokannan yhteysosoite
    String url = "jdbc:mysql://localhost:3306/vn";
    // Käyttäjän tunnus ja salasana
    String user = "root";
    String password = "Heleppohomma23?3";

    try {
        Connection conn = DriverManager.getConnection(url, user, password);
        {
            System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

            // Luodaan SQL-lause, jolla alueen tiedot poistetaan tietokannasta
            String sql = "DELETE FROM alue WHERE alue_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Asetetaan SQL-lauseelle arvot
            stmt.setInt(1, alueId);

            // Suoritetaan SQL-lause
            stmt.executeUpdate();

            // Suljetaan tietokantayhteys ja vapautetaan resurssit
            stmt.close();
            conn.close();
            System.out.println("Alueen " + alueId + " tiedot on poistettu");
        }
    } catch (SQLException e) {
        System.out.println("Yhteys tietokantaan epäonnistui!");
        e.printStackTrace();
    }
}

    public static void main(String[] args) throws SQLException {
    Alue alue1 = new Alue("Kauppi");
    haeAlueenTiedot("Kuopio");
    paivitaAlueenTiedot(1, "Petonen");
    haeAlueenTiedot("Petonen");
    poistaAlueenTiedot(1);
}
}

