package com.example.mokit_r31;

/** Laskua käsittelevä pääluokka.
 * Tietokantaan tallennetut tiedot: lasku id, varaus id, alv, summa.
 * Tiedostonimi generoituu, kun laskusta luodaan PDF-tiedosto.
 * Maksettu on boolean-arvo, jonka käyttäjä voi päivittä käyttöliittymässä olevilla checkboxeilla.
 * Tätä luokkaa käsittelee LaskunHallinta.
 */

public class Lasku {

    private double alv;
    private double summa;
    private int laskuId;
    private int varausId;
    private String tiedostonimi;
    private boolean maksettu;

    public Lasku(double alv, double summa, int laskuId, int varausId, boolean maksettu) {
        this.alv = alv;
        this.summa = summa;
        this.laskuId = laskuId;
        this.varausId = varausId;
        this.maksettu = maksettu;
    }

    public Lasku(){}

    public boolean isMaksettu() {
        return maksettu;}

    public void setMaksettu(boolean maksettu) {
        this.maksettu = maksettu;}

    public String getTiedostonimi() {
        return tiedostonimi;}
    public void setTiedostonimi(String tiedostonimi) {
        this.tiedostonimi = tiedostonimi;}

    public int getVarausId() {
        return varausId;}
    public void setVarausId(int varausId) {
        this.varausId = varausId;}
    public double getAlv() {
        return alv;}
    public void setAlv(double alv) {
        this.alv = alv;}
    public double getSumma() {
        return summa;}
    public void setSumma(double summa) {
        this.summa = summa;}
    public int getLaskuId() {
        return laskuId;}
    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;}

    @Override
    public String toString() {
        boolean onkoMaksettu = this.isMaksettu();
        return "Lasku ID: " + laskuId + ", varaus ID:" + varausId + "\n" +
                "Alv: " + alv + "\n" +
                "Veroton hinta: " + summa + "\n" +
                "Laskun summa: " + (summa+alv) + "\n" +
                "Lasku maksettu: " + (onkoMaksettu ? "kyllä" : "ei");
    }
}