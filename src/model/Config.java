/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author nguye
 */
public class Config {
    public static String DB_URL = "jdbc:mysql://localhost:3306/ltm";
    public static String DB_USER = "root";
    public static String DB_PASSWORD = "mysql";
    public static Connection getConnection() {
        try {
            // Tải driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Kết nối tới database
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Kết nối thành công tới MySQL.");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy driver MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối database thất bại.");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // Kiểm tra kết nối
        getConnection();
    }
}
