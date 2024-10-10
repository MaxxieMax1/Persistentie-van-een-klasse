package nl.hu.dp.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Product;
import nl.hu.dp.MOD.Reiziger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    private EntityManager entityManager ;
    private ReizigerDAO reizigerDAO;
    private ProductDAO  productDAO;

    public OVChipkaartDAOHibernate(EntityManager entityManager ) {
        this.entityManager  = entityManager ;
    }

    public void setReizigerDAO(ReizigerDAO reizigerDAO) {
        this.reizigerDAO = reizigerDAO;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        entityManager.getTransaction().begin();
        entityManager.persist(ovChipkaart);
        entityManager.getTransaction().commit();

        return true;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        entityManager.getTransaction().begin();
        entityManager.merge(ovChipkaart);
        entityManager.getTransaction().commit();

        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        entityManager.getTransaction().begin();
        entityManager.remove(ovChipkaart);
        entityManager.getTransaction().commit();

        return true;
    }

    @Override
    public List<OVChipkaart> findAll() {
        TypedQuery <OVChipkaart> query = entityManager.createQuery("SELECT o FROM OVChipkaart o", OVChipkaart.class);
        return query.getResultList();
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        TypedQuery<OVChipkaart> query = entityManager.createQuery(
                "SELECT o FROM OVChipkaart o WHERE o.reiziger = :reiziger", OVChipkaart.class
        );
        query.setParameter("reiziger", reiziger);
        return query.getResultList();
    }

    @Override
    public OVChipkaart findByKaartNummer(int kaartNummer) {
        return entityManager.find(OVChipkaart.class, kaartNummer);
    }
}
