/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package client.view;

import client.Client;
import helper.Message;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.User;

public class Home extends javax.swing.JFrame {
    private Client client;
    private User user;
    private Thread thread;
    private volatile boolean running = true;
    private DefaultTableModel defaultTableModel;

    public Home(Client client) {
        initComponents();
        setLocationRelativeTo(null);
        
        this.client = client;
        user = null;
        
        thread = initThread();
        thread.start();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
                requestRemoveUser();
                client.closeEverything();
            }
        });
        
        initLabel();
        initTable();
        
        requestGetUserInfo();
        requestGetAllUserInfo();
        requestGetAllOnlineUserInfo();
    }
    
    public Thread initThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(running) {
                        if(!client.getSocket().isConnected()) break;
                        
                        try {
                            Message message = (Message) client.getObjectInputStream().readObject();
                            switch (message.getCode()) {
                                case "01" -> handleGetUserInfoResponse(message);
                                case "1" -> handleGetAllUserInfoResponse(message);
                                case "2" -> handleGetAllOnlineUserInfoResponse(message);
                                case "3" -> handleAddUserToOnlineUserRequest(message);
                                case "4" -> handleRemoveUserFromOnlineUserRequest(message);
                                case "6" -> handleReceiveInvitationRequest(message);
                                case "9" -> handleReceiveAgreementResponse(message);
                                case "10" -> handleReceiveRejectionResponse(message);
                                case "11" -> handleReceiveBusyResponse(message);
                                default -> {
                                }
                            }
                        } catch (Exception e) {
                            break;
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void initLabel() {
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leaderBoardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        onlineUsersLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void initTable() {
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("USER");
        defaultTableModel.addColumn("WIN");
        defaultTableModel.addColumn("TIE");
        defaultTableModel.addColumn("LOSE");
    }
    
    private void updateInfo() {
        usernameLabel.setText("Username: " + this.user.getUsername());
        winLabel.setText("Win: " + this.user.getWin());
        loseLabel.setText("Lose: " + this.user.getLose());
        tieLabel.setText("Tie: " + this.user.getTie());
    }
    
    private void updateTable(List<User> users) {
        leaderBoardTable.setModel(defaultTableModel);
        for(User user: users) {
            defaultTableModel.addRow(new Object[] {
                user.getUsername(),
                user.getWin(),
                user.getTie(),
                user.getLose()
            });
        }
    }
    
    private void updateList(List<User> users) {
        jPanel5.setLayout(new FlowLayout());
        for(User user: users) {
            if(user.getUsername().equals(this.user.getUsername())) continue;
            jPanel5.add(createLabel(user));
        }
        jPanel5.revalidate();
    }
    
    
    private JLabel createLabel(User user) {
        JLabel label = new JLabel(user.getUsername(), SwingConstants.CENTER);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    requestInviteUser(user);
                }
            }
        });
        return label;
    }
    
    private JLabel findLabel(User user, JPanel panel) {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            if (panel.getComponent(i) instanceof JLabel) {
                JLabel label = (JLabel) panel.getComponent(i);
                if (label.getText().equals(user.getUsername())) {
                    return label;
                }
            }
        }
        return null;
    }
    
    
    private void requestGetUserInfo() {
        client.sendMessage(new Message("01"));
    }
    
    private void requestGetAllUserInfo() {
        client.sendMessage(new Message("1"));
    }
    
    private void requestGetAllOnlineUserInfo() {
        client.sendMessage(new Message("2"));
    }
    
    private void handleGetUserInfoResponse(Message message) {
        User user = (User) message.getObject();
        this.user = user;
        
        updateInfo();
    }
    
    private void handleGetAllUserInfoResponse(Message message) {
        List<User> users = (List<User>) message.getObject();
        updateTable(users);
    }
    
    private void handleGetAllOnlineUserInfoResponse(Message message) {
        List<User> users = (List<User>) message.getObject();
        updateList(users);
    }
    
    
    private void requestRemoveUser() {
        client.sendMessage(new Message("4"));
    }
    
    private void handleRemoveUserFromOnlineUserRequest(Message message) {
        User user = (User) message.getObject();
        SwingUtilities.invokeLater(() -> {
            jPanel5.remove(findLabel(user, jPanel5));
            jPanel5.revalidate();
        });
}
    
    private void handleAddUserToOnlineUserRequest(Message message) {
        User user = (User) message.getObject();
        SwingUtilities.invokeLater(() -> {
            jPanel5.add(createLabel(user));
            jPanel5.revalidate();
        });
    }
    
    
    private void requestInviteUser(User receiver) {
        client.sendMessage(new Message("5", receiver));
        JOptionPane.showMessageDialog(
            mainPanel, 
            "Sending your invitation to " + receiver.getUsername(), 
            "Notification", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void sendAgreeResponse(User receiver) {
        client.sendMessage(new Message("7", receiver));
    }
    
    private void sendRejectResponse(User receiver) {
        client.sendMessage(new Message("8", receiver));
    }
        
    private void handleReceiveInvitationRequest(Message message) {
        User sender = (User) message.getObject();
        int response = JOptionPane.showConfirmDialog(
            mainPanel, 
            sender.getUsername() + " invites you to join a game!", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION
        );

        if(response == JOptionPane.YES_OPTION) {
            sendAgreeResponse(sender);
            
            running = false;
            this.dispose();
            new Main(client, sender).setVisible(true);
        } else {
            sendRejectResponse(sender);
        }
    }
    
    private void handleReceiveAgreementResponse(Message message) {
        User sender = (User) message.getObject();
        JOptionPane.showMessageDialog(
            mainPanel, 
            sender.getUsername() + " agrees join your game!", 
            "Confirmation", 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        running = false;
        this.dispose();
        new Main(client, sender).setVisible(true);
    } 
    
    private void handleReceiveRejectionResponse(Message message) {
        User sender = (User) message.getObject();
        JOptionPane.showMessageDialog(
            mainPanel, 
            sender.getUsername() + " rejects your invitation!", 
            "Confirmation", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void handleReceiveBusyResponse(Message message) {
        User sender = (User) message.getObject();
        JOptionPane.showMessageDialog(
            mainPanel, 
            sender.getUsername() + " is on another game!", 
            "Confirmation", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        leaderBoardLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        leaderBoardTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        onlineUsersLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        winLabel = new javax.swing.JLabel();
        loseLabel = new javax.swing.JLabel();
        tieLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        titleLabel.setText("Spot the Difference");

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(logoutButton)
                .addContainerGap())
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoutButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        leaderBoardLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        leaderBoardLabel.setText("Leader Board");

        leaderBoardTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "User", "Win", "Tie", "Lose"
            }
        ));
        jScrollPane1.setViewportView(leaderBoardTable);

        onlineUsersLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        onlineUsersLabel.setText("Online Users (double-click on the username to invite them to the game)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 83, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(onlineUsersLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(onlineUsersLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(134, 134, 134))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(leaderBoardLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(leaderBoardLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        usernameLabel.setText("Username: A");

        winLabel.setText("Win: 0");

        loseLabel.setText("Lose: 0");

        tieLabel.setText("Tie: 0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(loseLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(winLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tieLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usernameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(winLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loseLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tieLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        requestRemoveUser();
        client.closeEverything();
        
        this.dispose();
        new Auth().setVisible(true);
    }//GEN-LAST:event_logoutButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel leaderBoardLabel;
    private javax.swing.JTable leaderBoardTable;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel loseLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel onlineUsersLabel;
    private javax.swing.JLabel tieLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel winLabel;
    // End of variables declaration//GEN-END:variables
}
