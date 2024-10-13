/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author nguye
 */
public class UserDao {
    
    public static void main(String[] args) {
        
    }
    
    public static User getUser(int id) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "";
            ResultSet resultset = statement.executeQuery(query);
            
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public static List<User> getAllUsers() {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "";
            ResultSet resultset = statement.executeQuery(query);
            
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    
    public static void createUser(User user) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "";
            int result = statement.executeUpdate(query);
            
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
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
