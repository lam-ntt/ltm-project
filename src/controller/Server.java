/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    public void startServer() {
        try {
            while(!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client is connected!");

                ClientHandler clientHandler = new ClientHandler(socket);
                System.out.println(clientHandler.getClientUsername());
                
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void closeServer() {
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
