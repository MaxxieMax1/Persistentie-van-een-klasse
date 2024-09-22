package nl.hu.dp.DAO;

import nl.hu.dp.MOD.Adres;
import nl.hu.dp.MOD.Reiziger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class AdresDAOHibernate implements AdresDAO {
    private Session session;

    public AdresDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Adres adres) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(adres);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(adres);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(adres);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Adres findByReiziger(int reizigerId) {
        try {
            return session.createQuery("from Adres where reiziger.id = :reizigerId", Adres.class)
                    .setParameter("reizigerId", reizigerId)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Adres> findAll() {
        try {
            return session.createQuery("from Adres", Adres.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
