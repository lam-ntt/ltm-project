/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package client.view;

import client.Client;
import helper.Message;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import javax.swing.table.DefaultTableModel;
import model.Click;
import model.Game;
import model.User;


public class Main extends javax.swing.JFrame {
    private Client client;
    private User user, parner;
    private Thread thread;
    private volatile boolean running = true;
    private DefaultTableModel defaultTableModel;
    private int readyCount;
    private Game game;

    public Main(Client client, User parner) {
        initComponents();
        
        this.client = client;
        this.user = client.getUser();
        this.parner = parner;
        
        this.thread = initThread();
        thread.start();
        
        initLabel();
        initTable();
        
        setUpAtBeginning();
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
                                case "12" -> handleReceiveReadyState(message);
                                case "13" -> handleReceiveGame(message);
                                case "15" -> handleReceiveRightClick(message);
                                case "161" -> handleReceiveFailClick(message, 1);
                                case "162" -> handleReceiveFailClick(message, 2);
                                case "17" -> handleReceiveContinueRequest(message);
                                case "18" -> handleReceiveStopRequest();
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
        clockLabel.setHorizontalAlignment(JLabel.CENTER);
        notiLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        ruleDescTextArea.setText(Const.RULE);
        ruleDescTextArea.setOpaque(false);
        ruleDescTextArea.setLineWrap(true);
        ruleDescTextArea.setWrapStyleWord(true);
    }
    
    private void initTable() {
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn(user.getUsername());
        defaultTableModel.addColumn(parner.getUsername());
        
        scoreTable.setModel(defaultTableModel);
        defaultTableModel.addRow(new Object[] {0, 0});
    }
    
    private void updateTable() {
        defaultTableModel.removeRow(0);
        if(game.getUser1().getUsername().equals(user.getUsername())) {
            defaultTableModel.addRow(new Object[] {
                game.getScore1(), 
                game.getScore2()
            });
        } else {
            defaultTableModel.addRow(new Object[] {
                game.getScore2(), 
                game.getScore1()
            });
        }
    }
    
    private void setUpAtBeginning () {
        clockLabel.setText("");
        notiLabel.setText("");
        messageLabel.setText("");
        
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        scoreTable.setVisible(false);
        
        readyCount = 0;
        game = null;
    }
    
    private void setUpInGame (Game game) {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        scoreTable.setVisible(true);

        image1Label.setIcon(convertImage(Const.BASE_PATH + game.getPair().getImage1()));
        image1Label.setHorizontalAlignment(JLabel.CENTER);
        image1Label.setVerticalAlignment(JLabel.CENTER);

        image2Label.setIcon(convertImage(Const.BASE_PATH + game.getPair().getImage2()));
        image2Label.setHorizontalAlignment(JLabel.CENTER);
        image2Label.setVerticalAlignment(JLabel.CENTER);

        openNextCard();
    }
    
    private void resetGame() {
        initTable();
        setUpAtBeginning();
        openNextCard();
    }
    
    
    private void sendReadyState() {
        client.sendMessage(new Message("12", parner));
    }
    
    private void handleReceiveReadyState(Message message) {
        readyCount += 1;
        if(readyCount == 1) {
            User user = (User) message.getObject();
            notiLabel.setText(user.getUsername() + " is ready!");
        }
    }
    
    private void handleReceiveGame(Message message) {
        game = (Game) message.getObject();
        setUpInGame(game);
    }
    
    
    private void sendClick(Click click) {
        client.sendMessage(new Message("14", click));
    }
    
