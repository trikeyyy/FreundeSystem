package ch.trikeyyy.friendmanager;

import ch.trikeyyy.mysql.MySql;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by trikeyyy on 01.11.2015.
 * Date: 07.11.2015
 * Time: 18:35
 */
public class FriendManager {

    public static HashMap<String, String> anfragen = new HashMap<String, String>();

    @SuppressWarnings("deprecation")
    public static void createFreundschaft(ProxiedPlayer player1, ProxiedPlayer player2) {

        try {

            if(!alreadyFriends(player1.getName(), player2.getName())) {

                PreparedStatement ps1 = MySql.getConnection().prepareStatement("INSERT INTO FreundeSystem (Player,PlayerUUID,Friend,FriendUUID) VALUES (?,?,?,?)");
                ps1.setString(1, player1.getName());
                ps1.setString(2, player1.getUUID().toString());
                ps1.setString(3, player2.getName());
                ps1.setString(4, player2.getUUID().toString());
                ps1.execute();

                PreparedStatement ps2 = MySql.getConnection().prepareStatement("INSERT INTO FreundeSystem (Player,PlayerUUID,Friend,FriendUUID) VALUES (?,?,?,?)");
                ps2.setString(1, player2.getName());
                ps2.setString(2, player2.getUUID().toString());
                ps2.setString(3, player1.getName());
                ps2.setString(4, player1.getUUID().toString());
                ps2.execute();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public static ArrayList<String> getFriends(String spieler) {

        ArrayList<String> list = new ArrayList<String>();

        try {
            PreparedStatement ps = MySql.getConnection().prepareStatement("SELECT * FROM FreundeSystem WHERE Player = ?");
            ps.setString(1, spieler);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                list.add(rs.getString("Friend"));
            }

            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }

    }

    public static void deleteFreundschaft(String player1, String player2) {

        if(alreadyFriends(player1, player2)) {

            try {
                PreparedStatement ps = MySql.getConnection().prepareStatement("DELETE FROM FreundeSystem WHERE Player = ? AND Friend = ?");
                ps.setString(1, player1);
                ps.setString(2, player2);
                ps.execute();

                PreparedStatement ps2 = MySql.getConnection().prepareStatement("DELETE FROM FreundeSystem WHERE Player = ? AND Friend = ?");
                ps2.setString(1, player2);
                ps2.setString(2, player1);
                ps2.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    public static boolean alreadyFriends(String player1, String player2) {

        try {
            PreparedStatement ps1 = MySql.getConnection().prepareStatement("SELECT * FROM FreundeSystem WHERE Friend = ?");
            ps1.setString(1, player2);
            ResultSet rs1 = ps1.executeQuery();
            while(rs1.next()) {

                if(rs1.getString("Player").equalsIgnoreCase(player1)) {

                    return true;

                }

            }

            PreparedStatement ps2 = MySql.getConnection().prepareStatement("SELECT * FROM FreundeSystem WHERE Friend = ?");
            ps2.setString(1, player1);
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()) {

                if(rs2.getString("Player").equalsIgnoreCase(player2)) {

                    return true;

                }

            }

            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @SuppressWarnings("deprecation")
    public static boolean bereitsAngefragtWorden(ProxiedPlayer player1, ProxiedPlayer player2) {

        String id1 = player1.getUUID().toString();
        String id2 = player2.getUUID().toString();

        if(anfragen.get(id2) != null && anfragen.get(id2).equalsIgnoreCase(id1)) {

            return true;

        } else {

            return false;

        }

    }

    @SuppressWarnings("deprecation")
    public static boolean hatBereitsAngefragt(ProxiedPlayer player1, ProxiedPlayer player2) {

        String id1 = player1.getUUID().toString();
        String id2 = player2.getUUID().toString();

        if(anfragen.get(id1) != null && anfragen.get(id1).equalsIgnoreCase(id2)) {

            return true;

        } else {

            return false;

        }

    }

}
