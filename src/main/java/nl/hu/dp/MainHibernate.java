package nl.hu.dp;

import nl.hu.dp.DAO.*;
import nl.hu.dp.DAO.ReizigerDAO;
import nl.hu.dp.DAO.ReizigerDAOPsql;
import nl.hu.dp.MOD.Adres;
import nl.hu.dp.MOD.Reiziger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.util.List;

public class MainHibernate {
    public static void main(String[] args) {
        // Maak een SessionFactory aan via de HibernateUtil klasse
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Maak een ReizigerDAOHibernate object aan met de sessionFactory
        ReizigerDAOHibernate reizigerDAOHibernate = new ReizigerDAOHibernate(sessionFactory);
        AdresDAOHibernate adresDAOHibernate  = new AdresDAOHibernate(session);


        try {
//            testReizigerDAO(reizigerDAOHibernate);
            testAdresDAOHibernate(adresDAOHibernate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Testmethode moet buiten de main-methode worden gedefinieerd
    private static void testReizigerDAO(ReizigerDAOHibernate rdao) throws SQLException {
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
        Reiziger sietske = new Reiziger(77,"S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }

    private static void testAdresDAOHibernate(AdresDAOHibernate adao, ReizigerDAOHibernate reizigerDAOHibernate) throws SQLException {
        System.out.println("\n---------- Test AdresDAOHibernate ----------");

        // 1. Maak een nieuw adres aan en sla het op in de database
        Session session = HibernateUtil.getSessionFactory().openSession();
        Reiziger reiziger = session.get(Reiziger.class, 1); // Zoek een reiziger op in de database, bijvoorbeeld met ID 1

        Adres nieuwAdres = new Adres();
        nieuwAdres.setPostcode("1234AB");
        nieuwAdres.setHuisnummer("12");
        nieuwAdres.setStraat("Nieuwe Straat");
        nieuwAdres.setWoonplaats("Utrecht");
        nieuwAdres.setReiziger(reiziger);

        reiziger.setAdres(nieuwAdres); // Zet de inverse relatie
        reizigerDAOHibernate.save(reiziger); // Sla de reiziger (met adres) op



        System.out.println("[Test] Eerst, het nieuwe adres opslaan");
        System.out.println("Adres opgeslagen: " + nieuwAdres);

        // 2. Haal het zojuist opgeslagen adres op met findByReiziger
        System.out.println("[Test] Het adres ophalen voor de reiziger met ID: " + reiziger.getId());
        Adres opgehaaldAdres = adao.findByReiziger(reiziger.getId());
        System.out.println("Opgehaald adres: " + opgehaaldAdres);

        // 3. Werk het adres bij en sla de wijzigingen op
        System.out.println("[Test] Het adres updaten met een nieuwe postcode en straat");
        opgehaaldAdres.setPostcode("4321BA");
        opgehaaldAdres.setStraat("Oude Straat");
        adao.update(opgehaaldAdres);
        System.out.println("Adres na update: " + opgehaaldAdres);

        // 4. Haal alle adressen op en print ze
        System.out.println("[Test] Alle adressen ophalen:");
        List<Adres> adressen = adao.findAll();
        for (Adres adres : adressen) {
            System.out.println(adres);
        }

        // 5. Verwijder het adres en controleer of het verwijderd is
        System.out.println("[Test] Het adres verwijderen:");
        adao.delete(opgehaaldAdres);
        Adres verwijderdAdres = adao.findByReiziger(reiziger.getId());
        if (verwijderdAdres == null) {
            System.out.println("Adres succesvol verwijderd");
        } else {
            System.out.println("Adres verwijderen is mislukt: " + verwijderdAdres);
        }

        session.close();
    }

}

