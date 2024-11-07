/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ClickDao {
    
    public static void main(String[] args) {
        
    }
    
    public static boolean createClick(Click click) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "INSERT INTO CLICK(X, Y, USERID, GAMEID) " + 
                        "VALUES(" + click.getX() + ", " + click.getY() + ", " + 
                        click.getUser().getId() + ", " + click.getGame().getId() + ")";
            int result = statement.executeUpdate(query);
            
            return (result > 0);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
}
