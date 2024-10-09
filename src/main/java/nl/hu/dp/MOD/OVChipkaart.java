package nl.hu.dp.MOD;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ov_chipkaart")
public class OVChipkaart {
    @Id
    @Column(name = "kaart_nummer")
    private int kaart_nummer;
    @Column(name = "geldig_tot")
    private java.sql.Date geldig_tot;
    @Column(name = "klasse")
    private int klasse;
    @Column(name = "saldo")
    private double saldo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reiziger_id", nullable = false)
    private Reiziger reiziger;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ov_chipkaart_product", joinColumns = @JoinColumn(name = "kaart_nummer"), inverseJoinColumns = @JoinColumn(name = "product_nummer"))
    private List<Product> producten = new ArrayList<>();

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

    public List<Product> getProducten() {
        return producten;
    }
    @Override
    public String toString() {
        return "OVChipkaart {#" + kaart_nummer + " geldig tot: " + geldig_tot + ", klasse: " + klasse + ", saldo: €" + saldo + "}";
    }
}
