/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author nguye
 */
public class Client {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String username;
    
    
    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.username = username;
        } catch(Exception e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
            e.printStackTrace();
        }
    }
    
    public void sendMessage() {
        try {
            objectOutputStream.writeObject(username);
            
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()) {
                String messagetoSend = scanner.nextLine();
                objectOutputStream.writeObject(username + ": " + messagetoSend);
            }
            
        } catch(Exception e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
            e.printStackTrace();
        }
    }
    
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while(socket.isConnected()) {
                    try {
                        msgFromGroupChat = (String) objectInputStream.readObject();
                        System.out.println(msgFromGroupChat);
                    } catch(Exception e) {
                        closeEverything(socket, objectInputStream, objectOutputStream);
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    public void closeEverything(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();
        
        Socket socket = new Socket("localhost", 1234);
        
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}
