package nl.hu.dp.DAO;

import nl.hu.dp.MOD.Adres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdresDAOPsql  implements AdresDAO{
    private Connection conn;
    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
    }


    @Override
    public boolean save(Adres adres) throws SQLException {
        PreparedStatement statement = this.conn.prepareStatement("INSERT INTO adres " +
                "(adres_id," +
                " postcode," +
                " huisnummer," +
                " straat," +
                "woonplaats," +
                " reiziger_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)");
        statement.setInt(1, adres.getId());
        statement.setString(2, adres.getPostcode());
        statement.setString(3, adres.getHuisnummer());
        statement.setString(4, adres.getStraat());
        statement.setString(5, adres.getWoonplaats());
        statement.setInt(6, adres.getReiziger().getId());

        statement.executeUpdate();
        statement.close();
        return true;
    }

    @Override
    public boolean update(Adres adres) {
        return false;
    }

    @Override
    public boolean delete(Adres adres) {
        return false;
    }

    @Override
    public Adres findByReiziger(int id) {
        return null;
    }

    @Override
    public List<Adres> findAll() {
        return List.of();
    }
}
