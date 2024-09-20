package nl.hu.dp;

import nl.hu.dp.DAO.AdresDAO;
import nl.hu.dp.DAO.AdresDAOPsql;
import nl.hu.dp.DAO.ReizigerDAO;
import nl.hu.dp.DAO.ReizigerDAOPsql;
import nl.hu.dp.MOD.Adres;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Connection myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchipkaart", "postgres", "H0meW0rk");
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(myConn);
            testReizigerDAO(reizigerDAOPsql);
            AdresDAOPsql adresDAOPsql = new AdresDAOPsql(myConn);
            testAdresDAO(adresDAOPsql);

        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     * <p>
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Test de update-functionaliteit
        System.out.println("[Test] Update de naam van de reiziger met id 77.");
        sietske.setAchternaam("Bakker");
        rdao.update(sietske);
        Reiziger updatedSietske = rdao.findBy(77);
        System.out.println("Reiziger met id 77 na update: " + updatedSietske + "\n");

        // Test de findByGbdatum-functionaliteit
        System.out.println("[Test] Vind de geboortedatum van de reiziger met geb " + gbdatum);
        List<Reiziger> reizigersMetGeb = rdao.findByGbdatum(java.sql.Date.valueOf(gbdatum));
        System.out.println("Reizigers met deze geboortedatum:");
        for (Reiziger r : reizigersMetGeb) {
            System.out.println(r);
        }
        System.out.println();

        // Test de delete-functionaliteit
        System.out.println("[Test] Verwijder de reiziger met id 77.");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println("Aantal reizigers na ReizigerDAO.delete(): " + reizigers.size());
        System.out.println("[Test] Reiziger met id 77 zou verwijderd moeten zijn.");
        Reiziger deletedSietske = rdao.findBy(77);
        if (deletedSietske == null) {
            System.out.println("Reiziger met id 77 is succesvol verwijderd.\n");
        } else {
            System.out.println("Er is iets misgegaan, reiziger met id 77 bestaat nog steeds: " + deletedSietske + "\n");
        }
        Reiziger sietske2 = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        rdao.save(sietske);



    }

    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        Reiziger testReiziger = new Reiziger();
        testReiziger.setId(77);

        Adres adres = new Adres();
        adres.setId(12);
        adres.setPostcode("1234AB");
        adres.setHuisnummer("12A");
        adres.setStraat("Teststraat");
        adres.setWoonplaats("Teststad");
        adres.setReiziger(testReiziger);

        boolean saved = adao.save(adres);
        System.out.println("Adres saved: " + saved);

        List<Adres> adressen = adao.findAll();
        System.out.println("All Adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        // Test findByReiziger method
        Adres foundAdres = adao.findByReiziger(1);
        System.out.println("Found Adres by Reiziger ID 1: " + foundAdres);

        // Update test data
        adres.setPostcode("5678CD");
        adao.update(adres);
        Adres updatedAdres = adao.findByReiziger(77);
        System.out.println("Adres updated: " + updatedAdres);


//        System.out.println("[Test] Update de naam van de reiziger met id 77.");
//        sietske.setAchternaam("Bakker");
//        rdao.update(sietske);
//        Reiziger updatedSietske = rdao.findBy(77);
//        System.out.println("Reiziger met id 77 na update: " + updatedSietske + "\n");

        // Delete test data
        boolean deleted = adao.delete(adres);
        System.out.println("Adres deleted: " + deleted);

        adressen = adao.findAll();

    }
}
