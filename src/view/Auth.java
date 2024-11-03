/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.Client;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import model.User;
import model.UserDao;


public class Auth extends javax.swing.JFrame {
    Client client;

    public Auth() {
        initComponents();
        setLocationRelativeTo(null);
        
        
        client = null;
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(client != null) {
                    client.closeEverything();
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginPanel = new javax.swing.JPanel();
        registerButton2 = new javax.swing.JButton();
        loginLabel = new javax.swing.JLabel();
        usernameLoginLabel = new javax.swing.JLabel();
        usernameLoginTextField = new javax.swing.JTextField();
        passwordLoginTextField = new javax.swing.JTextField();
        passwordLoginLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        registerPanel = new javax.swing.JPanel();
        usernameRegisterLabel = new javax.swing.JLabel();
        usernameRegisterTextField = new javax.swing.JTextField();
        passwordRegisterLabel = new javax.swing.JLabel();
        passwordRegisterTextField = new javax.swing.JTextField();
        registerButton = new javax.swing.JButton();
        registerLabel = new javax.swing.JLabel();
        loginButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        registerButton2.setText("Register");
        registerButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButton2ActionPerformed(evt);
            }
        });

        loginLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        loginLabel.setText("LOGIN TO JOIN WITH US");

        usernameLoginLabel.setText("Username");

        passwordLoginLabel.setText("Password");

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(0, 77, Short.MAX_VALUE)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addComponent(registerButton2)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(usernameLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordLoginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernameLoginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(loginButton))
                        .addGap(70, 70, 70))))
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(loginLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(registerButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(loginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLoginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameLoginLabel))
                .addGap(19, 19, 19)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLoginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordLoginLabel))
                .addGap(18, 18, 18)
                .addComponent(loginButton)
                .addGap(75, 75, 75))
        );

        getContentPane().add(loginPanel, "card3");

        usernameRegisterLabel.setText("Username");

        passwordRegisterLabel.setText("Password");

        registerButton.setText("Register");
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        registerLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        registerLabel.setText("REGISTER A NEW ACCOUNT");

        loginButton2.setText("Login");
        loginButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout registerPanelLayout = new javax.swing.GroupLayout(registerPanel);
        registerPanel.setLayout(registerPanelLayout);
        registerPanelLayout.setHorizontalGroup(
            registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registerPanelLayout.createSequentialGroup()
                .addGap(0, 82, Short.MAX_VALUE)
                .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, registerPanelLayout.createSequentialGroup()
                        .addComponent(loginButton2)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, registerPanelLayout.createSequentialGroup()
                        .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(usernameRegisterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordRegisterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordRegisterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernameRegisterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(registerButton))
                        .addGap(65, 65, 65))))
            .addGroup(registerPanelLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(registerLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        registerPanelLayout.setVerticalGroup(
            registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loginButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(registerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameRegisterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameRegisterLabel))
                .addGap(19, 19, 19)
                .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordRegisterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordRegisterLabel))
                .addGap(18, 18, 18)
                .addComponent(registerButton)
                .addGap(75, 75, 75))
        );

        getContentPane().add(registerPanel, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        String username = usernameRegisterTextField.getText();
        String password = passwordRegisterTextField.getText();
        
        if(username.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(
                null, 
                "You must enter username and password!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        } else {
            if(UserDao.getUserByName(username) != null) {
                JOptionPane.showMessageDialog(
                    null, 
                    "This username was selected. Please choose another! ", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            } else {
                User user = new User(username, password);
                boolean isUserCreated = UserDao.createUser(user);
                if(isUserCreated) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Your account created successfully. Now, you able to login! ", 
                        "Message", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    usernameRegisterTextField.setText("");
                    passwordRegisterTextField.setText("");
                    
                    CardLayout cartLayout = (CardLayout) this.getContentPane().getLayout();
                    cartLayout.next(this.getContentPane());
                } else {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Fail to create your account. Please try again! ", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }//GEN-LAST:event_registerButtonActionPerformed

    private void loginButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButton2ActionPerformed
        CardLayout cartLayout = (CardLayout) this.getContentPane().getLayout();
        cartLayout.next(this.getContentPane());
    }//GEN-LAST:event_loginButton2ActionPerformed

    private void registerButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButton2ActionPerformed
        CardLayout cartLayout = (CardLayout) this.getContentPane().getLayout();
        cartLayout.next(this.getContentPane());
    }//GEN-LAST:event_registerButton2ActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        String username = usernameLoginTextField.getText();
        String password = passwordLoginTextField.getText();
        
        if(username.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(
                null, 
                "You must enter username and password!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        } else {
            User user = UserDao.getUserByName(username);
            if(user != null) {
                if(user.getPassword().equals(password)) {
                    usernameLoginTextField.setText("");
                    passwordLoginTextField.setText("");
                    
                    try {
                        client = new Client(new Socket("localhost", 1234), user);
                        this.dispose();
                        new Home(client).setVisible(true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        null, 
                        "This password is wrong! ", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    null, 
                    "This username is not existed! ", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Auth().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton loginButton;
    private javax.swing.JButton loginButton2;
    private javax.swing.JLabel loginLabel;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel passwordLoginLabel;
    private javax.swing.JTextField passwordLoginTextField;
    private javax.swing.JLabel passwordRegisterLabel;
    private javax.swing.JTextField passwordRegisterTextField;
    private javax.swing.JButton registerButton;
    private javax.swing.JButton registerButton2;
    private javax.swing.JLabel registerLabel;
    private javax.swing.JPanel registerPanel;
    private javax.swing.JLabel usernameLoginLabel;
    private javax.swing.JTextField usernameLoginTextField;
    private javax.swing.JLabel usernameRegisterLabel;
    private javax.swing.JTextField usernameRegisterTextField;
    // End of variables declaration//GEN-END:variables
}
