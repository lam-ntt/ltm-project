/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.net.Socket;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import view.Home;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            while ((line = input.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 3) {
                    output.println("Invalid command format. Use: command;username;password");
                    continue;
                }

                String command = parts[0]; // "register" hoặc "login"
                String username = parts[1];
                String password = parts[2];

                if (command.equalsIgnoreCase("register")) {
                    handleRegister(username, password, output);
                } else if (command.equalsIgnoreCase("login")) {
                    handleLogin(username, password, output);
                } else {
                    output.println("Invalid command.");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + socket.getInetAddress());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRegister(String username, String password, PrintWriter output) {
        try (Connection connection = Server.getConnection();
             PreparedStatement checkUserStmt = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {

            checkUserStmt.setString(1, username);
            ResultSet rs = checkUserStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                output.println("Username already exists.");
            } else {
                try (PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, password); // Có thể thêm mã hóa mật khẩu tại đây
                    insertStmt.executeUpdate();
                    output.println("Registration successful for user: " + username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            output.println("Database error occurred.");
        }
    }

    private void handleLogin(String username, String password, PrintWriter output) {
        try (Connection connection = Server.getConnection();
             PreparedStatement loginStmt = connection.prepareStatement("SELECT password FROM users WHERE username = ?")) {

            loginStmt.setString(1, username);
            ResultSet rs = loginStmt.executeQuery();

            if (rs.next() && rs.getString("password").equals(password)) {
                output.println("Login successful for user: " + username);
                // Hiển thị Home form ở đây
                SwingUtilities.invokeLater(() -> {
                    Home homeForm = new Home();
                    homeForm.setVisible(true);
                });
            } else {
                output.println("Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            output.println("Database error occurred.");
        }
    }
}
