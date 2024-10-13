/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nguye
 */
public class User {
    private int id;
    private String username, password;
    private int win, tie, lose;

    public User() {
    }

    public User(String username, String password, int win, int tie, int lose) {
        this.username = username;
        this.password = password;
        this.win = win;
        this.tie = tie;
        this.lose = lose;
    }

    public User(int id, String username, String password, int win, int tie, int lose) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.win = win;
        this.tie = tie;
        this.lose = lose;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getWin() {
        return win;
    }

    public int getTie() {
        return tie;
    }

    public int getLose() {
        return lose;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public void setTie(int tie) {
        this.tie = tie;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }
}
