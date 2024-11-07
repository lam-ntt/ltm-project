/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

public class Click implements Serializable {
    private int id, x, y;
    private User user;
    private Game game;

    public Click() {
    }

    public Click(int x, int y, User user, Game game) {
        this.x = x;
        this.y = y;
        this.user = user;
        this.game = game;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGame(Game game) {
        this.game = game;
    } 
}
