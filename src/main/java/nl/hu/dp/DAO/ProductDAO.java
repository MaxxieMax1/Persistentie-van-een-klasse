package nl.hu.dp.DAO;

import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product) throws SQLException;
    boolean delete(Product product) throws SQLException;
    boolean update(Product product)throws SQLException;
    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart)throws SQLException;
    List<Product> findAll()throws SQLException;
}
