package com.example.mokit_r31;

/** Alueiden pääluokka.
 *
 */
public class Alue {
    private int alueId;
    private String nimi; // 40

    public Alue(int alueId, String nimi){
        this.alueId = alueId;
        this.nimi = nimi;
    }
    public Alue(){}

    public int getAlueId() {
        return alueId;
    }

    public void setAlueId(int alueId) {
        this.alueId = alueId;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    @Override
    public String toString() {
        return "Alue - " +
                "alueId: " + alueId +
                ", nimi: " + nimi;
    }
}
