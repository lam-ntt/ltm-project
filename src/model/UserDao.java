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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserDao {
    
    public static void main(String[] args) {
//        System.out.println(getUserByName("lam"));
//        createUser(new User("lam", "123", 0, 0, 0));

        List<User> users = getAllUsers();
        
        for(User user: users) {
            System.out.println(user.getUsername() + " " + user.getWin() + " " + user.getTie() + " " + user.getLose());
        }
    }
    
    public static User getUser(int id) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM USER WHERE ID = " + id;
            ResultSet resultset = statement.executeQuery(query);
            
            if(!resultset.next()) {
                return null;
            }
            
            return new User(
                    resultset.getInt("id"), 
                    resultset.getString("username"), 
                    resultset.getString("password"),
                    resultset.getInt("win"),
                    resultset.getInt("tie"),
                    resultset.getInt("lose")
            );
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public static User getUserByName(String username) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM USER WHERE USERNAME = '" + username + "'";
            ResultSet resultset = statement.executeQuery(query);
                        
            if(!resultset.next()) {
                return null;
            }
            
            return new User(
                    resultset.getInt("id"), 
                    resultset.getString("username"), 
                    resultset.getString("password"),
                    resultset.getInt("win"),
                    resultset.getInt("tie"),
                    resultset.getInt("lose")
            );
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public static List<User> getAllUsers() {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM USER";
            ResultSet resultset = statement.executeQuery(query);
            
            List<User> users = new ArrayList();
            while(resultset.next()) {
                users.add(new User(
                    resultset.getInt("id"), 
                    resultset.getString("username"), 
                    resultset.getString("password"),
                    resultset.getInt("win"),
                    resultset.getInt("tie"),
                    resultset.getInt("lose")
                ));
            }
            
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User user1, User user2) {
                    if(user1.getWin() != user2.getWin()) {
                        return user2.getWin() - user1.getWin();
                    }
                    
                    if(user1.getTie() != user2.getTie()) {
                        return user2.getTie() - user1.getTie();
                    }
                    
                    return user1.getLose() - user2.getLose();
                }
                
            });
            
            return users;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    
    

    
    public static boolean createUser(User user) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "INSERT INTO USER(USERNAME, PASSWORD) " + 
                        "VALUES('" + user.getUsername() + "', '" + user.getPassword() + "')";
            int result = statement.executeUpdate(query);
            
            return (result > 0);
        } catch(SQLException ex) {
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
            
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
