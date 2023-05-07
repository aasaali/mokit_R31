package com.example.mokit_r31;

public class Mokki {
    private int mokkiId;
    private int alueId;
    private String postiNro; //5
    private String nimi; //45
    private String osoite; //45
    private double hinta; //8,2
    private String kuvaus; //150
    private int hlo;
    private String varustelu; //100

public Mokki(int mokkiId,int alueId, String postiNro, String nimi, String osoite, double hinta, String kuvaus, int hlo, String varustelu){
    this.mokkiId = mokkiId;
    this.alueId = alueId;
    this.postiNro = postiNro;
    this.nimi = nimi;
    this.osoite = osoite;
    this.hinta = hinta;
    this.kuvaus = kuvaus;
    this.hlo = hlo;
    this.varustelu = varustelu;
}

    public int getMokkiId() {
        return mokkiId;
    }

    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }


    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public double getHinta() {
        return hinta;
    }

    public void setHinta(double hinta) {
        this.hinta = hinta;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getVarustelu() {
        return varustelu;
    }

    public void setVarustelu(String varustelu) {
        this.varustelu = varustelu;
    }

    public int getAlueId() {
        return alueId;
    }

    public void setAlueId(int alueId) {
        this.alueId = alueId;
    }

    @Override
    public String toString() {
        return "Mokki{" +
                "mokkiId=" + mokkiId +
                ", alueId=" + alueId +
                ", postiNro=" + postiNro +
                ", nimi='" + nimi + '\'' +
                ", osoite='" + osoite + '\'' +
                ", hinta=" + hinta +
                ", kuvaus='" + kuvaus + '\'' +
                ", hlo=" + hlo +
                ", varustelu='" + varustelu + '\'' +
                '}';
    }

    public void setPostiNro(String postiNro) {
        this.postiNro = postiNro;
    }

    public String getPostiNro() {
        return postiNro;
    }

    public int getHlo() {
        return hlo;
    }

    public void setHlo(int hlo) {
        this.hlo = hlo;
    }
}