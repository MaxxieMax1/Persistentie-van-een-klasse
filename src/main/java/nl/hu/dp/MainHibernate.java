package nl.hu.dp;

import nl.hu.dp.DAO.*;
import nl.hu.dp.DAO.ReizigerDAO;
import nl.hu.dp.DAO.ReizigerDAOPsql;
import nl.hu.dp.MOD.Adres;
import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Product;
import nl.hu.dp.MOD.Reiziger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainHibernate {
    public static void main(String[] args) {
        // Maak een SessionFactory aan via de HibernateUtil klasse
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Maak een ReizigerDAOHibernate object aan met de sessionFactory
        ReizigerDAOHibernate reizigerDAOHibernate = new ReizigerDAOHibernate(sessionFactory);
        AdresDAOHibernate adresDAOHibernate = new AdresDAOHibernate(session);
        OVChipkaartDAOHibernate ovChipkaartDAOHibernate = new OVChipkaartDAOHibernate(session);
        ProductDAOHibernate productDAOHibernate = new ProductDAOHibernate(session);


        try {
//
            testVeelOpVeel(ovChipkaartDAOHibernate, productDAOHibernate, reizigerDAOHibernate);
        } finally {

        }
    }

    private static void testVeelOpVeel(OVChipkaartDAOHibernate ovChipkaartDAO, ProductDAOHibernate productDAO, ReizigerDAOHibernate reizigerDAO) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();


        try {
            session.clear();


            List<Product> products = productDAO.findAll();
            for (Product p : products) {
                System.out.println(p);
            }
            System.out.println("====================================================================");

            List<OVChipkaart> ovChipkaarts = ovChipkaartDAO.findAll();
            for (OVChipkaart ov : ovChipkaarts) {
                System.out.println(ov);
            }

            if (ovChipkaartDAO.findByKaartNummer(12345) == null) {
                OVChipkaart ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaart_nummer(12345);
                ovChipkaart.setGeldig_tot(Date.valueOf("2025-12-31"));
                ovChipkaart.setKlasse(1);
                ovChipkaart.setSaldo(50.0);
                ovChipkaart.setReiziger(reizigerDAO.findBy(2));
                ovChipkaartDAO.save(ovChipkaart);
                System.out.println("[Test] Nieuwe OVChipkaart opgeslagen.");
            }

            // Testen van het toevoegen van een nieuw product
            Product newProduct = new Product();
            newProduct.setProduct_nummer(12345); // Zorg ervoor dat dit nummer uniek is
            newProduct.setNaam("Test Product");
            newProduct.setBeschrijving("Dit is een test product.");
            newProduct.setPrijs(9.99);
            boolean deleteResult1 = productDAO.delete(newProduct);
            System.out.println("[Test] Product verwijderen resultaat: " + deleteResult1);

            int i = 57401;
            newProduct.addOVChipkaart(ovChipkaartDAO.findByKaartNummer(i));

            int x = 12345;
            newProduct.setProduct_nummer(x);
            newProduct.setNaam("Nieuwe naam Product");
            boolean updateResult = productDAO.update(newProduct);
            System.out.println("[Test] Product updaten resultaat: " + updateResult);

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


            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

