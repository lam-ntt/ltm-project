/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.dao;

import helper.Hash;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.User;

public class UserDao {
    
    public static void main(String[] args) {
        User user = createUser(new User("14354", "123"));
        
        
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
    
    
    public static User createUser(User user) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            user.setPassword(Hash.hash(user.getPassword()));
            
            String query = "INSERT INTO USER(USERNAME, PASSWORD) " + 
                        "VALUES('" + user.getUsername() + "', '" + user.getPassword() + "')";
            
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            
            resultSet.next();
            user = getUser(resultSet.getInt(1));
            return user;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public static boolean updateUser(User user, String state) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query;
            if (state.equals("win")) {
                query = "UPDATE USER SET WIN = " + (user.getWin() + 1) +
                        " WHERE ID = " + user.getId();
            } else if (state.equals("lose")) {
                query = "UPDATE USER SET LOSE = " + (user.getLose()+ 1) +
                        " WHERE ID = " + user.getId();
            } else {
                query = "UPDATE USER SET TIE = " + (user.getTie()+ 1) +
                        " WHERE ID = " + user.getId();
                query = "";
            }
            
            int result = statement.executeUpdate(query);
            return (result > 0);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
}
