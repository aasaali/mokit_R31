package com.example.mokit_r31;


public class Palvelu {
    //Testi
    private int Id;
    private int alueId;
    private String nimi;
    private int tyyppi;
    private String kuvaus;
    private double hinta;
    private double alv;

    public Palvelu(int alueId, String nimi, int tyyppi, String kuvaus, double hinta, double alv) {

        this.alueId = alueId;
        this.nimi = nimi;
        this.tyyppi = tyyppi;
        this.kuvaus = kuvaus;
        this.hinta = hinta;
        this.alv = alv;
    }

    //aatu lisännyt parametrittoman konstruktorin:

    public Palvelu(){

    }
    public Palvelu(int id, int alueId, String nimi, int tyyppi, String kuvaus, double hinta, double alv) {
        this.Id = id;
        this.alueId = alueId;
        this.nimi = nimi;
        this.tyyppi = tyyppi;
        this.kuvaus = kuvaus;
        this.hinta = hinta;
        this.alv = alv;
    }

    public int getId() {
        return Id;
    }
    public int getAlueId() {
        return alueId;
    }

    public String getNimi() {
        return nimi;
    }

    public int getTyyppi() {
        return tyyppi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public double getHinta() {
        return hinta;
    }

    public double getAlv() {
        return alv;
    }


    public void setAlueId(int alueId) { this.alueId = alueId; }

    public void setId(int Id) { this.Id = Id; }

    public int setAlueId() {
        return alueId;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setTyyppi(int tyyppi) {
        this.tyyppi = tyyppi;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public void setHinta(double hinta) {
        this.hinta = hinta;
    }

    public void setAlv(double alv) {
        this.alv = alv;
    }

    @Override
    public String toString() {
        return "Palvelu{" + getId() +
                ", alueId=" + alueId +
                ", nimi='" + nimi + '\'' +
                ", tyyppi=" + tyyppi +
                ", kuvaus='" + kuvaus + '\'' +
                ", hinta=" + hinta +
                ", alv=" + alv +
                '}';
    }
}
