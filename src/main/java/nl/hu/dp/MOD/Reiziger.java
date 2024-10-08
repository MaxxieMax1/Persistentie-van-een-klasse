package nl.hu.dp.MOD;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date datum;

    private Adres adres;
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

    public void addOvChipkaart(OVChipkaart ovChipkaart) {
        this.OVChipkaart.add(ovChipkaart);
    }

    public String toString() {
        String naam = voorletters + (tussenvoegsel == null || tussenvoegsel.isEmpty() ? " " : " " + tussenvoegsel + " ") + achternaam;
        String gebDatum = "geb. " + datum;
        String adresStr = adres != null ? ", " + adres : "";  // Controleer of een adres aanwezig is
        return "Reiziger {#" + id + " " + naam + ", " + gebDatum + adresStr + "}";
    }
}
