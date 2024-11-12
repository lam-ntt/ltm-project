/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectionn {
    
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    Config.DB_URL,
                    Config.DB_USER,
                    Config.DB_PASSWORD
            );
            return conn;
            
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}
