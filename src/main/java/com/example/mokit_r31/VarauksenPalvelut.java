package com.example.mokit_r31;

public class VarauksenPalvelut {
    private int varausId;
    private int palveluId;
    private int lkm;

    // Tyhjä konstruktori
    public VarauksenPalvelut() {
    }

    // Konstruktori kaikilla parametreilla
    public VarauksenPalvelut(int varausId, int palveluId, int lkm) {
        this.varausId = varausId;
        this.palveluId = palveluId;
        this.lkm = lkm;
    }


    // Getterit ja setterit
    public int getVarausId() {
        return varausId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public int getPalveluId() {
        return palveluId;
    }

    public void setPalveluId(int palveluId) {
        this.palveluId = palveluId;
    }

    public int getLkm() {
        return lkm;
    }

    public void setLkm(int lkm) {
        this.lkm = lkm;
    }

    @Override
    public String toString() {
        return "VarauksenPalvelut{" +
                "varausId=" + varausId +
                ", palveluId=" + palveluId +
                ", lkm=" + lkm +
                '}';
    }
}