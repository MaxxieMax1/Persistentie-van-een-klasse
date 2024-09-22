package nl.hu.dp.DAO;

import nl.hu.dp.MOD.Reiziger;
import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Adres;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {

    private SessionFactory sessionFactory;

    public ReizigerDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(reiziger);  // Slaat de reiziger op (inclusief Adres en OVChipkaarten door CascadeType.ALL)
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(reiziger);  // Update de reiziger
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(reiziger);  // Verwijdert de reiziger
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reiziger findBy(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Reiziger.class, id);  // Haal reiziger op met het gegeven id
        }
    }

    @Override
    public List<Reiziger> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Reiziger> query = session.createQuery("from Reiziger", Reiziger.class);  // HQL-query om alle reizigers op te halen
            return query.list();
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) {
        try (Session session = sessionFactory.openSession()) {
            Query<Reiziger> query = session.createQuery("from Reiziger where datum = :datum", Reiziger.class);
            query.setParameter("datum", date);
            return query.list();
        }
    }
}
