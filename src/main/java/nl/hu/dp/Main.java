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
            Connection myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/ovchipkaart", "postgres", "H0meW0rk");
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


    }

    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));

        Adres adres = new Adres();
        adres.setId(132);
        adres.setPostcode("1234AB");
        adres.setHuisnummer("12");
        adres.setStraat("Hoofdstraat");
        adres.setWoonplaats("Utrecht");
        adres.setReiziger(sietske);

        List<Adres> adreslijst = adao.findAll();

        System.out.print("[Test] Eerst " + adreslijst.size() + " reizigers, na ReizigerDAO.save() ");
        adao.save(adres);

        System.out.println(adreslijst.size() + " reizigers\n");
    }
}
