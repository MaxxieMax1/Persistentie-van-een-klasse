package nl.hu.dp;

import nl.hu.dp.DAO.*;
import nl.hu.dp.MOD.Adres;
import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Product;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Connection myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchipkaart", "postgres", "H0meW0rk");
            AdresDAOPsql adresDAOPsql = new AdresDAOPsql(myConn);
            OVChipkaartDAOPsql ovChipkaartDAOPsql = new OVChipkaartDAOPsql(myConn);
            ProductDAOPsql productDAOPsql = new ProductDAOPsql(myConn);
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(myConn, adresDAOPsql, ovChipkaartDAOPsql);

            productDAOPsql.setOvChipkaartDAO(ovChipkaartDAOPsql);
            ovChipkaartDAOPsql.setReizigerDAO(reizigerDAOPsql);

            // Scanner voor gebruikersinput
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\nKies een testoptie:");
                System.out.println("1. Test Veel-op-Veel Relatie");
                System.out.println("2. Test Product bewerkingen");
                System.out.println("3. Afsluiten");
                System.out.print("Voer je keuze in (1-3): ");

                String keuze = scanner.nextLine();

                switch (keuze) {
                    case "1":
                        testVeelOpVeel(ovChipkaartDAOPsql, productDAOPsql, reizigerDAOPsql);
                        break;
                    case "2":
                        bla(ovChipkaartDAOPsql, productDAOPsql, reizigerDAOPsql, adresDAOPsql);
                        break;
                    case "3":
                        System.out.println("Afsluiten...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Ongeldige keuze, probeer het opnieuw.");
                }
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private static void bla(OVChipkaartDAO ovChipkaartDAO, ProductDAO productDAO, ReizigerDAO reizigerDAO, AdresDAO adresDAO) throws SQLException {
        List<Product> products = productDAO.findAll();
        for (Product product : products){
            System.out.println(product);
        }
        Product p = new Product();
        p.setNaam("STUDENTJEOV");
        p.setBeschrijving("Dingetje voor studenten je weet wel");
        p.setPrijs(19);
        p.setProduct_nummer(999);
        p.addOVChipkaart(ovChipkaartDAO.findByKaartNummer(57401).getKaart_nummer());
        p.addOVChipkaart(ovChipkaartDAO.findByKaartNummer(79625).getKaart_nummer());
        p.addOVChipkaart(ovChipkaartDAO.findByKaartNummer(18326).getKaart_nummer());

//        productDAO.delete(p);

        productDAO.save(p);

        productDAO.update(p);
        System.out.println("-----------------------------");
        List<Product> products1 = productDAO.findAll();
        for (Product product : products1){
            System.out.println(product);
        }
        productDAO.delete(p);
    }

    private static void testVeelOpVeel(OVChipkaartDAO ovChipkaartDAO, ProductDAO productDAO, ReizigerDAO reizigerDAO) throws SQLException {
        List<Reiziger> reizigers = reizigerDAO.findAll();
        System.out.println("[Test] Reiziger.findAll() geeft de volgende producten:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println("--------------------------------------------------------------------");

        List<Product> products = productDAO.findAll();
        System.out.println("[Test] ProductDAO.findAll() geeft de volgende producten:");
        for (Product p : products) {
            System.out.println(p);
        }
        System.out.println("--------------------------------------------------------------------");

        List<OVChipkaart> ovChipkaarts = ovChipkaartDAO.findAll();
        for (OVChipkaart ov : ovChipkaarts) {
            System.out.println(ov);
        }

        System.out.println("--------------------------------------------------------------------");

        // Test voor OVChipkaart aanmaken
        int newKaartNummer = 12345;
        if (ovChipkaartDAO.findByKaartNummer(newKaartNummer) == null) {
            OVChipkaart ovChipkaart = new OVChipkaart();
            ovChipkaart.setKaart_nummer(newKaartNummer);
            ovChipkaart.setGeldig_tot(Date.valueOf("2025-12-31"));
            ovChipkaart.setKlasse(1);
            ovChipkaart.setSaldo(50.0);
            ovChipkaart.setReiziger(reizigerDAO.findBy(2)); // Zorg ervoor dat reiziger met id 2 bestaat
            ovChipkaartDAO.save(ovChipkaart);
            System.out.println("[Test] Nieuwe OVChipkaart opgeslagen.");
        }

        // Testen van het toevoegen van een nieuw product
        Product newProduct = new Product();
        newProduct.setProduct_nummer(12345);
        newProduct.setNaam("Test Product");
        newProduct.setBeschrijving("Dit is een test product.");
        newProduct.setPrijs(9.99);

        // Voeg de nieuwe OVChipkaart toe aan het product
        newProduct.addOVChipkaart(newKaartNummer); // Voeg het kaartnummer toe

        // Product opslaan in de database
        boolean saveResult = productDAO.save(newProduct);
        System.out.println("[Test] Product opslaan resultaat: " + saveResult);

        // Update het product
        newProduct.setNaam("Nieuwe naam Product");
        boolean updateResult = productDAO.update(newProduct);
        System.out.println("[Test] Product updaten resultaat: " + updateResult);

        // Zoek producten via OVChipkaart
        List<Product> foundProducts = productDAO.findByOVChipkaart(ovChipkaartDAO.findByKaartNummer(newKaartNummer));
        System.out.println("[Test] Producten gevonden voor OVChipkaart:");
        if (foundProducts.isEmpty()) {
            System.out.println("Geen producten gevonden voor deze OVChipkaart.");
        } else {
            for (Product p : foundProducts) {
                System.out.println(p);
            }
        }

        // OV kaart verwijderen terwijl er een link is met een product
        boolean deleteOV = ovChipkaartDAO.delete(ovChipkaartDAO.findByKaartNummer(newKaartNummer));
        System.out.println("[Test] OV verwijderen resultaat: " + deleteOV);

        // Product verwijderen
        boolean deleteResult = productDAO.delete(newProduct);
        System.out.println("[Test] Product verwijderen resultaat: " + deleteResult);
    }

}
