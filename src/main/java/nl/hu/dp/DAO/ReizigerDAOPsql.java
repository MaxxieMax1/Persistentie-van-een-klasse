package nl.hu.dp.DAO;

//import com.sun.jdi.connect.spi.Connection;
import nl.hu.dp.MOD.Adres;
import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Product;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{
    private Connection conn;
    AdresDAO adresDAO;
    OVChipkaartDAO ovChipkaartDAO;

    public ReizigerDAOPsql(Connection conn, AdresDAO adresDAO, OVChipkaartDAO ovChipkaartDAO) {
        this.conn = conn;
        this.adresDAO = adresDAO;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }



    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        PreparedStatement ps = this.conn.prepareStatement(
                "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)"
        );
        ps.setInt(1, reiziger.getId());
        ps.setString(2, reiziger.getVoorletters());
        ps.setString(3, reiziger.getTussenvoegsel());
        ps.setString(4, reiziger.getAchternaam());
        ps.setDate(5, reiziger.getDatum());

        ps.executeUpdate();

        if (reiziger.getAdres() != null) {
            adresDAO.save(reiziger.getAdres());
        }
        List<OVChipkaart> ovChipkaarten = reiziger.getOVChipkaart();
        if (ovChipkaarten != null && !ovChipkaarten.isEmpty()) {
            for (OVChipkaart ovChipkaart : ovChipkaarten) {
                ovChipkaartDAO.save(ovChipkaart);
            }
        }

        ps.close();
        return true;
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

            if (reiziger.getAdres() != null) {
                if (adresDAO.findByReiziger(reiziger.getId()) != null) {
                    adresDAO.update(reiziger.getAdres());
                }else {
                    adresDAO.save(reiziger.getAdres());
                }
            }
            if (reiziger.getOVChipkaart() != null){
                List<OVChipkaart> bestaandeKaarten = ovChipkaartDAO.findByReiziger(reiziger);

                for (OVChipkaart ovChipkaart : reiziger.getOVChipkaart()) {
                    if (bestaandeKaarten.contains(ovChipkaart)) {
                        ovChipkaartDAO.update(ovChipkaart);
                    } else {
                        ovChipkaartDAO.save(ovChipkaart);
                    }
                }
            }

            ps.close();
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

            if (reiziger.getAdres() != null) {
                adresDAO.delete(reiziger.getAdres());
            }
            if (reiziger.getOVChipkaart() != null) {
                for (OVChipkaart ovChipkaart : reiziger.getOVChipkaart()) {
                    ovChipkaartDAO.delete(ovChipkaart);
                }
            }

            ps.setInt(1, reiziger.getId());
            int rowsAffected = ps.executeUpdate();
            ps.close();
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
                    Reiziger reiziger = new Reiziger(
                            rs.getInt("reiziger_id"),
                            rs.getString("voorletters"),
                            rs.getString("tussenvoegsel"),
                            rs.getString("achternaam"),
                            rs.getDate("geboortedatum")
                    );
                    Adres adres = adresDAO.findByReiziger(id);
                    reiziger.setAdres(adres);
                    List<OVChipkaart> ovchipkaarten = ovChipkaartDAO.findByReiziger(reiziger);
                    reiziger.setOVChipkaart(ovchipkaarten);
                    ps.close();
                    return reiziger;
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
                    Reiziger reiziger = new Reiziger(
                            rs.getInt("reiziger_id"),
                            rs.getString("voorletters"),
                            rs.getString("tussenvoegsel"),
                            rs.getString("achternaam"),
                            rs.getDate("geboortedatum")
                    );
                    Adres adres = adresDAO.findByReiziger(reiziger.getId());
                    reiziger.setAdres(adres);

                    List<OVChipkaart> ovchipkaarten = ovChipkaartDAO.findByReiziger(reiziger);
                    reiziger.setOVChipkaart(ovchipkaarten);

                    reizigers.add(reiziger);
                }
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;
    }
    @Override
    public List<Reiziger> findAll() {
        String query = "SELECT * FROM reiziger";
        List<Reiziger> reizigers = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        try  {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");

                Reiziger reiziger = new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum);

                Adres adres = adresDAO.findByReiziger(id);
                reiziger.setAdres(adres);

                List<OVChipkaart> ovchipkaarten = ovChipkaartDAO.findByReiziger(reiziger);
                reiziger.setOVChipkaart(ovchipkaarten);

                reizigers.add(reiziger);

            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;
    }
}