    private void handleReceiveRightClick(Message message) {
        Click click = (Click) message.getObject();
        String mes;
        if(click.getUser().getUsername().equals(user.getUsername())) {
            mes = "Correct point!";
            if(game.getUser1().getUsername().equals(user.getUsername())) {
                game.setScore1(game.getScore1() + 1);
            } else {
                game.setScore2(game.getScore2() + 1);
            }
        } else {
            mes = click.getUser().getUsername() + " found one correct point!";
            if(game.getUser1().getUsername().equals(parner.getUsername())) {
                game.setScore1(game.getScore1() + 1);
            } else {
                game.setScore2(game.getScore2() + 1);
            }
        }
        messageLabel.setText(mes);
        
        updateTable();
        markPos(image1Label, "O", click.getX(), click.getY());
        markPos(image2Label, "O", click.getX(), click.getY());

        if(game.getScore1() + game.getScore2() == 5) endGame();
    }
    
    private void handleReceiveFailClick(Message message, int error) {
        Click click = (Click) message.getObject();
        String mes;
        if(click.getUser().getUsername().equals(user.getUsername())) {
            mes = click.getUser().getUsername();
            if(error == 1) mes += " clicked in incorrect point!";
            else mes += " clicked in checked point!";
            
        } else {
            if(error == 1) mes = "Incorrect point!";
            else mes = "Checked point!";
        }
        messageLabel.setText(mes);
    }
    
    
    private void endGame() {
        client.sendMessage(new Message("19", game));
        String msg;
        if(game.getUser1().getUsername().equals(user.getUsername())) {
            if(game.getScore1() > game.getScore2()) {
                msg = "Congratulations! You win this game with a score ";
            } else {
                msg = "You lose this game with a score ";
            }
            msg += game.getScore1() + "-" + game.getScore2();
        } else {
            if(game.getScore1() < game.getScore2()) {
                msg = "Congratulations! You win this game with a score ";
            } else {
                msg = "You lose this game with a score ";
            }
            msg += game.getScore2() + "-" + game.getScore1();
        }
        msg += ". You want to continue this game?";
        
        int response = JOptionPane.showConfirmDialog(
                this, 
                msg, 
                "Notification", 
                JOptionPane.YES_NO_OPTION
        );
        
        if(response == JOptionPane.YES_OPTION) {
            sendContinueState();
        } else {
            sendNotContinueState();
            running = false;
            this.dispose();
            new Home(client).setVisible(true);
        }
    }
    
    
    private void sendContinueState() {
        client.sendMessage(new Message("17", "Yes"));
    }
    
    private void sendNotContinueState() {
        client.sendMessage(new Message("17", "No"));
    }
    
