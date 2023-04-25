package com.example.mokit_r31;

import java.sql.*;

public class VaraustenHallinta {
    private Connection conn;

    public VaraustenHallinta(Connection conn) {
        this.conn = conn;
    }

    // Lisää uuden varauksen tietokantaan
    public void lisaaVaraus(Varaus varaus) throws SQLException {
        String sql = "INSERT INTO Varaus (asiakasId, mokkiId, varattuPvm, vahvistusPvm, varattuAlkupvm, varattuLoppupvm) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, varaus.getAsiakasId());
            stmt.setInt(2, varaus.getMokkiId());
            stmt.setTimestamp(3, Timestamp.valueOf(varaus.getVarattuPvm()));
            stmt.setTimestamp(4, varaus.getVahvistusPvm() != null ? Timestamp.valueOf(varaus.getVahvistusPvm()) : null);
            stmt.setTimestamp(5, Timestamp.valueOf(varaus.getVarattuAlkupvm()));
            stmt.setTimestamp(6, Timestamp.valueOf(varaus.getVarattuLoppupvm()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Varauksen lisääminen tietokantaan epäonnistui");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    varaus.setVarausId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Varauksen lisääminen tietokantaan epäonnistui, id:n haku epäonnistui");
                }
            }
        }
    }
}


