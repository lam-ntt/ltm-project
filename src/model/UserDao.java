/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public static void main(String[] args) {
        System.out.println(getUserByName("lam"));
//        createUser(new User("lam", "123", 0, 0, 0));
    }

    public static  List<User> getUser() {
        List<User> users = new ArrayList<>();
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();

            String query = "SELECT * FROM USER ";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                    String userName = rs.getString("username");
                users.add(new User(userName));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return users ;
    }

    public static User getUserByName(String username) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();

            String query = "SELECT * FROM USER WHERE USERNAME = '" + username + "'";
            ResultSet resultset = statement.executeQuery(query);

            if (!resultset.next()) {
                return null;
            }

            return new User(
                    resultset.getInt("id"),
                    resultset.getString("username"),
                    resultset.getString("password")
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static User getUserInfo(String username) {
        User user = null;

        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();

            String query = "SELECT * FROM user WHERE username =  '" + username + "'";
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                String userName = rs.getString("username");
                int wins = rs.getInt("win");
                int losses = rs.getInt("lose");
                int ties = rs.getInt("tie");
                user = new User(userName, wins, losses, ties); // Giả sử User có constructor tương ứng
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();

            String query = "SELECT username, win, tie, lose FROM user ORDER BY win DESC LIMIT 5";
            ResultSet resultset = statement.executeQuery(query);

            while (resultset.next()) {
                String username = resultset.getString("username");
                int winCount = resultset.getInt("win");
                int tieCount = resultset.getInt("tie");
                int loseCount = resultset.getInt("lose");

                // Thêm người chơi vào danh sách
                users.add(new User(username, winCount, tieCount, loseCount));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return users;
    }

    public static boolean createUser(User user) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();

            String query = "INSERT INTO USER(USERNAME, PASSWORD) "
                    + "VALUES('" + user.getUsername() + "', '" + user.getPassword() + "')";
            int result = statement.executeUpdate(query);

            return (result > 0);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static void updateUser(User user) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();

            String query = "";
            int result = statement.executeUpdate(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
