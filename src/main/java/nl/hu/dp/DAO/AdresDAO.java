package nl.hu.dp.DAO;

import nl.hu.dp.MOD.Adres;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    public boolean save(Adres adres) throws SQLException;
    public boolean update(Adres adres);
    public boolean delete(Adres adres);
    public Adres findByReiziger(int id);
    public List<Adres> findAll();
}
