package main.java.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private static String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    private static String dbUserName = "root";
    private static String dbPassword = "123456";
    private static String dbUrl = "jdbc:mysql://localhost:3306/login?serverTimezone=GMT%2B8&characterEncoding=UTF-8";

    public static Connection getCon()throws Exception{
        Class.forName(jdbcDriver);
        Connection con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        return con;
    }

    public void closeCon(Connection con)throws Exception{
        if(con!=null){
            con.close();
        }
    }

//    public static void main(String[] args){
//        DBUtil dbUtil = new DBUtil();
//        try {
//            dbUtil.getCon();
//            System.out.println("Success!");
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println("Failed!");
//        }
//
//    }

}
