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

/**
 *
 * @author nguye
 */
public class Handler implements Runnable{
    public static ArrayList<Handler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String clientUsername;
    
    public Handler (Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.clientUsername = (String) objectInputStream.readObject();
            
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
            
        } catch(Exception e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
            e.printStackTrace();
        }
    }

    public String getClientUsername() {
        return clientUsername;
    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()) {
            try {
                messageFromClient = (String) objectInputStream.readObject();
                broadcastMessage(messageFromClient);
            } catch(Exception e) {
                closeEverything(socket, objectInputStream, objectOutputStream);
                e.printStackTrace();
                break;
            }
        }
    }
    
    public void broadcastMessage(String messageToSend) {
        for(Handler clientHandler: clientHandlers) {
            try {
                if(!clientHandler.clientUsername.equals(this.clientUsername)) {
                    clientHandler.objectOutputStream.writeObject(messageToSend);
                }
            } catch (IOException e) {
                closeEverything(socket, objectInputStream, objectOutputStream);
                e.printStackTrace();
            }
        }
    }
    
    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }
    
    public void closeEverything(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        removeClientHandler();
        try {
            if(objectInputStream != null) {
                objectInputStream.close();
            }
            
            if(objectOutputStream != null) {
                objectOutputStream.close();
            }
            
            if(socket != null) {
                socket.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
