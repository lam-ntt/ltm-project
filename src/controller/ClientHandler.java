/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import model.User;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static ArrayList<GroupHandler> groupHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User clientUser;
    private Thread thread;
    
    public ClientHandler (Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.clientUser = (User) objectInputStream.readObject();
            clientHandlers.add(this);
            
//            thread = new Thread(this);
//            thread.start();
            
        } catch(IOException | ClassNotFoundException e) {
            closeEverything();
            e.printStackTrace();
        }
    }

    public String getClientUsername() {
        return clientUser.getUsername();
    }

    @Override
    public void run() {
        String messageFromClient;
        try {
            while(true) {
                if(socket.isConnected()) {
                    messageFromClient = (String) objectInputStream.readObject();
                    if(messageFromClient.equals("Close")) {
                        closeEverything();
                        break;
                    }
                }
            }
        } catch(IOException | ClassNotFoundException e) {
            closeEverything();
            e.printStackTrace();
        }
    }
    
    public void broadcastMessage(String messageToSend) {
        for(ClientHandler clientHandler: clientHandlers) {
            try {
                if(!clientHandler.clientUser.getUsername().equals(this.clientUser.getUsername())) {
                    clientHandler.objectOutputStream.writeObject(messageToSend);
                }
            } catch (IOException e) {
                closeEverything();
                e.printStackTrace();
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
