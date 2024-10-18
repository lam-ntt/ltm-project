/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 *
 * @author nguye
 */
public class Game implements Serializable {
    private int id;
    private User user1, user2;
    private Pair pair;
    private int score1, score2;
    private String stateString;

    public Game() {
    }

    public Game(User user1, User user2, Pair pair, int score1, int score2, String stateString) {
        this.user1 = user1;
        this.user2 = user2;
        this.pair = pair;
        this.score1 = score1;
        this.score2 = score2;
        this.stateString = stateString;
    }

    public Game(int id, User user1, User user2, Pair pair, int score1, int score2, String stateString) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.pair = pair;
        this.score1 = score1;
        this.score2 = score2;
        this.stateString = stateString;
    }

    public int getId() {
        return id;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public Pair getPair() {
        return pair;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public String getStateString() {
        return stateString;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public void setStateString(String stateString) {
        this.stateString = stateString;
    }
}
