package nl.hu.dp.DAO;

import nl.hu.dp.MOD.Adres;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql  implements AdresDAO{
    private Connection conn;
    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
    }


    @Override
    public boolean save(Adres adres) throws SQLException {
        PreparedStatement statement = this.conn.prepareStatement(
                "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)"
        );
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
        String query = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ?  WHERE adres_id = ?";
        PreparedStatement ps = null;
        try  {
            ps = conn.prepareStatement(query);
            ps.setString(1, adres.getPostcode());
            ps.setString(2, adres.getHuisnummer());
            ps.setString(3, adres.getStraat());
            ps.setString(4, adres.getWoonplaats());
            ps.setInt(5, adres.getReiziger().getId());
            ps.setInt(6, adres.getId());

            int rowsAffected = ps.executeUpdate();
            ps.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        String query = "DELETE FROM adres WHERE adres_id = ?";
        PreparedStatement ps = null;
        try  {
            ps = conn.prepareStatement(query);
            ps.setInt(1, adres.getId());
            int rowsAffected = ps.executeUpdate();
            ps.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Adres findByReiziger(int reizigerId) {
        try {
            String query = "SELECT * FROM adres WHERE reiziger_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, reizigerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Adres adres = new Adres();
                adres.setId(rs.getInt("adres_id"));
                adres.setPostcode(rs.getString("postcode"));
                adres.setHuisnummer(rs.getString("huisnummer"));
                adres.setStraat(rs.getString("straat"));
                adres.setWoonplaats(rs.getString("woonplaats"));

                Reiziger reiziger = new Reiziger();
                reiziger.setId(reizigerId);
                adres.setReiziger(reiziger);

                rs.close();
                ps.close();

                return adres;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Adres> findAll() {
        String query = "SELECT * FROM adres";
        List<Adres> adressen = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Adres adres = new Adres();
                adres.setId(rs.getInt("adres_id"));
                adres.setPostcode(rs.getString("postcode"));
                adres.setHuisnummer(rs.getString("huisnummer"));
                adres.setStraat(rs.getString("straat"));
                adres.setWoonplaats(rs.getString("woonplaats"));

                int reizigerId = rs.getInt("reiziger_id");
                Reiziger reiziger = new Reiziger();
                reiziger.setId(reizigerId);
                adres.setReiziger(reiziger);

                adressen.add(adres);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return adressen;
    }
}
