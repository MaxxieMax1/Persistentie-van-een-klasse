package nl.hu.dp.DAO;

import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    boolean save(OVChipkaart ovChipkaart) throws SQLException;
    boolean update(OVChipkaart ovChipkaart) throws SQLException;
    boolean delete(OVChipkaart ovChipkaart) throws SQLException;
    List<OVChipkaart> getAll() throws SQLException;
    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    OVChipkaart findByKaartNummer(int kaartnummer) throws SQLException;


}
