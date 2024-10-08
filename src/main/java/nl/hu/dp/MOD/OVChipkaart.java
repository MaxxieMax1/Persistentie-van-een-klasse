package nl.hu.dp.MOD;

import java.sql.Date;

public class OVChipkaart {
    private int kaart_nummer;
    private java.sql.Date geldig_tot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;

    public OVChipkaart() {

    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public void setKaart_nummer(int kaart_nummer) {
        this.kaart_nummer = kaart_nummer;
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(Date geldig_tot) {
        this.geldig_tot = geldig_tot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }
    @Override
    public String toString() {
        return "OVChipkaart {#" + kaart_nummer + " geldig tot: " + geldig_tot + ", klasse: " + klasse + ", saldo: €" + saldo + "}";
    }
}
