package nl.hu.dp.MOD;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reiziger")
public class Reiziger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reiziger_id")
    private int id;
    @Column(name = "voorletters", nullable = false)
    private String voorletters;
    @Column(name = "tussenvoegsel")
    private String tussenvoegsel;
    @Column(name = "achternaam", nullable = false)
    private String achternaam;
    @Column(name = "geboortedatum", nullable = false)
    private Date datum;
    @OneToOne(mappedBy = "reiziger", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Adres adres;
    @OneToMany(mappedBy = "reiziger", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OVChipkaart> OVChipkaart = new ArrayList<>();

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date datum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.datum = datum;
    }

    public Reiziger() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<nl.hu.dp.MOD.OVChipkaart> getOVChipkaart() {
        return OVChipkaart;
    }

    public void setOVChipkaart(List<nl.hu.dp.MOD.OVChipkaart> OVChipkaart) {
        this.OVChipkaart = OVChipkaart;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reiziger {#").append(id).append(" ")
                .append(voorletters).append(" ")
                .append((tussenvoegsel == null || tussenvoegsel.isEmpty()) ? "" : tussenvoegsel + " ")
                .append(achternaam).append(", geb. ").append(datum);

        if (adres != null) {
            sb.append(", ").append(adres);
        }

        if (!OVChipkaart.isEmpty()) {
            sb.append(", OVChipkaarten: [");
            for (OVChipkaart ovChipkaart : OVChipkaart) {
                sb.append(ovChipkaart).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
}
