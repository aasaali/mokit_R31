package com.example.mokit_r31;
import java.sql.*;
public class MokkienHallinta {

    //testaan taas, toimiiko?? :)
        private Connection conn;

        public MokkiHallinta() {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:mokkihallinta.db");

                // Luo taulut, jos niitä ei ole jo olemassa ->>on olemassa
                //Statement stmt = conn.createStatement();
                //stmt.execute("CREATE TABLE IF NOT EXISTS alueet (id INTEGER PRIMARY KEY, nimi TEXT)");
                //stmt.execute("CREATE TABLE IF NOT EXISTS mokit (id INTEGER PRIMARY KEY, alue_id INTEGER, postinumero TEXT, nimi TEXT, katuosoite TEXT, hinta REAL, kuvaus TEXT, varustelu TEXT, FOREIGN KEY (alue_id) REFERENCES alueet(id))");

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        // Metodi alueen lisäämiseksi
        public void lisaaAlue(String nimi) {
            try {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO alueet (nimi) VALUES (?)");
                stmt.setString(1, nimi);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        // Metodi alueen hakemiseksi tietokannasta
        public void haeAlue(String nimi) {
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM alueet WHERE nimi = ?");
                stmt.setString(1, nimi);
                ResultSet rs = stmt.executeQuery();

                // Käy läpi kaikki haetut rivit
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String alueenNimi = rs.getString("nimi");
                    System.out.println("Alueen id: " + id + ", nimi: " + alueenNimi);
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        // Metodi alueen tietojen muuttamiseksi
        public void muutaAlueenNimi(int id, String nimi) {
            try {
                PreparedStatement stmt = conn.prepareStatement("UPDATE alueet SET nimi = ? WHERE id = ?");
                stmt.setString(1, nimi);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        // Metodi alueen poistamiseen tietokannasta


        // Metodi mökin lisäämiseksi
        public void lisaaMokki(int alueId, String postinumero, String nimi, String katuosoite, double hinta, String kuvaus, String varustelu) {
            try {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO mokit (alue_id, postinumero, nimi, katuosoite, hinta, kuvaus, varustelu) VALUES (?, ?, ?, ?, ?, ?, ?)");
                stmt.setInt(1, alueId);
                stmt.setString(2, postinumero);
                stmt.setString(3, nimi);
                stmt.setString(4, katuosoite);
                stmt.setDouble(5, hinta);
                stmt.setDouble(6, kuvaus);
                stmt.setDouble(7, varustelu);
            }
        }

        // Metodi mökin hakemiseksi

        // Metodi mökin tietojen muuttamiseen

        // Metodi mökin tietojen poistamiseen