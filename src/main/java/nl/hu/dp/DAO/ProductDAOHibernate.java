package nl.hu.dp.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductDAOHibernate implements ProductDAO {
    private EntityManager entityManager;

    public ProductDAOHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        entityManager.getTransaction().begin();
        entityManager.persist(product);
        entityManager.getTransaction().commit();
        return true;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(product) ? product : entityManager.merge(product));
        entityManager.getTransaction().commit();
        return true;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        entityManager.getTransaction().begin();
        entityManager.merge(product);
        entityManager.getTransaction().commit();
        return true;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        TypedQuery<Product> query = entityManager.createQuery(
                "SELECT p FROM Product p JOIN p.ovchipkaarten o WHERE o.kaart_nummer = :kaart_nummer", Product.class
        );
        query.setParameter("kaart_nummer", ovChipkaart.getKaart_nummer());
        return query.getResultList();
    }

    @Override
    public List<Product> findAll() throws SQLException {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p", Product.class);
        return query.getResultList();
    }
}
