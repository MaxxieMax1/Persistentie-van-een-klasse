package nl.hu.dp.DAO;

import nl.hu.dp.MOD.Reiziger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger) throws SQLException;
    public boolean update(Reiziger reiziger);
    public boolean delete(Reiziger reiziger);
    public Reiziger findBy(int id);
    public List<Reiziger> findByGbdatum(Date date);
    public List<Reiziger> findAll();
}
