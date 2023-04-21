package com.example.mokit_r31;

import java.sql.*;

public class VaraustenHallinta {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Muuta tiedot vastaamaan omaa tietokantayhteyttäsi
            String url = "jdbc:mysql://localhost:3306/vn";
            String user = "root";
            String pass = "R31_mokki";

            // Luodaan yhteys tietokantaan
            conn = DriverManager.getConnection(url, user, pass);

            // Luodaan SQL-kysely
            String sql = "INSERT INTO alue (nimi) VALUES (?)";
            stmt = conn.prepareStatement(sql);

            // Asetetaan parametri kyselylle
            stmt.setString(1, "Uusi alue");

            // Suoritetaan kysely
            int rivit = stmt.executeUpdate();

            // Tulostetaan ilmoitus onnistuneesta lisäyksestä
            System.out.println(rivit + " riviä lisätty.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Suljetaan tietokantayhteys ja resurssit
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
