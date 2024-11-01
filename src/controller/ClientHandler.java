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
import model.User;
import model.UserDao;

public class ClientHandler implements Runnable{
    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    private static List<GroupHandler> groupHandlers = new ArrayList<>();
    private ClientHandler partner;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User clientUser;
    
    public ClientHandler (Socket socket) {
        try {
            this.partner = null;
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.clientUser = (User) objectInputStream.readObject();
            
            requestAddUser();
            clientHandlers.add(this);
            
        } catch(IOException | ClassNotFoundException e) {
            closeEverything();
            e.printStackTrace();
        }
    }

    public String getClientUsername() {
        return clientUser.getUsername();
    }
    
    public void sendMessage(Message message) {
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
            users.add(clientHandler.clientUser);
        }
        sendMessage(new Message("2", users));
    }
    
    
    private void requestAddUser() {
        for(ClientHandler clientHandler: clientHandlers) {
            clientHandler.sendMessage(new Message("3", this.clientUser));
        }
    }
    
    private void transferRemoveUserRequest(Message message) {
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler != this) {
                clientHandler.sendMessage(new Message("4" , this.clientUser));
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
            if(clientHandler.clientUser.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("6", this.clientUser));
                break;
            }
        }
    }
    
    private void transferAgreementResponse(Message message) {
        User receiver = (User) message.getObject();
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.clientUser.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("9", this.clientUser));
                break;
            }
        }
    }
    
    private void transferRejectResponse(Message message) {
        User receiver = (User) message.getObject();
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.clientUser.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("10", this.clientUser));
                break;
            }
        }
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
