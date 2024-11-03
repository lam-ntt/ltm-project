/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PointDao {
    
    public static void main(String[] args) {
        
    }
    
    public static List<Point> getAllPointWithPairId(int id) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM POINT WHERE PAIRID = " + id;
            ResultSet resultset = statement.executeQuery(query);
            
            List<Point> points = new ArrayList<>();
            while(!resultset.next()) {
                points.add(new Point(
                        resultset.getInt("id"),
                        resultset.getInt("minX"),
                        resultset.getInt("minY"),
                        resultset.getInt("maxX"),
                        resultset.getInt("maxY")
                ));
            }
            
            return points;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}
