package nl.hu.dp.DAO;

import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    private Connection conn;
    private ReizigerDAO reizigerDAO;


    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }
    public void setReizigerDAO(ReizigerDAO reizigerDAO) {
        this.reizigerDAO = reizigerDAO;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)"
        ) ;
        ps.setInt(1, ovChipkaart.getKaart_nummer());
        ps.setDate(2, ovChipkaart.getGeldig_tot());
        ps.setInt(3, ovChipkaart.getKlasse());
        ps.setDouble(4, ovChipkaart.getSaldo());
        ps.setInt(5, ovChipkaart.getReiziger().getId());
        ps.executeUpdate();
        ps.close();
        return true;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?"
        );
        ps.setDate(1, ovChipkaart.getGeldig_tot());
        ps.setInt(2, ovChipkaart.getKlasse());
        ps.setDouble(3, ovChipkaart.getSaldo());
        ps.setInt(4, ovChipkaart.getReiziger().getId());
        ps.setInt(5, ovChipkaart.getKaart_nummer());
        ps.executeUpdate();
        ps.close();
        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?"
        );
        ps.setInt(1, ovChipkaart.getKaart_nummer());
        ps.executeUpdate();
        ps.close();
        return true;
    }

    @Override
    public List<OVChipkaart> getAll() throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM ov_chipkaart");
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        while (rs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart();
            ovChipkaart.setKaart_nummer(rs.getInt("kaart_nummer"));
            ovChipkaart.setGeldig_tot(rs.getDate("geldig_tot"));
            ovChipkaart.setKlasse(rs.getInt("klasse"));
            ovChipkaart.setSaldo(rs.getDouble("saldo"));
            ovChipkaarten.add(ovChipkaart);
        }
        rs.close();
        st.close();
        return ovChipkaarten;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String query = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaart_nummer(rs.getInt("kaart_nummer"));
                ovChipkaart.setGeldig_tot(rs.getDate("geldig_tot"));
                ovChipkaart.setKlasse(rs.getInt("klasse"));
                ovChipkaart.setSaldo(rs.getDouble("saldo"));

                ovChipkaart.setReiziger(reiziger);

                ovChipkaarten.add(ovChipkaart);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ovChipkaarten;
    }

    @Override
    public OVChipkaart findByKaartNummer(int kaartNummer) throws SQLException {
        String query = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        OVChipkaart ovChipkaart = null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, kaartNummer);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaart_nummer(rs.getInt("kaart_nummer"));
                ovChipkaart.setGeldig_tot(rs.getDate("geldig_tot"));
                ovChipkaart.setKlasse(rs.getInt("klasse"));
                ovChipkaart.setSaldo(rs.getDouble("saldo"));

                int reizigerId = rs.getInt("reiziger_id");
                Reiziger reiziger = reizigerDAO.findBy(reizigerId);
                ovChipkaart.setReiziger(reiziger);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ovChipkaart;
    }
}
