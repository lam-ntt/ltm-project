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
import model.User;
import view.Home;

public class Client {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User user;
    private Home home;

    public Client(Socket socket, User user) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.user = user;
            this.home = home;
            objectOutputStream.writeObject(user);
            listenForMessage();
            if (this.user == null) {
                System.out.println("User  is null!");
            } else {
                System.out.println("User  initialized: " + this.user.getUsername());
            }
        } catch (IOException e) {
            closeEverything();
            e.printStackTrace();
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (socket.isConnected()) {
                            // Đọc thông báo từ server
                            String message = (String) objectInputStream.readObject();
                            if (message.startsWith("Online Users:")) {
                                String online = message.substring("Online Users: ".length()).trim();
                                online = online.replace("[", "").replace("]", "").trim();
                                String[] onlineUsers = online.split(",");

                                home.updateOnlineUser(onlineUsers);
                            }
                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    closeEverything();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        // Send message should be implemented in Frame
        // This is just for test
        try {
            while (true) {
                if (socket.isConnected()) {
                    objectOutputStream.writeObject(message);
                    break;
                }
            }

        } catch (IOException e) {
            closeEverything();
            e.printStackTrace();
        }
    }

    public void closeEverything() {
        try {
            String message = "Close";
            objectOutputStream.writeObject(message);

            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();

        Socket socket = new Socket("localhost", 1234);

//        Client client = new Client(socket, new User(username, "123", 0, 0, 0));
//        client.listenForMessage();
//        client.sendMessage();
    }
}
