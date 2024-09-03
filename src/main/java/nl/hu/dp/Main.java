package nl.hu.dp;

import nl.hu.dp.DAO.ReizigerDAO;
import nl.hu.dp.DAO.ReizigerDAOPsql;
import nl.hu.dp.MOD.Reiziger;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{
            Connection myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/ovchipkaart", "postgres", "H0meW0rk");
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(myConn);

//            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(myConn);
//            List<Reiziger> reizigers = reizigerDAOPsql.findAll();
//            System.out.println("Alle reizigers: " + reizigers);


            String gbdatum = "1981-03-14";
            Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
            reizigerDAOPsql.save(sietske);
            List<Reiziger> reizigersId = reizigerDAOPsql.findAll();
            System.out.println(reizigersId);

//            Statement myStmt = myConn.createStatement();
//
//            ResultSet myRs = myStmt.executeQuery("SELECT * FROM reiziger");
//
//            while (myRs.next()){
//                System.out.println(myRs.getString(1) + ", "+ myRs.getString(2) + ", "+ myRs.getString(3) + ", "+ myRs.getString(4) + ", "+ myRs.getString(5));
//            }

        }catch (Exception exp){
            exp.printStackTrace();
        }

    }


    }
