/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import helper.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import model.User;

public class Client {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User user;
    
    public Client(Socket socket, User user) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.user = user;
            objectOutputStream.writeObject(user);
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
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
    
    public void closeEverything() {
        try {
            sendMessage(new Message("RQ-CLOSE"));
            if(objectInputStream != null) objectInputStream.close();
            if(objectOutputStream != null) objectOutputStream.close();
            if(socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
