package nl.hu.dp.MOD;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int product_nummer;
    private String naam;
    private String Beschrijving;
    private double prijs;
    private List<Integer> ovChipkaartNummers = new ArrayList<>();

    public Product() {
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return Beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        Beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<Integer> getOvchipkaarten() {
        return ovChipkaartNummers;
    }

    public void setOvChipkaartNummers(List<Integer> ovChipkaartNummers) {
        this.ovChipkaartNummers = ovChipkaartNummers;
    }

    public boolean addOVChipkaart(int kaartNummer) {
        if (!ovChipkaartNummers.contains(kaartNummer)) {
            ovChipkaartNummers.add(kaartNummer);
            return true;
        }
        return false;
    }

    public boolean removeOVChipkaart(int kaartNummer) {
        if (ovChipkaartNummers.contains(kaartNummer)) {
            ovChipkaartNummers.remove(kaartNummer);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_nummer=" + product_nummer +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + Beschrijving + '\'' +
                ", prijs=" + prijs +
                ", ovChipkaartNummers=" + (ovChipkaartNummers.isEmpty() ? "Geen OVChipkaarten" : ovChipkaartNummers.toString()) +
                '}';
    }


}