    private void handleReceiveContinueRequest(Message message) {
        if(((String) message.getObject()).equals("Yes")) {
            resetGame();
        } else {
            JOptionPane.showMessageDialog(
                    this, 
                    parner.getUsername() + " reject continuing this game!",
                    "Notification",
                    JOptionPane.INFORMATION_MESSAGE
            );
            
            running = false;
            client.closeEverything();
            this.dispose();
            try {
                new Home(new Client(new Socket(Const.HOST, Const.PORT), user)).setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    private void sendStopRequest() {
        client.sendMessage(new Message("18"));
    }
    
    private void handleReceiveStopRequest() {
        JOptionPane.showMessageDialog(
                this, 
                parner.getUsername() + " want to stop this game!", 
                "Notification", 
                JOptionPane.INFORMATION_MESSAGE
        );
        
        running = false;
        client.closeEverything();
        this.dispose();
        try {
            new Home(client = new Client(new Socket(Const.HOST, Const.PORT), user)).setVisible(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    private ImageIcon convertImage(String path) {
        ImageIcon originalIcon = new ImageIcon(path);
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        int newWidth = 350;
        int newHeight = (int) ((double) originalHeight / originalWidth * newWidth);
        Image scaledImage = 
                originalIcon.getImage()
                .getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                
        return new ImageIcon(scaledImage);
    }
    
    private void markPos(JLabel label, String shape, int x, int y) {
        SwingUtilities.invokeLater(() -> {
            Graphics2D g2d = (Graphics2D) label.getGraphics();

            g2d.setStroke(new BasicStroke(4.0f));
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            
            if(shape.equals("O")) {
                g2d.setColor(Color.GREEN);
                g2d.drawOval(
                        x - Const.SHAPE_SIZE / 2, y - Const.SHAPE_SIZE / 2, 
                        Const.SHAPE_SIZE, Const.SHAPE_SIZE
                );
            } else {
                g2d.setColor(Color.RED);
                g2d.drawLine(
                        x - Const.SHAPE_SIZE / 2, y - Const.SHAPE_SIZE / 2, 
                        x + Const.SHAPE_SIZE / 2, y + Const.SHAPE_SIZE / 2
                );
                g2d.drawLine(
                        x + Const.SHAPE_SIZE / 2, y - Const.SHAPE_SIZE / 2, 
                        x - Const.SHAPE_SIZE / 2, y + Const.SHAPE_SIZE / 2
                );
            }
        });
    }
    
    private void openNextCard() {
        CardLayout cartLayout = (CardLayout) jPanel1.getLayout();
        cartLayout.next(jPanel1);
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        ruleTitleLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ruleDescTextArea = new javax.swing.JTextArea();
        notiLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        image2Label = new javax.swing.JLabel();
        image1Label = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        scoreTable = new javax.swing.JTable();
        clockLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        stopButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setLayout(new java.awt.CardLayout());

        ruleTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ruleTitleLabel.setText("Game Rules");

        ruleDescTextArea.setColumns(20);
        ruleDescTextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ruleDescTextArea.setRows(5);
        jScrollPane2.setViewportView(ruleDescTextArea);

        notiLabel.setText("jLabel1");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 84, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(notiLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(315, 315, 315)
                .addComponent(ruleTitleLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(notiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ruleTitleLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, "card3");

        image2Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                image2LabelMouseClicked(evt);
            }
        });

        image1Label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                image1LabelMouseClicked(evt);
            }
        });

        messageLabel.setText("jLabel2");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(image1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(image2Label, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
                    .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(messageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(image1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(image2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, "card2");

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        scoreTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "User 1", "User 2"
            }
        ));
        jScrollPane1.setViewportView(scoreTable);
        if (scoreTable.getColumnModel().getColumnCount() > 0) {
            scoreTable.getColumnModel().getColumn(0).setHeaderValue("User 1");
            scoreTable.getColumnModel().getColumn(1).setResizable(false);
            scoreTable.getColumnModel().getColumn(1).setHeaderValue("User 2");
        }

        clockLabel.setText("jLabel1");

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        titleLabel.setText("In Game");

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(startButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stopButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titleLabel)
                .addGap(301, 301, 301))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(startButton)
                        .addComponent(stopButton))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(clockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void image1LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_image1LabelMouseClicked
        sendClick(new Click(evt.getX(), evt.getY(), user, game));
    }//GEN-LAST:event_image1LabelMouseClicked

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        startButton.setEnabled(false);
        readyCount += 1;
        sendReadyState();
    }//GEN-LAST:event_startButtonActionPerformed

    private void image2LabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_image2LabelMouseClicked
        sendClick(new Click(evt.getX(), evt.getY(), user, game));
    }//GEN-LAST:event_image2LabelMouseClicked

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        int response = JOptionPane.showConfirmDialog(
                this, 
                "You want to stop this game, right?", 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION
        );
        
        if(response == JOptionPane.YES_OPTION) {
            sendStopRequest();
        
            running = false;
            client.closeEverything();
            this.dispose();
            try {
                new Home(client = new Client(new Socket(Const.HOST, Const.PORT), user)).setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_stopButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clockLabel;
    private javax.swing.JLabel image1Label;
    private javax.swing.JLabel image2Label;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JLabel notiLabel;
    private javax.swing.JTextArea ruleDescTextArea;
    private javax.swing.JLabel ruleTitleLabel;
    private javax.swing.JTable scoreTable;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
