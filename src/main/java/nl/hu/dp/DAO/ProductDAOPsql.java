package nl.hu.dp.DAO;

import nl.hu.dp.MOD.OVChipkaart;
import nl.hu.dp.MOD.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;
    OVChipkaartDAO ovChipkaartDAO;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }
    public void setOvChipkaartDAO(OVChipkaartDAO ovChipkaartDAO) {
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        String query = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, product.getProduct_nummer());
        ps.setString(2, product.getNaam());
        ps.setString(3, product.getBeschrijving());
        ps.setDouble(4, product.getPrijs());
        ps.executeUpdate();
        for (Integer kaartnummer : product.getOvchipkaarten()) {
            ps = this.conn.prepareStatement(
                    "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)"
            );
            ps.setInt(1, kaartnummer);
            ps.setInt(2, product.getProduct_nummer());
            ps.execute();

            OVChipkaart ovChipkaart = ovChipkaartDAO.findByKaartNummer(kaartnummer);
            if (ovChipkaart != null) {
                ovChipkaart.addProduct(product);
            }
        }
        ps.close();
        return true;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        List<Integer> kaartNummers = product.getOvchipkaarten();
        for (Integer kaartNummer : kaartNummers) {
            OVChipkaart ovChipkaart = ovChipkaartDAO.findByKaartNummer(kaartNummer);
            if (ovChipkaart != null) {
                ovChipkaart.removeProduct(product);
            }
        }

        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?"
        );
        ps.setInt(1, product.getProduct_nummer());
        ps.executeUpdate();

        ps = conn.prepareStatement("DELETE FROM product WHERE product_nummer = ?");
        ps.setInt(1, product.getProduct_nummer());
        ps.executeUpdate();
        ps.close();
        return true;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?"
        );
        ps.setString(1, product.getNaam());
        ps.setString(2, product.getBeschrijving());
        ps.setDouble(3, product.getPrijs());
        ps.setInt(4, product.getProduct_nummer());
        ps.executeUpdate();

        ps = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
        ps.setInt(1, product.getProduct_nummer());
        ps.executeUpdate();

        for (Integer kaartNummer : product.getOvchipkaarten()) {
            PreparedStatement psLink = conn.prepareStatement(
                    "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)"
            );
            psLink.setInt(1,kaartNummer);
            psLink.setInt(2, product.getProduct_nummer());
            psLink.executeUpdate();
            psLink.close();
        }

        return true;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.* , o.kaart_nummer FROM product p " +
                "JOIN ov_chipkaart_product o ON p.product_nummer = o.product_nummer " +
                "WHERE o.kaart_nummer = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProduct_nummer(rs.getInt("product_nummer"));
                product.setNaam(rs.getString("naam"));
                product.setBeschrijving(rs.getString("beschrijving"));
                product.setPrijs(rs.getDouble("prijs"));

                product.addOVChipkaart(ovChipkaart.getKaart_nummer());

                products.add(product);
            }
        }
        return products;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, ocp.kaart_nummer FROM product p " +
                "LEFT JOIN ov_chipkaart_product ocp ON p.product_nummer = ocp.product_nummer " +
                "ORDER BY p.product_nummer";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            Map<Integer, Product> productMap = new HashMap<>();
            while (rs.next()) {
                int productNummer = rs.getInt("product_nummer");
                Product product = productMap.get(productNummer);
                if (product == null) {
                    product = new Product();
                    product.setProduct_nummer(productNummer);
                    product.setNaam(rs.getString("naam"));
                    product.setBeschrijving(rs.getString("beschrijving"));
                    product.setPrijs(rs.getDouble("prijs"));
                    productMap.put(productNummer, product);
                    products.add(product);
                }
                int kaartNummer = rs.getInt("kaart_nummer");
                if (!rs.wasNull()) {
                    OVChipkaart ovChipkaart = ovChipkaartDAO.findByKaartNummer(kaartNummer);
                    if (ovChipkaart != null) {
                        product.addOVChipkaart(ovChipkaart.getKaart_nummer());
                    }
                }
            }
        }

        return products;
    }




}



