/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import model.User;

public class Client {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Scanner scanner;
    private User user;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.scanner = new Scanner(System.in);
        } catch (IOException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            System.out.println("Start chatting! Type 'exit' to quit.");

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                if (messageToSend.equalsIgnoreCase("exit")) {
                    closeEverything(socket, objectInputStream, objectOutputStream);
                    break;
                }
                objectOutputStream.writeObject(user.getUsername() + ": " + messageToSend);
            }
        } catch (IOException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
            e.printStackTrace();
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            String msgFromGroupChat;
            while (socket.isConnected()) {
                try {
                    msgFromGroupChat = (String) objectInputStream.readObject();
                    System.out.println(msgFromGroupChat);
                } catch (IOException | ClassNotFoundException e) {
                    closeEverything(socket, objectInputStream, objectOutputStream);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void register() {
        System.out.print("Enter username to register: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            String command = "register;" + username + ";" + password;
            objectOutputStream.writeObject(command);

            String response = (String) objectInputStream.readObject();
            System.out.println(response);

            if (response.contains("successful")) {
                this.user = new User(username, password); // Lưu thông tin người dùng đã đăng ký
            }
        } catch (IOException | ClassNotFoundException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
            e.printStackTrace();
        }
    }

    public void login() {
        System.out.print("Enter username to login: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            String command = "login;" + username + ";" + password;
            objectOutputStream.writeObject(command);

            String response = (String) objectInputStream.readObject();
            System.out.println(response);

            if (response.contains("successful")) {
                this.user = new User(username, password); // Lưu thông tin người dùng đã đăng nhập
            }
        } catch (IOException | ClassNotFoundException e) {
            closeEverything(socket, objectInputStream, objectOutputStream);
            e.printStackTrace();
        }
    }

    public void closeEverything(Socket socket, ObjectInputStream input, ObjectOutputStream output) {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            if (scanner != null) scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket);

            System.out.println("Choose an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                client.register();
            } else if (choice == 2) {
                client.login();
            } else {
                System.out.println("Invalid choice");
                return;
            }

            if (client.user != null) { // Nếu đăng nhập/đăng ký thành công
                client.listenForMessage();
                client.sendMessage();
            } else {
                System.out.println("Please try again.");
            }
        } catch (UnknownHostException e) {
            System.out.println("Server not found.");
        } catch (IOException e) {
            System.out.println("I/O error occurred.");
        }
    }
}
