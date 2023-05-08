package com.example.mokit_r31;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Laskun tietoja:
asiakkaan tiedot: nimi, osoite, puhelinnumero
Varauksen tiedot: pvm-pvm, kohde, palvelut, hintaerittely
laskutus: summa, pvm, laskun numero, eräpäivä pvm + 14 vrk
 */

public class Lasku {

    private double alv;
    private double summa;
    private int laskuId;
    private int varausId;

    private Asiakas asiakas;

    public Lasku(double alv, double summa, int laskuId, int varausId) {
        this.alv = alv;
        this.summa = summa;
        this.laskuId = laskuId;
        this.varausId = varausId;
    }

    public Lasku(int varausId) {
        this.varausId=varausId;}

    public Lasku(){}

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

    public void laskeSummaJaAlv(double mokkiHinta, double palveluHinta, double palveluAlv) {
        double summa = mokkiHinta + palveluHinta;
        double alv = palveluAlv;
        this.summa = summa;
        this.alv = alv;
    }
}