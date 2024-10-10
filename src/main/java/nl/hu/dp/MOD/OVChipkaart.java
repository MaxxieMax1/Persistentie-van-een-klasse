package nl.hu.dp.MOD;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    private int kaart_nummer;
    private java.sql.Date geldig_tot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;
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
    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    public boolean addProduct(Product product) {
        if (!producten.contains(product)) {
            producten.add(product);
            product.addOVChipkaart(this.kaart_nummer);
            return true;
        }
        return false;
    }

    public boolean removeProduct(Product product) {
        if (producten.contains(product)) {
            producten.remove(product);
            product.removeOVChipkaart(this.kaart_nummer);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder productNamen = new StringBuilder();
        if (producten.isEmpty()) {
            productNamen.append("Geen producten");
        } else {
            for (Product product : producten) {
                productNamen.append(product.toString()).append(", ");
            }
            productNamen.setLength(productNamen.length() - 2);
        }


        return "OVChipkaart{" +
                "kaart_nummer=" + kaart_nummer +
                ", geldig_tot=" + geldig_tot +
                ", klasse=" + klasse +
                ", saldo=" + saldo +
                ", producten=" + productNamen+
                ", reiziger_id=" + (reiziger != null ? reiziger.getId() : "Geen reiziger") +
                '}';
    }



}
