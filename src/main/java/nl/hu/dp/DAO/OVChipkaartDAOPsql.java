package nl.hu.dp.DAO;

import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Product;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    private Connection conn;
    private ReizigerDAO reizigerDAO;
    private ProductDAO productDAO;


    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }
    public void setReizigerDAO(ReizigerDAO reizigerDAO) {
        this.reizigerDAO = reizigerDAO;
    }
    public void setProductDAO(ProductDAO productDAO) {}

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
        if (this.productDAO != null) {
            for (Product product : ovChipkaart.getProducten()){
                this.productDAO.save(product);
            }
        }
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
        if (this.productDAO != null) {
            for (Product product : ovChipkaart.getProducten()){
                this.productDAO.update(product);
            }
        }
        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement deleteAssociationsPs = conn.prepareStatement(
                "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?"
        );
        deleteAssociationsPs.setInt(1, ovChipkaart.getKaart_nummer());
        deleteAssociationsPs.executeUpdate();
        deleteAssociationsPs.close();

        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?"
        );
        ps.setInt(1, ovChipkaart.getKaart_nummer());
        ps.executeUpdate();
        ps.close();

        return true;
    }

    private List<Product> findProductsByOVChipkaart(int kaartNummer) throws SQLException {
        List<Product> producten = new ArrayList<>();

        String query = "SELECT p.* " +
                "FROM product p " +
                "JOIN ov_chipkaart_product ocp ON p.product_nummer = ocp.product_nummer " +
                "WHERE ocp.kaart_nummer = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, kaartNummer);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProduct_nummer(rs.getInt("product_nummer"));
                product.setNaam(rs.getString("naam"));
                product.setBeschrijving(rs.getString("beschrijving"));
                product.setPrijs(rs.getDouble("prijs"));

                producten.add(product);
            }

            rs.close();
        }

        return producten;
    }

    @Override
    public List<OVChipkaart> findAll() {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        String query = "SELECT * FROM ov_chipkaart";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaart_nummer(rs.getInt("kaart_nummer"));
                ovChipkaart.setGeldig_tot(rs.getDate("geldig_tot"));
                ovChipkaart.setKlasse(rs.getInt("klasse"));
                ovChipkaart.setSaldo(rs.getInt("saldo"));

                Reiziger reiziger = reizigerDAO.findBy(rs.getInt("reiziger_id"));
                ovChipkaart.setReiziger(reiziger);

                List<Product> producten = findProductsByOVChipkaart(ovChipkaart.getKaart_nummer());
                ovChipkaart.setProducten(producten);
                for (Product product : producten) {
                    ovChipkaart.addProduct(product);
                    product.addOVChipkaart(ovChipkaart.getKaart_nummer());
                }
                ovChipkaarten.add(ovChipkaart);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ovChipkaarten;
    }




    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String query = "SELECT oc.*, opc.product_nummer " +
                "FROM ov_chipkaart oc " +
                "LEFT JOIN ov_chipkaart_product opc ON oc.kaart_nummer = opc.kaart_nummer " +
                "WHERE oc.reiziger_id = ?";
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        Set<Integer> kaartNummerSet = new HashSet<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reiziger.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int kaartNummer = rs.getInt("kaart_nummer");

                if (!kaartNummerSet.contains(kaartNummer)) {
                    OVChipkaart ovChipkaart = new OVChipkaart();
                    ovChipkaart.setKaart_nummer(kaartNummer);
                    ovChipkaart.setGeldig_tot(rs.getDate("geldig_tot"));
                    ovChipkaart.setKlasse(rs.getInt("klasse"));
                    ovChipkaart.setSaldo(rs.getDouble("saldo"));
                    ovChipkaart.setReiziger(reiziger);

                    List<Product> producten = findProductsByOVChipkaart(kaartNummer);
                    ovChipkaart.setProducten(producten);

                    for (Product product : producten) {
                        ovChipkaart.addProduct(product);
                        product.addOVChipkaart(kaartNummer);
                    }

                    ovChipkaarten.add(ovChipkaart);
                    kaartNummerSet.add(kaartNummer);
                }
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ovChipkaarten;
    }


    @Override
    public OVChipkaart findByKaartNummer(int kaartNummer) throws SQLException {
        String query = "SELECT oc.*, r.reiziger_id, r.voorletters, r.tussenvoegsel, r.achternaam, r.geboortedatum, " +
                "op.product_nummer " +
                "FROM ov_chipkaart oc " +
                "JOIN reiziger r ON oc.reiziger_id = r.reiziger_id " +
                "LEFT JOIN ov_chipkaart_product op ON oc.kaart_nummer = op.kaart_nummer " +
                "WHERE oc.kaart_nummer = ?";
        OVChipkaart ovChipkaart = null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, kaartNummer);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Reiziger reiziger = new Reiziger();
                reiziger.setId(rs.getInt("reiziger_id"));
                reiziger.setVoorletters(rs.getString("voorletters"));
                reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
                reiziger.setAchternaam(rs.getString("achternaam"));
                reiziger.setDatum(rs.getDate("geboortedatum"));

                ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaart_nummer(rs.getInt("kaart_nummer"));
                ovChipkaart.setGeldig_tot(rs.getDate("geldig_tot"));
                ovChipkaart.setKlasse(rs.getInt("klasse"));
                ovChipkaart.setSaldo(rs.getDouble("saldo"));
                ovChipkaart.setReiziger(reiziger);

                List<Product> producten = findProductsByOVChipkaart(kaartNummer);
                ovChipkaart.setProducten(producten);

                for (Product product : producten) {
                    ovChipkaart.addProduct(product);
                    product.addOVChipkaart(kaartNummer);
                }
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ovChipkaart;
    }


}
