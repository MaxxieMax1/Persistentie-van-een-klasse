package nl.hu.dp;

import nl.hu.dp.DAO.*;
import nl.hu.dp.MOD.Adres;
import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Product;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

            testVeelOpVeel(ovChipkaartDAOPsql, productDAOPsql, reizigerDAOPsql);


        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }

    private static void testVeelOpVeel(OVChipkaartDAO ovChipkaartDAO, ProductDAO productDAO, ReizigerDAO reizigerDAO) throws SQLException {
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
        // Dit wordt de test voor OVChipkaart
        if (ovChipkaartDAO.findByKaartNummer(12345) == null) {
            OVChipkaart ovChipkaart = new OVChipkaart();
            ovChipkaart.setKaart_nummer(12345);
            ovChipkaart.setGeldig_tot(Date.valueOf("2025-12-31"));
            ovChipkaart.setKlasse(1);
            ovChipkaart.setSaldo(50.0);
            ovChipkaart.setReiziger(reizigerDAO.findBy(2)); // Zorg ervoor dat reiziger met id 2 bestaat
            ovChipkaartDAO.save(ovChipkaart);
            System.out.println("[Test] Nieuwe OVChipkaart opgeslagen.");
        }


        // Testen van het toevoegen van een nieuw product
        Product newProduct = new Product();
        newProduct.setProduct_nummer(12345); // Zorg ervoor dat dit nummer uniek is
        newProduct.setNaam("Test Product");
        newProduct.setBeschrijving("Dit is een test product.");
        newProduct.setPrijs(9.99);

        // Voeg kaartnummers toe in plaats van OVChipkaart objecten
        List<Integer> kaartnummers = new ArrayList<>();
        kaartnummers.add(57401); // Zorg ervoor dat dit kaartnummer bestaat in je database
        newProduct.setOvChipkaartNummers(kaartnummers);

        // Product opslaan in de database
        boolean saveResult = productDAO.save(newProduct);
        System.out.println("[Test] Product opslaan resultaat: " + saveResult);

        // Update methode
        kaartnummers.add(12345); // Voeg het nieuwe kaartnummer toe
        newProduct.setOvChipkaartNummers(kaartnummers);
        newProduct.setNaam("Nieuwe naam Product");
        boolean updateResult = productDAO.update(newProduct);
        System.out.println("[Test] Product updaten resultaat: " + updateResult);

        // findByOV
        List<Product> foundProducts = productDAO.findByOVChipkaart(ovChipkaartDAO.findByKaartNummer(12345));
        System.out.println("[Test] Producten gevonden voor OVChipkaart:");
        for (Product p : foundProducts) {
            System.out.println(p);
        }

        // OV kaart verwijderen terwijl er een link is met een product
        boolean deleteOV = ovChipkaartDAO.delete(ovChipkaartDAO.findByKaartNummer(12345));
        System.out.println("[Test] OV verwijderen resultaat: " + deleteOV);

        // delete
        boolean deleteResult = productDAO.delete(newProduct);
        System.out.println("[Test] Product verwijderen resultaat: " + deleteResult);
    }
}
