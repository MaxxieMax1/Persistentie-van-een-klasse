package nl.hu.dp.MOD;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_nummer")
    private int product_nummer;

    @Column(name = "naam")
    private String naam;

    @Column(name = "beschrijving")
    private String beschrijving;

    @Column(name = "prijs")
    private double prijs;

    @ManyToMany(mappedBy = "producten")
    private List<OVChipkaart> ovchipkaarten = new ArrayList<>();

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
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvchipkaarten() {
        return ovchipkaarten;
    }

    public void setOvchipkaarten(List<OVChipkaart> ovchipkaarten) {
        this.ovchipkaarten = ovchipkaarten;
    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        if (!this.ovchipkaarten.contains(ovChipkaart)) {
            this.ovchipkaarten.add(ovChipkaart);
            ovChipkaart.getProducten().add(this);  // Ensuring bidirectional relationship
        }
    }

    // Method to remove an OVChipkaart
    public void removeOVChipkaart(OVChipkaart ovChipkaart) {
        if (this.ovchipkaarten.contains(ovChipkaart)) {
            this.ovchipkaarten.remove(ovChipkaart);
            ovChipkaart.getProducten().remove(this);  // Ensuring bidirectional relationship
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_nummer=" + product_nummer +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                ", ovChipkaartNummers=" + ovchipkaarten +
                '}';
    }
}
