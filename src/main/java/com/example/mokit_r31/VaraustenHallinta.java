package com.example.mokit_r31;

import java.sql.*;

public class VaraustenHallinta {
    public static void main(String[] args) {

        // Määritä yhteysparametrit
        String url = "jdbc:mysql://localhost:3306/vn";
        String username = "root";
        String password = "R31_mokki";

        // Määritä tietojen lisättävä arvo
        String nimi = "Uusi alue";

        // Yritä yhdistää tietokantaan
        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            // Luo SQL-lauseke
            String sql = "INSERT INTO alue (nimi) VALUES (?)";

            // Luo valmisteltu lauseke
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Aseta arvo parametriin
            stmt.setString(1, nimi);

            // Suorita lauseke
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Uusi alue lisätty tauluun alue!");
            }

        } catch (SQLException ex) {
            System.out.println("Tapahtui virhe tietokantaan yhdistettäessä tai tietoja lisättäessä: " + ex.getMessage());
        }
    }
}