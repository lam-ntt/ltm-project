/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Game;
import model.GameDao;
import model.Point;
import model.PointDao;
import model.User;
import model.UserDao;

public class ClientHandler implements Runnable{
    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    private static List<GroupHandler> groupHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ClientHandler clientParner;
    private User user;
    private Game game;
    private List<Point> points = new ArrayList<>();
    private boolean isLoadingGame = false;
    
    public ClientHandler (Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.clientParner = null;
            this.user = (User) objectInputStream.readObject();
            
            requestAddUser();
            clientHandlers.add(this);
            
        } catch(IOException | ClassNotFoundException e) {
            closeEverything();
            e.printStackTrace();
        }
    }

    public String getClientUsername() {
        return user.getUsername();
    }
    
    public synchronized void sendMessage(Message message) {
        try {
            while(true) {
                if(socket.isConnected()) {
                    objectOutputStream.writeObject(message);
                    break;
                }
            }
            
        } catch(IOException e) {
            closeEverything();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                if(!socket.isConnected()) break;
                
                try {
                    Message message = (Message) objectInputStream.readObject();
                    switch (message.getCode()) {
                        case "0" -> closeEverything();
                        case "1" -> handleGetAllUserRequest(message);
                        case "2" -> handleGetAllOnlineUserRequest(message);
                        case "4" -> transferRemoveUserRequest(message);
                        case "5" -> transferInviteRequest(message);
                        case "7" -> transferAgreementResponse(message);
                        case "8" -> transferRejectResponse(message);
                        case "12" -> {
                            clientParner.sendMessage(new Message("12", user));
                        }
                        case "13" -> {
                            if(isLoadingGame == false) {
                                setUpRandomGame();
                                sendMessage(new Message("13", game.getPair()));
                                clientParner.sendMessage(new Message("13", game.getPair()));
                            }
                            
//                            if(isLoadingGame == false) {
//                                setUpRandomGame();
//                            }
//                            sendMessage(new Message("13", game.getPair()));
                        }
                        default -> {
                        }
                    }
                } catch (SocketException e) {
                    break;
                }
            }
        } catch(IOException | ClassNotFoundException e) {
            closeEverything();
            e.printStackTrace();
        }
    }
    
    
    
    private void handleGetAllUserRequest(Message message) {
        List<User> users = UserDao.getAllUsers();
        sendMessage(new Message("1", users));
    }
    
    private void handleGetAllOnlineUserRequest(Message message) {
        List<User> users = new ArrayList();
        for(ClientHandler clientHandler: clientHandlers) {
            users.add(clientHandler.user);
        }
        sendMessage(new Message("2", users));
    }
    
    
    private void requestAddUser() {
        for(ClientHandler clientHandler: clientHandlers) {
            clientHandler.sendMessage(new Message("3", this.user));
        }
    }
    
    private void transferRemoveUserRequest(Message message) {
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler != this) {
                clientHandler.sendMessage(new Message("4" , this.user));
            }
        }
    }
    
    
    private void transferInviteRequest(Message message) {
        User receiver = (User) message.getObject();
        for(GroupHandler groupHandler: groupHandlers) {
            if(groupHandler.clientHandler1.getClientUsername().equals(receiver.getUsername()) || 
                    groupHandler.clientHandler2.getClientUsername().equals(receiver.getUsername())) {
                sendMessage(new Message("11", receiver));
                return;
            }
        }
        
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.user.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("6", this.user));
                break;
            }
        }
    }
    
    private void transferAgreementResponse(Message message) {
        User receiver = (User) message.getObject();
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.user.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("9", this.user));
                
                GroupHandler groupHandler = new GroupHandler(this, clientHandler);
                groupHandlers.add(groupHandler);
                clientParner = clientHandler;
                clientHandler.clientParner = this;
                break;
            }
        }
    }
    
    private void transferRejectResponse(Message message) {
        User receiver = (User) message.getObject();
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.user.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("10", this.user));
                break;
            }
        }
    }
    
    
    private void setUpRandomGame() {
        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1;
        
        int gameId = GameDao.createGameWithPairId(
                user.getId(), 
                clientParner.user.getId(), 
                randomNumber
        );
        
        this.game = GameDao.getGame(gameId);
        this.points = PointDao.getAllPointWithPairId(this.game.getPair().getId());
        this.isLoadingGame = true;
        
        clientParner.game = this.game;
        clientParner.points = this.points;
        clientParner.isLoadingGame = true;
    }
    
    
    public void removeClientHandler() {
        clientHandlers.remove(this);
    }
    
    public void closeEverything() {
        try {
            removeClientHandler();
            if(objectInputStream != null) objectInputStream.close();
            if(objectOutputStream != null) objectOutputStream.close();
            if(socket != null) socket.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
