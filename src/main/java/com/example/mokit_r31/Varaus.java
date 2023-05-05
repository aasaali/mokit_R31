package com.example.mokit_r31;

import java.time.LocalDateTime;

/**
 * Olioluokka varauksille, joilla kenttinä tietokannan tiedot
 * VarausId, asiakasId, mokkiId, varauspvm, vahvistuspvm, alkupvm ja loppipvm.
 */
public class Varaus {
    private int varausId;
    private int asiakasId;
    private int mokkiId;

    private LocalDateTime vahvistusPvm;
    private LocalDateTime varattuAlkupvm;
    private LocalDateTime varattuLoppupvm;

    //Alustaja
    public Varaus(int asiakasId, int mokkiId, LocalDateTime varattuAlkupvm, LocalDateTime varattuLoppupvm) {
        this.asiakasId = asiakasId;
        this.mokkiId = mokkiId;
        this.vahvistusPvm = LocalDateTime.now();
        this.varattuAlkupvm = varattuAlkupvm;
        this.varattuLoppupvm = varattuLoppupvm;
    }
    // Parametriton alustaja
    public Varaus(){}
    public int getVarausId() {
        return varausId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public int getMokkiId() {
        return mokkiId;
    }

    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }


    public LocalDateTime getVahvistusPvm() {
        return vahvistusPvm;
    }

    public void setVahvistusPvm(LocalDateTime vahvistusPvm) {
        this.vahvistusPvm = vahvistusPvm;
    }

    public LocalDateTime getVarattuAlkupvm() {
        return varattuAlkupvm;
    }

    public void setVarattuAlkupvm(LocalDateTime varattuAlkupvm) {
        this.varattuAlkupvm = varattuAlkupvm;
    }

    public LocalDateTime getVarattuLoppupvm() {
        return varattuLoppupvm;
    }

    public void setVarattuLoppupvm(LocalDateTime varattuLoppupvm) {
        this.varattuLoppupvm = varattuLoppupvm;
    }

    @Override
    public String toString() {
        return "Varaus{" +
                "varausId=" + varausId +
                ", asiakasId=" + asiakasId +
                ", mokkiId=" + mokkiId +
                ", vahvistusPvm=" + vahvistusPvm +
                ", varattuAlkupvm=" + varattuAlkupvm +
                ", varattuLoppupvm=" + varattuLoppupvm +
                '}';
    }
}

