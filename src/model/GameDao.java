/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameDao {
    
    public static void main(String[] args) {
    }
    
    public static int createGameWithPairId(int userId1, int userId2, int id) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "INSERT INTO GAME(USERID1, USERID2, PAIRID) "
                    + "VALUES(" + userId1 + ", " + userId2 + ", " + id + ")";
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            
            resultSet.next();
            return  resultSet.getInt(1);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return -1;
    }
    
    public static Game getGame(int id) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM GAME WHERE ID = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            
            resultSet.next();
            Game game = new Game(
                    resultSet.getInt("id"),
                    UserDao.getUser(resultSet.getInt("userId1")),
                    UserDao.getUser(resultSet.getInt("userId2")),
                    PairDao.getPair(resultSet.getInt("pairId")),
                    resultSet.getInt("score1"),
                    resultSet.getInt("score2"),
                    resultSet.getInt("state")
            );
            
            return game;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public static boolean updateGame(Game game) {
        try {
            Connection con = Connectionn.getConnection();
            Statement statement = con.createStatement();
            
            String query = "UPDATE GAME SET SCORE1 = " + game.getScore1() + 
                    ", SCORE2 = " + game.getScore2() + 
                    ", STATE = '" + game.getState() + "' " + 
                    "WHERE ID = " + game.getId();
            
            int result = statement.executeUpdate(query);
            return (result > 0);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
}
