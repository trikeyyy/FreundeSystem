package ch.trikeyyy.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by trikeyyy on 01.11.2015.
 * Date: 07.11.2015
 * Time: 18:39
 */
public class MySql {

    public static String host = "localhost";
    public static String port = "3306";
    public static String database = "server";
    public static String username = "root";
    public static String password = null;
    public static Connection con;

    public static void connect() {

        if(!isConnected()) {

            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                System.out.println("[FreundeSystem] MySQL connected");
            } catch (SQLException e) {
                System.out.println("[FreundeSystem] Could not connect to MySQL");
            }

        }

    }

    public static void disconnect() {

        if(isConnected()) {

            try {
                con.close();
                System.out.println("[FreundeSystem] MySQL disconnected");
            } catch (SQLException e) {
                System.out.println("[FreundeSystem] Could not disconnect from MySQL");
            }

        }

    }

    public static boolean isConnected() {

        return (con == null ? false : true);

    }

    public static Connection getConnection() {

        return con;

    }

    public static void update(String qry) {
        try {
            Statement st = con.createStatement();
            st.executeUpdate(qry);
            st.close();
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
    }

}
