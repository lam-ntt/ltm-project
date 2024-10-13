/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nguye
 */
public class Point {
    private int id, x, y;
    private Pair pair;

    public Point() {
    }

    public Point(int x, int y, Pair pair) {
        this.x = x;
        this.y = y;
        this.pair = pair;
    }

    public Point(int id, int x, int y, Pair pair) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.pair = pair;
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

    public Pair getPair() {
        return pair;
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

    public void setPair(Pair pair) {
        this.pair = pair;
    }
}
