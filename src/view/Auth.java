/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class Auth extends javax.swing.JFrame {

    public Auth() {
        initComponents();
        registerButton.addActionListener(evt -> {
            String username = usernameRegisterTextField.getText().trim();
            String password = passwordRegisterTextField.getText().trim();
            sendRequest("register", username, password); 
        });

        loginButton.addActionListener(evt -> {
            String username = usernameLoginTextField.getText().trim();
            String password = passwordLoginTextField.getText().trim();
            sendRequest("login", username, password); 
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Khởi tạo các thành phần
        usernameRegisterLabel = new JLabel("Username:");
        usernameRegisterTextField = new JTextField(15);
        passwordRegisterLabel = new JLabel("Password:");
        passwordRegisterTextField = new JPasswordField(15);
        registerButton = new JButton("Register");

        usernameLoginLabel = new JLabel("Username:");
        usernameLoginTextField = new JTextField(15);
        passwordLoginLabel = new JLabel("Password:");
        passwordLoginTextField = new JPasswordField(15);
        loginButton = new JButton("Login");

        // Tạo layout cho các panel
        registerPanel = createFormPanel("REGISTER", usernameRegisterLabel, usernameRegisterTextField, 
                                        passwordRegisterLabel, passwordRegisterTextField, registerButton);

        loginPanel = createFormPanel("LOGIN", usernameLoginLabel, usernameLoginTextField, 
                                        passwordLoginLabel, passwordLoginTextField, loginButton);

        // Sử dụng CardLayout để chuyển đổi giữa đăng ký và đăng nhập
        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(registerPanel, "register");
        cardPanel.add(loginPanel, "login");

        // Thêm sự kiện chuyển giữa các panel
        JButton switchToLogin = new JButton("Already have an account? Login");
        switchToLogin.addActionListener(e -> showPanel("login"));

        JButton switchToRegister = new JButton("Need an account? Register");
        switchToRegister.addActionListener(e -> showPanel("register"));

        registerPanel.add(switchToLogin, getConstraints(0, 3));
        loginPanel.add(switchToRegister, getConstraints(0, 3));

        // Thiết lập frame chính
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);  // Căn giữa cửa sổ
        setVisible(true);
    }

    private JPanel createFormPanel(String title, JLabel userLabel, JTextField userField,
                                   JLabel passLabel, JPasswordField passField, JButton button) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Khoảng cách giữa các thành phần

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel(title, JLabel.CENTER), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(button, gbc);

        return panel;
    }

    private GridBagConstraints getConstraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, name);
    }

    private void sendRequest(String command, String username, String password) {
        try (Socket socket = new Socket("localhost", 1234);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            output.println(command + ";" + username + ";" + password);
            String response = input.readLine();
            JOptionPane.showMessageDialog(this, response);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Connection error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new Auth().setVisible(true));
    }

    // Khai báo các thành phần giao diện
    private JPanel cardPanel;
    private JPanel registerPanel, loginPanel;
    private JLabel usernameRegisterLabel, passwordRegisterLabel;
    private JTextField usernameRegisterTextField;
    private JPasswordField passwordRegisterTextField;
    private JButton registerButton;

    private JLabel usernameLoginLabel, passwordLoginLabel;
    private JTextField usernameLoginTextField;
    private JPasswordField passwordLoginTextField;
    private JButton loginButton;
}
