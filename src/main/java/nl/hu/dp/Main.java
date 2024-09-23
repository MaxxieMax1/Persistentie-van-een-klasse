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
//            testAdresDAO(adresDAOPsql, reizigerDAOPsql);
//            testReizigerEnAdresUpdateEnVerwijder(reizigerDAOPsql, adresDAOPsql);
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
    }

    private static void testReizigerEnAdresUpdateEnVerwijder(ReizigerDAOPsql rdao, AdresDAO adao) throws SQLException {
        // Voeg een nieuwe reiziger met adres toe
        String gbdatum = "1990-01-01";
        Reiziger testReiziger = new Reiziger(100, "T", "van", "Test", java.sql.Date.valueOf(gbdatum));

        Adres testAdres = new Adres();
        testAdres.setId(100);
        testAdres.setPostcode("1234AB");
        testAdres.setHuisnummer("10");
        testAdres.setStraat("Teststraat");
        testAdres.setWoonplaats("Teststad");
        testAdres.setReiziger(testReiziger);

        testReiziger.setAdres(testAdres);
        // Sla de reiziger op
        System.out.println("Opslaan van nieuwe reiziger en adres:");
        rdao.save(testReiziger);
        System.out.println("Reiziger en adres opgeslagen.");

        // Controleer of de reiziger en het adres correct zijn opgeslagen
        Reiziger gevondenReiziger = rdao.findBy(100);
        System.out.println("Opgeslagen Reiziger: " + gevondenReiziger);
        System.out.println("Bijbehorend Adres: " + gevondenReiziger.getAdres());

        // Update de reiziger en het adres
        System.out.println("\nUpdaten van de reiziger en zijn adres:");
        gevondenReiziger.setAchternaam("TestUpdated");
        gevondenReiziger.getAdres().setPostcode("4321BA");
        gevondenReiziger.getAdres().setWoonplaats("UpdateStad");

        rdao.update(gevondenReiziger);
        Reiziger updatedReiziger = rdao.findBy(100);
        System.out.println("Geüpdatete Reiziger: " + updatedReiziger);
        System.out.println("Geüpdatete Adres: " + updatedReiziger.getAdres());

        // Verwijder de reiziger en controleer of het adres ook verwijderd is
        System.out.println("\nVerwijderen van reiziger en zijn adres:");
        rdao.delete(updatedReiziger);

        // Controleer of de reiziger is verwijderd
        Reiziger verwijderdeReiziger = rdao.findBy(100);
        System.out.println("Verwijderde Reiziger: " + verwijderdeReiziger);

        // Controleer of het adres ook verwijderd is
        Adres verwijderdeAdres = adao.findByReiziger(100);
        System.out.println("Verwijderde Adres: " + verwijderdeAdres);

        List<Reiziger> testje = rdao.findAll();
        for(Reiziger r: testje){
            System.out.println(r);
        }
    }


    private static void testAdresDAO(AdresDAO adao, ReizigerDAOPsql rdao) throws SQLException {
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        rdao.save(sietske);

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
        Adres foundAdres = adao.findByReiziger(2);
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

        rdao.delete(sietske);

    }

    private static void testOVChipkaartDAO(OVChipkaartDAO ovdao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
//        ben lui om telkens het aan te passen dus ik vv m als ie er nog toevallig in staat
        if (rdao.findBy(88) != null) {
            rdao.delete(rdao.findBy(88));
        }

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

        OVChipkaart ovChipkaart2 = new OVChipkaart();
        ovChipkaart2.setKaart_nummer(636341);
        ovChipkaart2.setGeldig_tot(Date.valueOf("2025-12-31"));
        ovChipkaart2.setKlasse(2);
        ovChipkaart2.setSaldo(20.0);
        ovChipkaart2.setReiziger(testReiziger);


        // Test de save() functionaliteit
        boolean saved = ovdao.save(ovChipkaart);
        System.out.println("OVChipkaart saved: " + saved);

        boolean saved2 = ovdao.save(ovChipkaart2);
        System.out.println("OVChipkaart2 saved: " + saved2);

        // Test findByReiziger() om de kaarten voor een specifieke reiziger te vinden
        List<OVChipkaart> kaartenVoorReiziger = ovdao.findByReiziger(testReiziger);
        System.out.println("Found OVChipkaarten by Reiziger ID " + testReiziger.getId() + ":");
        for (OVChipkaart ov : kaartenVoorReiziger) {
            System.out.println(ov);
        }

        // Test de update() functionaliteit
        ovChipkaart.setKlasse(2);
        ovChipkaart.setSaldo(99999.0);
        ovdao.update(ovChipkaart);
        OVChipkaart updatedOVChipkaart = ovdao.findByKaartNummer(12345);
        System.out.println("OVChipkaart updated: " + updatedOVChipkaart);

        // Test de delete() functionaliteit
        boolean deleted = ovdao.delete(ovChipkaart);
        System.out.println("OVChipkaart deleted: " + deleted);
        boolean deleted2 = ovdao.delete(ovChipkaart2);
        System.out.println("OVChipkaart2 deleted: " + deleted2);

    }


}
