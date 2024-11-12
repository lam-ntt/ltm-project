/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author nguye
 */
public class GroupHandler {
    public ClientHandler clientHandler1, clientHandler2;

    public GroupHandler(ClientHandler clientHandler1, ClientHandler clientHandler2) {
        this.clientHandler1 = clientHandler1;
        this.clientHandler2 = clientHandler2;
    }
}
