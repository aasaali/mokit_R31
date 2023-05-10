package com.example.mokit_r31;

public class Posti {

    String postinro; //char(5) PK
    String toimipaikka; // varchar(45)

    public Posti(String postinro, String toimipaikka){
        this.postinro = postinro;
        this.toimipaikka=toimipaikka;
    }
    public Posti(){}
    public Posti(String postinro){ this.postinro=postinro;}

    public String getPostinro() {
        return postinro;
    }

    public void setPostinro(String postinro) {
        this.postinro = postinro;
    }

    public String getToimipaikka() {
        return toimipaikka;
    }

    public void setToimipaikka(String toimipaikka) {
        this.toimipaikka = toimipaikka;
    }

    @Override
    public String toString() {
        return
                "Postinumero: " + postinro +
                ", toimipaikka: " + toimipaikka;
    }
}
