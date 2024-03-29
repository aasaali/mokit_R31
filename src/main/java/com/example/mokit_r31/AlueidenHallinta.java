package com.example.mokit_r31;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Alue-luokkaa hallinnoiva luokka.
 *
 */
public class AlueidenHallinta {
    private Tietokanta tietokanta;
    public AlueidenHallinta(Tietokanta tietokanta) {
        this.tietokanta = tietokanta;}

// Alueen tietojen lisääminen tietokantaan SQL INSERT, jos aluetta ei ole lisätty samoilla tiedoilla
public void lisaaAlueenTiedot(Alue newAlue) {
    try {
        Connection conn = tietokanta.getYhteys();
        System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

        // Tarkistetaan, onko alue jo olemassa
        String checkSql = "SELECT COUNT(*) AS count FROM alue WHERE nimi = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setString(1, newAlue.getNimi());
        ResultSet checkResult = checkStmt.executeQuery();
        checkResult.next();
        int count = checkResult.getInt("count");

        if (count > 0) {
            // Alue on jo olemassa, näytetään virheilmoitus
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Alue " + newAlue.getNimi() + " on jo olemassa");
            alert.showAndWait();
        } else {
            // Luodaan SQL-lause, jolla alueen tiedot lisätään tietokantaan
            String sql = "INSERT INTO alue (nimi) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Asetetaan SQL-lauseelle arvot
            stmt.setString(1, newAlue.getNimi());

            // Suoritetaan SQL-lause
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int alueId = generatedKeys.getInt(1);
                newAlue.setAlueId(alueId);
                System.out.println("Uusi alue tallennettu tietokantaan, id = " + alueId);
            } else {
                System.out.println("Uuden alueen tallennus tietokantaan epäonnistui!");
            }

            // Suljetaan tietokantayhteys ja vapautetaan resurssit
            stmt.close();
        }
        conn.close();
    } catch (SQLException e) {
        System.out.println("Uuden alueen tallennus tietokantaan epäonnistui!");
        e.printStackTrace();
    }
}

// Alueen tietojen hakeminen tietokannasta SQL SELECT>
    public List<Alue> haeAlueenTiedot(String hakuNimi){
        // Tietokannan yhteysosoite

        List<Alue> alueet = new ArrayList<>();

        try {
            Connection conn = tietokanta.getYhteys();
            {
                System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

                // Luodaan SQL-lause, jolla alueen tiedot lisätään tietokantaan
                String sql = "SELECT alue_id, nimi FROM alue WHERE nimi LIKE ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Asetetaan SQL-lauseelle arvot
                stmt.setString(1, "%" + hakuNimi + "%");
                ResultSet resultSet = stmt.executeQuery();

                // Muodostetaan lista Alue-olioita hakutuloksista
                while (resultSet.next()) {
                    int alueId = resultSet.getInt("alue_id");
                    String nimi = resultSet.getString("nimi");
                    Alue alue = new Alue(alueId, nimi);
                    alueet.add(alue);
                }

                // Suljetaan tietokantayhteys ja vapautetaan resurssit
                resultSet.close();
                stmt.close();
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println("Yhteys tietokantaan epäonnistui!");
            e.printStackTrace();
        }
    return alueet;
    }

// Alueen tietojen muokkaaminen tietokannassa SQL UPDATE

    public void paivitaAlueenTiedot(Alue alue) {


        try {
            Connection conn = tietokanta.getYhteys();
            {
                System.out.println("Yhteys tietokantaan " + conn.getMetaData().getDatabaseProductName() + " onnistui!");

                // Luodaan SQL-lause, jolla alueen tiedot päivitetään tietokantaan
                String sql = "UPDATE alue SET nimi = ? WHERE alue_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Asetetaan SQL-lauseelle arvot
                stmt.setInt(2, alue.getAlueId());
                stmt.setString(1, alue.getNimi());

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
    Tietokanta tietokanta = new Tietokanta();

    try {
        Connection conn = tietokanta.getYhteys();
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

    public List<Alue> haeKaikkiAlueet() throws SQLException {
        List<Alue> alueet = new ArrayList<>();
        Connection yhteys = null;
        PreparedStatement haku = null;
        ResultSet tulosjoukko = null;
        try {
            yhteys = Tietokanta.getYhteys();
            String sql = "SELECT * FROM alue";
            haku = yhteys.prepareStatement(sql);
            tulosjoukko = haku.executeQuery();
            while (tulosjoukko.next()) {
                Alue alue = new Alue();
                alue.setAlueId(tulosjoukko.getInt("alue_id"));
                alue.setNimi(tulosjoukko.getString("nimi"));
                alueet.add(alue);
            }
        } finally {
            Tietokanta.sulje(tulosjoukko, haku, yhteys);
        }
        return alueet;
    }
}

