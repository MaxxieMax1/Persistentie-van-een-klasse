package nl.hu.dp.DAO;

//import com.sun.jdi.connect.spi.Connection;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{
    private Connection conn;
    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }
    AdresDAO adresDAO;


    @Override
    public boolean save(Reiziger reiziger) {
        String query = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, reiziger.getId());
            ps.setString(2, reiziger.getVoorletters());
            ps.setString(3, reiziger.getTussenvoegsel());
            ps.setString(4, reiziger.getAchternaam());
            ps.setDate(5, reiziger.getDatum());

            int rowsAffected = ps.executeUpdate();

            if (this.adresDAO != null) {
                this.adresDAO.save(reiziger.getAdres());
            }

            return rowsAffected > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean update(Reiziger reiziger) {
        String query = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        PreparedStatement ps = null;
        try  {
            ps = conn.prepareStatement(query);
            ps.setString(1, reiziger.getVoorletters());
            ps.setString(2, reiziger.getTussenvoegsel());
            ps.setString(3, reiziger.getAchternaam());
            ps.setDate(4, reiziger.getDatum());
            ps.setInt(5, reiziger.getId());
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean delete(Reiziger reiziger) {
        String query = "DELETE FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement ps = null;
        try  {
            ps = conn.prepareStatement(query);
            ps.setInt(1, reiziger.getId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Reiziger findBy(int id) {
        String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Reiziger(
                            rs.getInt("reiziger_id"),
                            rs.getString("voorletters"),
                            rs.getString("tussenvoegsel"),
                            rs.getString("achternaam"),
                            rs.getDate("geboortedatum")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Reiziger> findByGbdatum(Date date) {
        String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        List<Reiziger> reizigers = new ArrayList<>();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            ps.setDate(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reizigers.add(new Reiziger(
                            rs.getInt("reiziger_id"),
                            rs.getString("voorletters"),
                            rs.getString("tussenvoegsel"),
                            rs.getString("achternaam"),
                            rs.getDate("geboortedatum")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;
    }
    @Override
    public List<Reiziger> findAll() {
        String query = "SELECT * FROM reiziger";
        List<Reiziger> reizigers = new ArrayList<>();
        try  {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                reizigers.add(new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;
    }
}
