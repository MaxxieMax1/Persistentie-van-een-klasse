package nl.hu.dp;

import nl.hu.dp.DAO.*;
import nl.hu.dp.MOD.Adres;
import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Connection myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchipkaart", "postgres", "H0meW0rk");
            AdresDAOPsql adresDAOPsql = new AdresDAOPsql(myConn);
            OVChipkaartDAOPsql ovChipkaartDAOPsql = new OVChipkaartDAOPsql(myConn);
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(myConn, adresDAOPsql, ovChipkaartDAOPsql);

            ovChipkaartDAOPsql.setReizigerDAO(reizigerDAOPsql);

            testReizigerDAO(reizigerDAOPsql);
            testAdresDAO(adresDAOPsql);
            testOVChipkaartDAO(ovChipkaartDAOPsql, reizigerDAOPsql);

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
//        Heb ik er in staan zodat je het kan gebruiken in de testADresDAO
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

        // Delete test data
        boolean deleted = adao.delete(adres);
        System.out.println("Adres deleted: " + deleted);

        adressen = adao.findAll();

    }

    private static void testOVChipkaartDAO(OVChipkaartDAO ovdao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Maak een test Reiziger aan
        Reiziger testReiziger = new Reiziger(88, "S", "", "Boers", java.sql.Date.valueOf("1981-03-14"));
        rdao.save(testReiziger);

        // Maak een test OVChipkaart aan
        OVChipkaart ovChipkaart = new OVChipkaart();
        ovChipkaart.setKaart_nummer(12345);
        ovChipkaart.setGeldig_tot(Date.valueOf("2025-12-31"));
        ovChipkaart.setKlasse(1);
        ovChipkaart.setSaldo(50.0);
        ovChipkaart.setReiziger(testReiziger);

        // Test de save() functionaliteit
        boolean saved = ovdao.save(ovChipkaart);
        System.out.println("OVChipkaart saved: " + saved);

        // Test findByReiziger() om de kaarten voor een specifieke reiziger te vinden
        List<OVChipkaart> kaartenVoorReiziger = ovdao.findByReiziger(testReiziger);
        System.out.println("Found OVChipkaarten by Reiziger ID " + testReiziger.getId() + ":");
        for (OVChipkaart ov : kaartenVoorReiziger) {
            System.out.println(ov);
        }

        // Test de update() functionaliteit
        ovChipkaart.setKlasse(2);
        ovdao.update(ovChipkaart);
        OVChipkaart updatedOVChipkaart = ovdao.findByKaartNummer(12345);
        System.out.println("OVChipkaart updated: " + updatedOVChipkaart);

        // Test de delete() functionaliteit
        boolean deleted = ovdao.delete(ovChipkaart);
        System.out.println("OVChipkaart deleted: " + deleted);

        // Zorg dat de kaart niet meer bestaat in de database
        OVChipkaart deletedOVChipkaart = ovdao.findByKaartNummer(12345);
        if (deletedOVChipkaart == null) {
            System.out.println("OVChipkaart met kaartnummer 12345 is succesvol verwijderd.");
        } else {
            System.out.println("Er is iets misgegaan, OVChipkaart bestaat nog steeds: " + deletedOVChipkaart);
        }
    }


}
