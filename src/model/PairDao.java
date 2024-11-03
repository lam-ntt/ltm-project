/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PairDao {
    
    public static void main(String[] args) {
        System.out.println(getPair(1).getImage1());
        System.out.println(getPair(1).getImage2());
    }
    
    public static Pair getPair(int id) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM PAIR WHERE ID = " + id;
            ResultSet resultset = statement.executeQuery(query);
            
            if(!resultset.next()) {
                return null;
            }
            
            return new Pair(
                    resultset.getInt("id"), 
                    resultset.getString("image1"), 
                    resultset.getString("image2")
            );
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}
